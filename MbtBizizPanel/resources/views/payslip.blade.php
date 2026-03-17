@extends('layouts.app')

@section('content')
    <div class="container-fluid" style="margin-top: 25px;">
        <div class="row content">
            @include('leftsidebar')

            <div class="col-md-9" style="margin-top: 15px;">
                <h3 style="margin-bottom: 25px;">Payslip</h3>

                @if(session('success'))
                    <div class="alert alert-success">{{ session('success') }}</div>
                @endif

                @if(session('error'))
                    <div class="alert alert-danger">{{ session('error') }}</div>
                @endif

                <div class="eventdiv active" style="padding: 15px; margin-bottom: 20px;">
                    <div class="row" style="display: flex; align-items: center;">
                        <div class="col-md-8">
                            <strong>Payslip Status:</strong>
                            <span style="margin-left: 8px;" class="label {{ $isActive ? 'label-success' : 'label-default' }}">
                                {{ $isActive ? 'Active' : 'Inactive' }}
                            </span>
                        </div>
                        <div class="col-md-4" style="text-align: right;">
                            <form method="POST" action="{{ route('payslip.toggle') }}" style="display: inline;">
                                @csrf
                                <button type="submit" class="btn {{ $isActive ? 'btn-warning' : 'btn-success' }}">
                                    {{ $isActive ? 'Deactivate' : 'Activate' }}
                                </button>
                            </form>
                        </div>
                    </div>

                    <div class="row" style="margin-top: 20px;">
                        <div class="col-md-12" style="margin-bottom: 15px;">
                            <form method="POST" action="{{ route('payslip.deactivationMessage') }}">
                                @csrf
                                <label for="deactivation_error_message" style="display:block; margin-bottom: 8px;">
                                    Error Message for deactivation
                                </label>
                                <textarea
                                    id="deactivation_error_message"
                                    name="deactivation_error_message"
                                    class="form-control"
                                    rows="3"
                                    maxlength="2000"
                                    placeholder="Enter the message shown when payslip is globally deactivated...">{{ $deactivationErrorMessage }}</textarea>
                                <button type="submit" class="btn btn-primary" style="margin-top: 10px;">Save Message</button>
                            </form>
                        </div>

                        <div class="col-md-12">
                            <form method="POST" action="{{ route('payslip.periodNotOpenMessage') }}">
                                @csrf
                                <label for="period_not_open_error_message" style="display:block; margin-bottom: 8px;">
                                    Error Message for period not opened yet
                                </label>
                                <textarea
                                    id="period_not_open_error_message"
                                    name="period_not_open_error_message"
                                    class="form-control"
                                    rows="3"
                                    maxlength="2000"
                                    placeholder="Enter the message shown when selected payslip period is not opened yet...">{{ $periodNotOpenErrorMessage }}</textarea>
                                <button type="submit" class="btn btn-primary" style="margin-top: 10px;">Save Message</button>
                            </form>
                        </div>
                    </div>
                </div>

                <div class="eventdiv active" style="padding: 15px; margin-bottom: 20px;">
                    <form action="{{ route('payslip.import') }}" method="POST" enctype="multipart/form-data" class="form-inline" style="gap: 10px; display: flex; flex-wrap: wrap;">
                        @csrf
                        <div class="form-group">
                            <label for="excel_file" style="margin-right: 10px;">Import Excel:</label>
                            <input type="file" name="excel_file" id="excel_file" class="form-control" accept=".xlsx" required>
                        </div>
                        <button type="submit" class="btn btn-primary">Import</button>
                    </form>
                </div>

                <div class="eventdiv active" style="padding: 15px; margin-bottom: 20px;">
                    <form method="GET" action="{{ url('/payslip') }}" class="form-inline" style="margin-bottom: 15px;">
                        <label for="year" style="margin-right: 10px;">Year:</label>
                        <select name="year" id="year" class="form-control" style="margin-right: 10px;">
                            @foreach($years as $year)
                                <option value="{{ $year }}" {{ (int)$selectedYear === (int)$year ? 'selected' : '' }}>{{ $year }}</option>
                            @endforeach
                        </select>
                        <button type="submit" class="btn btn-default">Filter</button>
                    </form>

                    <div class="table-responsive">
                        <table class="table table-bordered table-striped" style="background: #fff; color: #222;">
                            <thead>
                            <tr>
                                <th>Dönem</th>
                                <th>Yaka Türü</th>
                                <th>Başlangıç Tarihi</th>
                                <th>Bitiş Tarihi</th>
                            </tr>
                            </thead>
                            <tbody>
                            @forelse($rows as $row)
                                <tr>
                                    <td>{{ \Carbon\Carbon::parse($row->donem)->format('Y-m') }}</td>
                                    <td>{{ $row->yaka_turu }}</td>
                                    <td>{{ \Carbon\Carbon::parse($row->baslangic_tarihi)->format('d-m-Y') }}</td>
                                    <td>{{ \Carbon\Carbon::parse($row->bitis_tarihi)->format('d-m-Y') }}</td>
                                </tr>
                            @empty
                                <tr>
                                    <td colspan="4" style="text-align: center;">No data found.</td>
                                </tr>
                            @endforelse
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
@endsection
