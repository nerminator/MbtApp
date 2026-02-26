//
//  PayslipPeriodViewController.swift
//  MBTBiziz
//
//  Created by Nermy on 12.08.2025.
//  Copyright © 2025 Onion Tech. All rights reserved.
//

import UIKit
import PDFKit

// MARK: - Small theme helpers
private extension UIColor {
    static var mbtCardBackground: UIColor {
        if #available(iOS 13.0, *) { return UIColor.secondarySystemBackground.withAlphaComponent(0.92) }
        return UIColor(white: 1, alpha: 0.08)
    }
    static var mbtSelectionBand: UIColor {
        if #available(iOS 13.0, *) { return UIColor.systemBackground.withAlphaComponent(0.15) }
        return UIColor(white: 1, alpha: 0.12)
    }
    static var mbtLabel: UIColor {
        if #available(iOS 13.0, *) { return .label }
        return .white
    }
    static var mbtSecondaryLabel: UIColor {
        if #available(iOS 13.0, *) { return .secondaryLabel.withAlphaComponent(0.9) } // biraz daha belirgin
        return UIColor(white: 1, alpha: 0.8)
    }
}

private extension UIBlurEffect {
    static var mbtThinDark: UIBlurEffect {
        if #available(iOS 13.0, *) { return .init(style: .systemThinMaterialDark) }
        return .init(style: .dark)
    }
}

// MARK: - VC
final class PayslipPeriodViewController: MBTBaseViewController, UIDocumentInteractionControllerDelegate {

    // Keep a strong reference for preview
    private var docController: UIDocumentInteractionController?

    // Nav bar state backup (preview öncesi/kapanınca geri almak için)
    private var savedAppearance: UINavigationBarAppearance?
    private var savedTintColor: UIColor?
    private var savedBarStyle: UIBarStyle?

    // MARK: UI
    @IBOutlet weak var picker: UIPickerView!

    private let months = Array(1...12)
    private let years: [Int] = {
        let cur = Calendar.current.component(.year, from: Date())
        return Array((cur-5)...cur).reversed()
    }()

    // MARK: Life Cycle
    override func viewDidLoad() {
        super.viewDidLoad()
        title = "TXT_PROFILE_PROFILE_3RD_BUTTON".localized()
        configurePickerCard()
        configureDefaultSelection()
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        applyNavTheme()
    }

    // MARK: - Navbar Theming (koyu tema, beyaz başlık/ikon)
    private func applyNavTheme() {
        guard let navBar = navigationController?.navigationBar else { return }

        // “Back” yazısını gizle
        if #available(iOS 14.0, *) {
            navigationItem.backButtonDisplayMode = .minimal
        }
        navigationItem.backButtonTitle = ""

        if #available(iOS 13.0, *) {
            let ap = UINavigationBarAppearance()
            ap.configureWithTransparentBackground()
            ap.backgroundColor = .clear
            ap.titleTextAttributes = [.foregroundColor: UIColor.white]
            ap.largeTitleTextAttributes = [.foregroundColor: UIColor.white]

