package com.daimlertruck.dtag.internal.android.mbt.test.ui.main.orchestra;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Base64;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseActivity;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.test.base.SingleLiveEvent;
import com.daimlertruck.dtag.internal.android.mbt.databinding.ActivityPayslipPeriodBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponse;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.profile.PayslipEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.AbstractApiUtils;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.NetworkCallback;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.main.main.MainActivity;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class PayslipPeriodActivity extends BaseActivity<ActivityPayslipPeriodBinding> {

    @Inject
    AbstractApiUtils apiUtils;

    private Spinner spinnerMonth, spinnerYear;
    private Button btnShowPayslip;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_payslip_period; // you can reuse fragment_payslip_period.xml as this layout
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseApplication) getApplication()).getComponent().inject(this);

        spinnerMonth = findViewById(R.id.spinnerMonth);
        spinnerYear = findViewById(R.id.spinnerYear);
        btnShowPayslip = findViewById(R.id.btnShowPayslip);

        ArrayAdapter<Integer> monthAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item_light, getMonths());
        monthAdapter.setDropDownViewResource(R.layout.spinner_item_light);
        spinnerMonth.setAdapter(monthAdapter);

        ArrayAdapter<Integer> yearAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item_light, getYears());
        yearAdapter.setDropDownViewResource(R.layout.spinner_item_light);
        spinnerYear.setAdapter(yearAdapter);

        Calendar cal = Calendar.getInstance();
        spinnerMonth.setSelection(cal.get(Calendar.MONTH));
        spinnerYear.setSelection(0);

        btnShowPayslip.setOnClickListener(v -> {
            int selectedMonth = (int) spinnerMonth.getSelectedItem();
            int selectedYear = (int) spinnerYear.getSelectedItem();
            fetchPayslip(selectedYear, selectedMonth);
        });
    }

    private List<Integer> getMonths() {
        List<Integer> months = new ArrayList<>();
        for (int i = 1; i <= 12; i++) months.add(i);
        return months;
    }

    private List<Integer> getYears() {
        List<Integer> years = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int y = currentYear; y >= currentYear - 5; y--) years.add(y);
        return years;
    }

    private void fetchPayslip(int selectedYear, int selectedMonth) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("year", selectedYear);
        body.put("month", selectedMonth);

        showProgressDialog();
        apiUtils.fetchPayslip(body, new NetworkCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                dismissProgressDialog();

                // Check for non-success status and show backend error message
                if (response.getStatuscode() == null || response.getStatuscode() != 200) {
                    String errorMsg = response.getErrormessage();
                    if (errorMsg == null || errorMsg.trim().isEmpty()) {
                        errorMsg = getString(R.string.TXT_PAYSLIP_NOT_FOUND);
                    }

                    // Session expired (401) → show error and go back to profile
                    if (response.getStatuscode() != null && response.getStatuscode() == 401) {
                        new AlertDialog.Builder(PayslipPeriodActivity.this)
                                .setTitle(R.string.TXT_COMMON_ERROR)
                                .setMessage(errorMsg)
                                .setPositiveButton(R.string.TXT_COMMON_DONE, (d, w) -> {
                                    navigateBackToProfile();
                                })
                                .setCancelable(false)
                                .show();
                        return;
                    }

                    new AlertDialog.Builder(PayslipPeriodActivity.this)
                            .setTitle(R.string.TXT_COMMON_ERROR)
                            .setMessage(errorMsg)
                            .setPositiveButton(R.string.TXT_COMMON_DONE, null)
                            .show();
                    return;
                }

                try {
                    JSONObject data = new JSONObject(new Gson().toJson(response.getResponseData()));
                    String base64 = data.optString("base64", "");

                    if (base64.isEmpty()) {
                        Toast.makeText(PayslipPeriodActivity.this, R.string.TXT_PAYSLIP_NOT_FOUND, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    byte[] pdfAsBytes = Base64.decode(base64, Base64.DEFAULT);

                    String fileName = String.format("%02d_%d_payslip.pdf", selectedMonth, selectedYear);
                    File file = new File(getCacheDir(), fileName);
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        fos.write(pdfAsBytes);
                    }

                    // Open PDF viewer
                    Uri pdfUri = FileProvider.getUriForFile(
                            PayslipPeriodActivity.this,
                            getPackageName() + ".provider",
                            file
                    );
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(pdfUri, "application/pdf");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        // No external PDF viewer — render in-app
                        showPdfInApp(file);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(PayslipPeriodActivity.this,
                            String.format(getString(R.string.TXT_PAYSLIP_PDF_OPEN_ERROR), e.getLocalizedMessage()),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onServiceFailure(int code, String message) {
                dismissProgressDialog();
                if (code == 400 || code == 401) {
                    String errorMsg = (message != null && !message.trim().isEmpty())
                            ? message
                            : getString(R.string.TXT_PAYSLIP_PERIOD_TOAST3);
                    new AlertDialog.Builder(PayslipPeriodActivity.this)
                            .setTitle(R.string.TXT_COMMON_ERROR)
                            .setMessage(errorMsg)
                            .setPositiveButton(R.string.TXT_COMMON_DONE, (d, w) -> {
                                navigateBackToProfile();
                            })
                            .setCancelable(false)
                            .show();
                } else {
                    String errorMsg = (message != null && !message.trim().isEmpty())
                            ? message
                            : getString(R.string.TXT_COMMON_CONNECTION_ERROR);
                    Toast.makeText(PayslipPeriodActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNetworkFailure(Throwable t) {
                dismissProgressDialog();
                Toast.makeText(PayslipPeriodActivity.this, R.string.TXT_COMMON_CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void navigateBackToProfile() {
        Intent intent = new Intent(this, com.daimlertruck.dtag.internal.android.mbt.test.ui.main.main.MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void showPdfInApp(File file) {
        try {
            ParcelFileDescriptor fd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            PdfRenderer renderer = new PdfRenderer(fd);
            PdfRenderer.Page page = renderer.openPage(0);

            Bitmap bitmap = Bitmap.createBitmap(page.getWidth() * 2, page.getHeight() * 2, Bitmap.Config.ARGB_8888);
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            page.close();
            renderer.close();
            fd.close();

            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(bitmap);
            imageView.setPadding(0, 0, 0, 0);

            new AlertDialog.Builder(this)
                    .setTitle(file.getName().replace(".pdf", ""))
                    .setView(imageView)
                    .setPositiveButton(R.string.TXT_COMMON_DONE, null)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this,
                    String.format(getString(R.string.TXT_PAYSLIP_PDF_OPEN_ERROR), e.getLocalizedMessage()),
                    Toast.LENGTH_SHORT).show();
        }
    }
}