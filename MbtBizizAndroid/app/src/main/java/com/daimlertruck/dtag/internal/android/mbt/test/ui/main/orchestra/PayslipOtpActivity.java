package com.daimlertruck.dtag.internal.android.mbt.test.ui.main.orchestra;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseActivity;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.databinding.ActivityPayslipOtpBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponse;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.AbstractApiUtils;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.NetworkCallback;

import java.util.HashMap;

import javax.inject.Inject;

public class PayslipOtpActivity extends BaseActivity<ActivityPayslipOtpBinding> {

    private static final long OTP_TIMEOUT_MS =  60 * 1000; // 60 seconds
    private CountDownTimer countDownTimer;

    private EditText etOtp;
    private Button btnVerify;
    private Button btnResend;
    private TextView tvTimer;

    @Inject
    AbstractApiUtils abstractApiUtils;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_payslip_otp;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseApplication) getApplication()).getComponent().inject(this);

        // Inflate layout views
        etOtp = findViewById(R.id.etOtpCode);
        btnVerify = findViewById(R.id.btnVerifyOtp);
        btnResend = findViewById(R.id.btnResendOtp);
        tvTimer = findViewById(R.id.tvTimer);

        etOtp.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Enable button only if 6 digits are entered
                btnVerify.setEnabled(s.length() == 6);
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        startTimer();
        requestOtp();

        btnVerify.setOnClickListener(v -> verifyOtp());
        btnResend.setOnClickListener(v -> {
            etOtp.setText("");
            requestOtp();
            startTimer();
        });
    }

    private void startTimer() {
        if (countDownTimer != null) countDownTimer.cancel();

        btnResend.setEnabled(false); // başta kapalı
        countDownTimer = new CountDownTimer(OTP_TIMEOUT_MS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = (millisUntilFinished / 1000) / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                tvTimer.setText(String.format("%s: %02d:%02d", getString(R.string.TXT_OTP_TIME), minutes, seconds));
            }

            @Override
            public void onFinish() {
                btnResend.setEnabled(true);
                tvTimer.setText(R.string.TXT_OTP_EXPIRED);
            }
        };
        countDownTimer.start();
        btnResend.setEnabled(false);
    }

    private void requestOtp() {
        if (abstractApiUtils == null) {
            Toast.makeText(this, "API not initialized", Toast.LENGTH_SHORT).show();
            return;
        }

        abstractApiUtils.requestPayslipOtp(new NetworkCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                if (response.getStatuscode()!=200) {
                    Toast.makeText(PayslipOtpActivity.this, R.string.TXT_OTP_TOAST2, Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onServiceFailure(int code, String message) {
                Toast.makeText(PayslipOtpActivity.this, R.string.TXT_OTP_TOAST2, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNetworkFailure(Throwable t) {
                Toast.makeText(PayslipOtpActivity.this, R.string.TXT_OTP_TOAST3, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {}
        });
    }

    private void verifyOtp() {
        String code = etOtp.getText().toString().trim();
        if (code.isEmpty()) {
            Toast.makeText(this, "Lütfen kodu giriniz", Toast.LENGTH_SHORT).show();
            return;
        }

        if (abstractApiUtils == null) {
            Toast.makeText(this, "API not initialized", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, Object> body = new HashMap<>();
        body.put("otp", code);

        abstractApiUtils.verifyPayslipOtp(body, new NetworkCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                //Toast.makeText(PayslipOtpActivity.this, R.string.TXT_OTP_TOAST4, Toast.LENGTH_SHORT).show();
                if (response.getStatuscode() == 200)
                {
                    // Go to period screen
                    Intent intent = new Intent(PayslipOtpActivity.this, PayslipPeriodActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(PayslipOtpActivity.this, R.string.TXT_OTP_TOAST5, Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onServiceFailure(int code, String message) {
                Toast.makeText(PayslipOtpActivity.this, R.string.TXT_OTP_TOAST3, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNetworkFailure(Throwable t) {
                Toast.makeText(PayslipOtpActivity.this, R.string.TXT_OTP_TOAST3, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {}
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, PayslipOtpActivity.class);
        context.startActivity(starter);
    }
}