            navBar.standardAppearance = ap
            navBar.scrollEdgeAppearance = ap
            navBar.compactAppearance = ap
            navBar.tintColor = .white  // sol ok & sağ bilgi ikon rengi
        } else {
            navBar.barStyle = .black
            navBar.isTranslucent = true
            navBar.tintColor = .white
            navBar.titleTextAttributes = [.foregroundColor: UIColor.white]
        }
    }

    // MARK: UI Composition (picker’ı “kart” içinde göstermek)
    private func configurePickerCard() {
        picker.dataSource = self
        picker.delegate   = self

        // Başlık
        let header = BaseUILabel()
        header.text = "TXT_PAYSLIP_PERIOD".localized()
        header.textAlignment = .center
        header.textColor = UIColor.white // MBT mavisi gibi
        header.font = .systemFont(ofSize: 17, weight: .semibold)
        header.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(header)

        // Kart
        let card = UIView()
        card.translatesAutoresizingMaskIntoConstraints = false
        card.backgroundColor = .mbtCardBackground
        card.layer.cornerRadius = 20
        card.layer.masksToBounds = true
        view.insertSubview(card, belowSubview: picker)

        // İçine hafif blur
        let blur = UIVisualEffectView(effect: UIBlurEffect.mbtThinDark)
        blur.translatesAutoresizingMaskIntoConstraints = false
        card.addSubview(blur)

        // Seçim bandı
        let band = UIView()
        band.translatesAutoresizingMaskIntoConstraints = false
        band.backgroundColor = .mbtSelectionBand
        band.isUserInteractionEnabled = false
        card.addSubview(band)

        // Layout
        NSLayoutConstraint.activate([
            header.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 16),
            header.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            header.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -24),

            card.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 20),
            card.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -20),
            card.topAnchor.constraint(equalTo: header.bottomAnchor, constant: 14),
            card.heightAnchor.constraint(equalToConstant: 230),

            blur.leadingAnchor.constraint(equalTo: card.leadingAnchor),
            blur.trailingAnchor.constraint(equalTo: card.trailingAnchor),
            blur.topAnchor.constraint(equalTo: card.topAnchor),
            blur.bottomAnchor.constraint(equalTo: card.bottomAnchor),
        ])

        // Picker'ı karta yerleştir
        picker.backgroundColor = .clear
        picker.translatesAutoresizingMaskIntoConstraints = false
        card.addSubview(picker)
        NSLayoutConstraint.activate([
            picker.leadingAnchor.constraint(equalTo: card.leadingAnchor, constant: 12),
            picker.trailingAnchor.constraint(equalTo: card.trailingAnchor, constant: -12),
            picker.topAnchor.constraint(equalTo: card.topAnchor, constant: 8),
            picker.bottomAnchor.constraint(equalTo: card.bottomAnchor, constant: -8)
        ])

        // Bandı orta sıraya hizala
        NSLayoutConstraint.activate([
            band.centerYAnchor.constraint(equalTo: card.centerYAnchor),
            band.leadingAnchor.constraint(equalTo: card.leadingAnchor),
            band.trailingAnchor.constraint(equalTo: card.trailingAnchor),
            band.heightAnchor.constraint(equalToConstant: 38)
        ])
    }

    private func configureDefaultSelection() {
        let now = Date()
        let m = Calendar.current.component(.month, from: now)
        let y = Calendar.current.component(.year, from: now)
        if let yi = years.firstIndex(of: y) { picker.selectRow(yi, inComponent: 1, animated: false) }
        picker.selectRow(m-1, inComponent: 0, animated: false)
    }

    // MARK: Actions
    @IBAction func btnShowTapped(_ sender: UIButton) {
        let selMonth = months[picker.selectedRow(inComponent: 0)]
        let selYear  = years[picker.selectedRow(inComponent: 1)]
        fetchPayslip(year: selYear, month: selMonth)
    }

    // MARK: API
    private func fetchPayslip(year: Int, month: Int) {
        WSProvider.shared.wsRequest(.payslipFetch(year: year, month: month)) { (_ response: MBTPayslipResponse?) in
            if let b64 = response?.base64, let data = Data(base64Encoded: b64) {
                self.presentPDF(data: data, title: String(format: "%02d/%d Bordro", month, year))
            } else {
                self.showAlert(title: "Bilgi", message: "Bordro bulunamadı.")
            }
        } failure: { error in
            
            self.navigationController?.popViewController(animated: true)
            self.navigationController?.popViewController(animated: true)
            
            self.showAlert(message: "Oturum Süresi Doldu!")
            
        }
    }

    // MARK: PDF Preview (sistem görüntüleyicisi + paylaşım)
    private func presentPDF(data: Data, title: String) {
        let url = URL(fileURLWithPath: NSTemporaryDirectory())
            .appendingPathComponent("payslip_\(UUID().uuidString).pdf")
        do {
            try data.write(to: url)
            let doc = UIDocumentInteractionController(url: url)
            doc.uti = "com.adobe.pdf"
            doc.name = title
            doc.delegate = self
            self.docController = doc

            if !doc.presentPreview(animated: true) {
                doc.presentOptionsMenu(from: self.view.bounds, in: self.view, animated: true)
            }
        } catch {
            self.showAlert(title: "Hata", message: "PDF açılamadı: \(error.localizedDescription)")
        }
    }

    // MARK: UIDocumentInteractionControllerDelegate (nav bar okunurluğu)
    func documentInteractionControllerWillBeginPreview(_ controller: UIDocumentInteractionController) {
        guard let navBar = navigationController?.navigationBar else { return }

        if #available(iOS 13.0, *) {
            savedAppearance = navBar.standardAppearance
            savedTintColor  = navBar.tintColor

            let ap = UINavigationBarAppearance()
            ap.configureWithOpaqueBackground()
            ap.backgroundColor = .systemBackground
            ap.titleTextAttributes = [.foregroundColor: UIColor.label]
            ap.largeTitleTextAttributes = [.foregroundColor: UIColor.label]

            navBar.standardAppearance = ap
            navBar.scrollEdgeAppearance = ap
            navBar.compactAppearance = ap
            navBar.tintColor = .systemBlue
        } else {
            savedBarStyle = navBar.barStyle
            savedTintColor = navBar.tintColor
            navBar.barStyle = .default
            navBar.tintColor = .systemBlue
        }
    }

    func documentInteractionControllerDidEndPreview(_ controller: UIDocumentInteractionController) {
        guard let navBar = navigationController?.navigationBar else { return }

        if #available(iOS 13.0, *) {
            if let ap = savedAppearance {
                navBar.standardAppearance = ap
                navBar.scrollEdgeAppearance = ap
                navBar.compactAppearance = ap
            }
            navBar.tintColor = savedTintColor ?? .white
        } else {
            navBar.barStyle = savedBarStyle ?? .black
            navBar.tintColor = savedTintColor ?? .white
        }
    }

    func documentInteractionControllerViewControllerForPreview(_ controller: UIDocumentInteractionController) -> UIViewController {
        return navigationController ?? self
    }

    // MARK: Navigation
    private func popToOTP() {
        if let otpVC = navigationController?.viewControllers.first(where: { $0 is PayslipOTPViewController }) {
            navigationController?.popToViewController(otpVC, animated: true)
        } else {
            navigationController?.popViewController(animated: true)
        }
    }
}

