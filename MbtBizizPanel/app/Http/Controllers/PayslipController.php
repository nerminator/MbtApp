<?php

namespace App\Http\Controllers;

use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Redis;
use Illuminate\Support\Facades\Validator;
use Shuchkin\SimpleXLSX;

class PayslipController extends Controller
{
    private const REDIS_KEY = 'payslip_active';
    private const REDIS_DEACTIVATION_MESSAGE_KEY = 'payslip_deactivation_error_message';
    private const REDIS_PERIOD_NOT_OPEN_MESSAGE_KEY = 'payslip_period_not_open_error_message';

    public function __construct()
    {
        $this->middleware('auth');
    }

    public function index(Request $request)
    {
        $selectedYear = (int) $request->query('year', now()->year);

        $years = DB::table('payslip_months')
            ->selectRaw('YEAR(donem) as y')
            ->distinct()
            ->orderByDesc('y')
            ->pluck('y')
            ->map(fn ($year) => (int) $year)
            ->values()
            ->toArray();

        if (count($years) > 0 && !in_array($selectedYear, $years, true)) {
            $selectedYear = $years[0];
        }

        $rows = DB::table('payslip_months')
            ->select(['donem', 'yaka_turu', 'baslangic_tarihi', 'bitis_tarihi'])
            ->when($selectedYear > 0, function ($query) use ($selectedYear) {
                $query->whereYear('donem', $selectedYear);
            })
            ->orderBy('donem', 'desc')
            ->orderByRaw("FIELD(yaka_turu, 'Mavi', 'Beyaz')")
            ->get();

        $isActive = Redis::get(self::REDIS_KEY) === '1';
        $deactivationErrorMessage = (string) (Redis::get(self::REDIS_DEACTIVATION_MESSAGE_KEY) ?? '');
        $periodNotOpenErrorMessage = (string) (Redis::get(self::REDIS_PERIOD_NOT_OPEN_MESSAGE_KEY) ?? '');

        return view('payslip', [
            'isActive' => $isActive,
            'deactivationErrorMessage' => $deactivationErrorMessage,
            'periodNotOpenErrorMessage' => $periodNotOpenErrorMessage,
            'rows' => $rows,
            'years' => $years,
            'selectedYear' => $selectedYear,
        ]);
    }

    public function toggle()
    {
        $newValue = Redis::get(self::REDIS_KEY) === '1' ? '0' : '1';
        Redis::set(self::REDIS_KEY, $newValue);

        return back()->with('success', $newValue === '1' ? 'Payslip activated.' : 'Payslip deactivated.');
    }

    public function savePeriodNotOpenMessage(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'period_not_open_error_message' => 'nullable|string|max:2000',
        ]);

        if ($validator->fails()) {
            return back()->with('error', 'Error message is invalid.');
        }

        $message = trim((string) $request->input('period_not_open_error_message', ''));
        Redis::set(self::REDIS_PERIOD_NOT_OPEN_MESSAGE_KEY, $message);

        return back()->with('success', 'Period not opened error message saved.');
    }

    public function saveDeactivationMessage(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'deactivation_error_message' => 'nullable|string|max:2000',
        ]);

        if ($validator->fails()) {
            return back()->with('error', 'Error message is invalid.');
        }

        $message = trim((string) $request->input('deactivation_error_message', ''));
        Redis::set(self::REDIS_DEACTIVATION_MESSAGE_KEY, $message);

        return back()->with('success', 'Deactivation error message saved.');
    }

    public function import(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'excel_file' => 'required|file|mimes:xlsx|max:10240',
        ]);

        if ($validator->fails()) {
            return back()->with('error', 'Please upload a valid .xlsx file (max 10MB).');
        }

        $path = $request->file('excel_file')->getRealPath();
        $xlsx = SimpleXLSX::parse($path);

        if (!$xlsx) {
            return back()->with('error', 'Excel file could not be parsed.');
        }

        $rows = $xlsx->rows();
        if (count($rows) <= 1) {
            return back()->with('error', 'Excel file is empty.');
        }

        $upsertRows = [];
        $invalidRows = 0;

        foreach ($rows as $index => $row) {
            if ($index === 0) {
                continue;
            }

            $donemRaw = trim((string)($row[0] ?? ''));
            $yakaTuruRaw = trim((string)($row[1] ?? ''));
            $baslangicRaw = trim((string)($row[2] ?? ''));
            $bitisRaw = trim((string)($row[3] ?? ''));

            if ($donemRaw === '' && $yakaTuruRaw === '' && $baslangicRaw === '' && $bitisRaw === '') {
                continue;
            }

            $donem = $this->parseDonem($donemRaw);
            $baslangic = $this->parseDate($baslangicRaw);
            $bitis = $this->parseDate($bitisRaw);

            $yakaTuru = $yakaTuruRaw === 'Mavi' ? 'Mavi' : ($yakaTuruRaw === 'Beyaz' ? 'Beyaz' : null);

            if (!$donem || !$baslangic || !$bitis || !$yakaTuru) {
                $invalidRows++;
                continue;
            }

            $upsertRows[] = [
                'donem' => $donem,
                'yaka_turu' => $yakaTuru,
                'baslangic_tarihi' => $baslangic,
                'bitis_tarihi' => $bitis,
                'updated_at' => now(),
                'created_at' => now(),
            ];
        }

        if (count($upsertRows) === 0) {
            return back()->with('error', 'No valid row found in the Excel file.');
        }

        DB::table('payslip_months')->upsert(
            $upsertRows,
            ['donem', 'yaka_turu'],
            ['baslangic_tarihi', 'bitis_tarihi', 'updated_at']
        );

        $message = count($upsertRows) . ' row(s) imported/updated successfully.';
        if ($invalidRows > 0) {
            $message .= ' ' . $invalidRows . ' row(s) skipped due to invalid format.';
        }

        return redirect()->route('payslip')->with('success', $message);
    }

    private function parseDonem(string $value): ?string
    {
        $value = trim($value);

        if (preg_match('/^\d{4}-\d{2}$/', $value)) {
            return $value . '-01';
        }

        if (preg_match('/^\d{4}-\d{2}-\d{2}$/', $value)) {
            try {
                $date = Carbon::createFromFormat('Y-m-d', $value);
                return $date->startOfMonth()->toDateString();
            } catch (\Throwable $e) {
                return null;
            }
        }

        if (is_numeric($value)) {
            $date = $this->excelSerialToDate($value);
            return $date ? $date->startOfMonth()->toDateString() : null;
        }

        return null;
    }

    private function parseDate(string $value): ?string
    {
        $value = trim($value);

        if ($value === '') {
            return null;
        }

        $formats = ['d-m-Y', 'Y-m-d', 'd/m/Y', 'd.m.Y'];

        foreach ($formats as $format) {
            try {
                $date = Carbon::createFromFormat($format, $value);
                return $date->toDateString();
            } catch (\Throwable $e) {
            }
        }

        if (is_numeric($value)) {
            $date = $this->excelSerialToDate($value);
            return $date ? $date->toDateString() : null;
        }

        return null;
    }

    private function excelSerialToDate(string $value): ?Carbon
    {
        if (!is_numeric($value)) {
            return null;
        }

        try {
            $serial = (int) round((float) $value);
            return Carbon::create(1899, 12, 30)->addDays($serial);
        } catch (\Throwable $e) {
            return null;
        }
    }
}
