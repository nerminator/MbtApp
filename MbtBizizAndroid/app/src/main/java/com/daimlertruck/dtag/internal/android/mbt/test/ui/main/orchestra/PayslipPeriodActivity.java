package com.daimlertruck.dtag.internal.android.mbt.test.ui.main.orchestra;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
        apiUtils.fetchPayslip(body, new NetworkCallback<BaseResponse<PayslipEntity>>() {
            @Override
            public void onSuccess(BaseResponse response) {
                dismissProgressDialog();
                try {
                    JSONObject data = new JSONObject(new Gson().toJson(response.getResponseData()));
                    String base64 = data.optString("base64", "");

                    if (base64.isEmpty()) {
                        Toast.makeText(PayslipPeriodActivity.this, R.string.TXT_PAYSLIP_PERIOD_TOAST1, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    byte[] pdfAsBytes = Base64.decode(base64, Base64.DEFAULT);

                    File file = new File(getCacheDir(),  getText(R.string.TXT_PAYSLIP_PERIOD_PAYSLIP)+"_"+selectedMonth+"_"+selectedYear+".pdf");
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        fos.write(pdfAsBytes);
                    }

                    // Open PDF viewer
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(
                            FileProvider.getUriForFile(
                                    PayslipPeriodActivity.this,  // ✅ FIXED
                                    getPackageName() + ".provider",
                                    file
                            ),
                            "application/pdf"
                    );
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(PayslipPeriodActivity.this, R.string.TXT_PAYSLIP_PERIOD_TOAST2, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onServiceFailure(int code, String message) {
                dismissProgressDialog();
                if (code == 400) {
                    Toast.makeText(PayslipPeriodActivity.this, R.string.TXT_PAYSLIP_PERIOD_TOAST3, Toast.LENGTH_SHORT).show();

                    // Close both OTP and Period screens → return to OrchestraFragment
                    Intent intent = new Intent(PayslipPeriodActivity.this, com.daimlertruck.dtag.internal.android.mbt.test.ui.main.main.MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(PayslipPeriodActivity.this, R.string.TXT_PAYSLIP_PERIOD_TOAST4, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNetworkFailure(Throwable t) {
                dismissProgressDialog();
                Toast.makeText(PayslipPeriodActivity.this, R.string.TXT_PAYSLIP_PERIOD_TOAST5, Toast.LENGTH_SHORT).show();
            }
        });


    }
}