// MARK: - UIPickerView
extension PayslipPeriodViewController: UIPickerViewDataSource, UIPickerViewDelegate {

    func numberOfComponents(in pickerView: UIPickerView) -> Int { 2 }
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        component == 0 ? months.count : years.count
    }

    func pickerView(_ pickerView: UIPickerView,
                    viewForRow row: Int,
                    forComponent component: Int,
                    reusing view: UIView?) -> UIView {
        let label = (view as? UILabel) ?? {
            let l = UILabel()
            l.textAlignment = .center
            l.adjustsFontSizeToFitWidth = true
            return l
        }()

        let isSelected = row == pickerView.selectedRow(inComponent: component)
        label.font = isSelected
            ? UIFont.systemFont(ofSize: 22, weight: .semibold)
            : UIFont.systemFont(ofSize: 20, weight: .regular)
        label.textColor = isSelected ? .mbtLabel : .mbtSecondaryLabel
        label.alpha = isSelected ? 1.0 : 0.9

        label.text = component == 0 ? String(format: "%02d", months[row]) : String(years[row])
        return label
    }

    func pickerView(_ pickerView: UIPickerView, rowHeightForComponent component: Int) -> CGFloat { 38 }

    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        pickerView.reloadComponent(0)
        pickerView.reloadComponent(1)
        UISelectionFeedbackGenerator().selectionChanged()
    }
}

extension UIViewController {
    func showAlert(title: String,
                   message: String,
                   okTitle: String = "Tamam",
                   onOK: (() -> Void)? = nil) {
        let ac = UIAlertController(title: title, message: message, preferredStyle: .alert)
        ac.addAction(UIAlertAction(title: okTitle, style: .default) { _ in onOK?() })
        present(ac, animated: true)
    }
}
