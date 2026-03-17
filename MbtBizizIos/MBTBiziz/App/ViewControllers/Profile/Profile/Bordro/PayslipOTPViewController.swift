import UIKit

final class PayslipOTPViewController: MBTBaseViewController, UITextFieldDelegate {

    @IBOutlet weak var txtOtp: UITextField!
    @IBOutlet weak var btnVerify: UIButton!
    @IBOutlet weak var btnResend: UIButton!
    @IBOutlet weak var lblTimer: UILabel!

    private var cooldownSeconds: Int = 30
    private var ttlSeconds: Int = 60   // OTP 1 dk
    private var timer: Timer?

    override func viewDidLoad() {
        super.viewDidLoad()
        configureUI()
        requestOtp() // ekran açılır açılmaz gönder
    }

    private func configureUI() {
        txtOtp.keyboardType = .numberPad
        btnResend.isEnabled = false
        txtOtp.delegate = self
        txtOtp.addTarget(self, action: #selector(otpTextChanged(_:)), for: .editingChanged)

        btnVerify.isEnabled = false
        btnVerify.alpha = 0.5   // disabled görünümü
        
        lblTimer.text = ""
    }

    // MARK: - OTP input listener
    @objc private func otpTextChanged(_ textField: UITextField) {
        let count = textField.text?.count ?? 0
        if count == 6 {
            btnVerify.isEnabled = true
            btnVerify.alpha = 1.0
        } else {
            btnVerify.isEnabled = false
            btnVerify.alpha = 0.5
        }
    }
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        // Current text
        let currentText = textField.text ?? ""
        // After input
        guard let stringRange = Range(range, in: currentText) else { return false }
        let updatedText = currentText.replacingCharacters(in: stringRange, with: string)

        // Limit to 6 characters
        return updatedText.count <= 6
    }
    
    private func startTimers() {
        // 1 sn aralıkla geri sayım
        timer?.invalidate()
        timer = Timer.scheduledTimer(withTimeInterval: 1, repeats: true) { [weak self] _ in
            guard let s = self else { return }
            if s.cooldownSeconds > 0 {
                s.cooldownSeconds -= 1
                s.btnResend.isEnabled = false
            } else {
                s.btnResend.isEnabled = true
            }

            if s.ttlSeconds > 0 {
                s.ttlSeconds -= 1
            }

            s.lblTimer.text = String(format: "TXT_OTP_TIME".localized()+": %02d:%02d", s.ttlSeconds/60, s.ttlSeconds%60)

            if s.ttlSeconds == 0 {
                s.timer?.invalidate()
                s.showToast("TXT_OTP_EXPIRED_MESSAGE".localized())
                s.btnResend.isEnabled = true
            }
        }
    }

    private func resetTimers() {
        cooldownSeconds = 30
        ttlSeconds = 60
        startTimers()
    }

    // MARK: - API

    private func requestOtp() {
        btnResend.isEnabled = false
        
        WSProvider.shared.wsRequest(.payslipRequestOtp,
            success: {(_ response : MBTBasicResponse?) in
                self.resetTimers()
            },
            failure: { error in
                self.navigationController?.popViewController(animated: true)
                self.showAlert(title: "TXT_COMMON_ERROR".localized(), message: error.errorDescription)

            }
        )
    }

    private func verifyOtp(code: String) {
        
        WSProvider.shared.wsRequest(.payslipVerifyOtp(code: code)) { (_ response : MBTBasicResponse?) in
            // backend success => push to period

                self.goToPeriod()
            
        } failure: { error in
            self.showAlert(title: "TXT_COMMON_ERROR".localized(), message: "TXT_OTP_INVALID_CODE".localized())
        }
    }

    private func goToPeriod() {
        let sb = UIStoryboard(name: "Profile", bundle: nil)
        let vc = sb.instantiateViewController(withIdentifier: "PayslipPeriodViewController") as! PayslipPeriodViewController
        
        if var vcs = navigationController?.viewControllers {
            // Remove current (OTP) VC from stack
            vcs.removeLast()
            vcs.append(vc)
            navigationController?.setViewControllers(vcs, animated: true)
        }
    }
    
    // MARK: - Actions

    @IBAction func btnVerifyTapped(_ sender: UIButton) {
        guard let code = txtOtp.text?.trimmingCharacters(in: .whitespaces), code.count >= 4 else {
            showToast("TXT_OTP_ENTER_CODE".localized())
            return
        }
        verifyOtp(code: code)
    }

    @IBAction func btnResendTapped(_ sender: UIButton) {
        txtOtp.text = ""
        requestOtp()
    }

    deinit { timer?.invalidate() }
}

// Basit placeholder response modelleri
final class EmptyResponse: MarshalResponse {}
final class SimpleStatusResponse: MarshalResponse {}
