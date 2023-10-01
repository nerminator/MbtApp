//
//  BarcodeScannerViewController.swift
//  UnileverVisit
//
//  Created by Serkut Yegin on 19.07.2019.
//  Copyright © 2019 VolantX. All rights reserved.
//

import UIKit
import AVFoundation

protocol BarcodeRoutable {

    func routeToBarcode(from controller: UIViewController?)
}

extension BarcodeRoutable {

    func routeToBarcode(from controller: UIViewController?) {

        let barcode = BarcodeScannerViewController.fromNib()
        barcode.hidesBottomBarWhenPushed = true
        controller?.navigationController?.pushViewController(barcode, animated: true)
    }
}

class BarcodeScannerViewController: MBTBaseViewController {

    @IBOutlet private weak var viewLayer: UIView!
    @IBOutlet private var messageLabel: BaseUILabelDemi!

    @IBOutlet private weak var viewInfo: UIView!
    @IBOutlet private weak var labelInfoTitle: BaseUILabelRegular!
    @IBOutlet private weak var labelInfoDesc: BaseUILabelRegular!
    @IBOutlet private weak var buttonRetry: UIButton!
    @IBOutlet private weak var buttonReturn: UIButton!
    @IBOutlet private weak var constInfoBottom: NSLayoutConstraint!


    private var captureSession = AVCaptureSession()
    private var videoPreviewLayer: AVCaptureVideoPreviewLayer?
    private var qrCodeFrameView: UIView?

    fileprivate var barcodeFoundBlock: ((_ barcode: String) -> Void)?

    private let supportedCodeTypes = [
        AVMetadataObject.ObjectType.qr
    ]

    override func viewDidLoad() {

        super.viewDidLoad()

        addVideoLayer()
        view.bringSubview(toFront: viewLayer)
        view.bringSubview(toFront: messageLabel)
        view.bringSubview(toFront: viewInfo)

        title = "TXT_QR_CODE_TITLE".localized()
        messageLabel.text = "TXT_QR_CODE_DESC".localized()
        if #available(iOS 11.0, *) {
            viewInfo.layer.maskedCorners = [.layerMaxXMinYCorner, .layerMinXMinYCorner]
        }
        viewInfo.layer.cornerRadius = 16
    }

    override func viewDidLayoutSubviews() {

        super.viewDidLayoutSubviews()
        videoPreviewLayer?.frame = view.layer.bounds
        viewLayer?.addQrCodeMaskLayer()
    }

    private func addVideoLayer() {

        // Get the back-facing camera for capturing videos
        let deviceDiscoverySession = AVCaptureDevice.DiscoverySession(
            deviceTypes: [.builtInWideAngleCamera],
            mediaType: AVMediaType.video,
            position: .back
        )

        guard let captureDevice = deviceDiscoverySession.devices.first else {
            print("Failed to get the camera device")
            return
        }

        do {
            // Get an instance of the AVCaptureDeviceInput class using the previous device object.
            let input = try AVCaptureDeviceInput(device: captureDevice)

            // Set the input device on the capture session.
            captureSession.addInput(input)

            // Initialize a AVCaptureMetadataOutput object and set it as the output device to the capture session.
            let captureMetadataOutput = AVCaptureMetadataOutput()
            captureSession.addOutput(captureMetadataOutput)

            // Set delegate and use the default dispatch queue to execute the call back
            captureMetadataOutput.setMetadataObjectsDelegate(self, queue: DispatchQueue.main)
            captureMetadataOutput.metadataObjectTypes = supportedCodeTypes
            //            captureMetadataOutput.metadataObjectTypes = [AVMetadataObject.ObjectType.qr]

        } catch {
            // If any error occurs, simply print it out and don't continue any more.
            print(error)
            return
        }

        // Initialize the video preview layer and add it as a sublayer to the viewPreview view's layer.
        videoPreviewLayer = AVCaptureVideoPreviewLayer(session: captureSession)
        videoPreviewLayer?.videoGravity = AVLayerVideoGravity.resizeAspectFill
        videoPreviewLayer?.frame = view.layer.bounds
        view.layer.addSublayer(videoPreviewLayer!)

        // Start video capture.
        captureSession.startRunning()

        // Move the message label and top bar to the front
        view.bringSubview(toFront: messageLabel)

        // Initialize QR Code Frame to highlight the QR code
        qrCodeFrameView = UIView()

        if let qrCodeFrameView = qrCodeFrameView {
            qrCodeFrameView.layer.borderColor = UIColor.green.cgColor
            qrCodeFrameView.layer.borderWidth = 2
            view.addSubview(qrCodeFrameView)
            view.sendSubview(toBack: qrCodeFrameView)
        }
    }

    @IBAction private func buttonRetryTapped(_ sender: UIButton) {

        captureSession.startRunning()
        hideBottomSheet()
    }

    @IBAction private func buttonReturnTapped(_ sender: UIButton) {

        navigationController?.popViewController(animated: true)
    }
}

// MARK: - AVCaptureMetadataOutputObjectsDelegate

extension BarcodeScannerViewController: AVCaptureMetadataOutputObjectsDelegate {

    func metadataOutput(
        _ output: AVCaptureMetadataOutput,
        didOutput metadataObjects: [AVMetadataObject],
        from connection: AVCaptureConnection
        ) {

        // Check if the metadataObjects array is not nil and it contains at least one object.
        if metadataObjects.isEmpty {
            qrCodeFrameView?.frame = CGRect.zero
            return
        }

        // Get the metadata object.
        guard let metadataObj = metadataObjects.first as? AVMetadataMachineReadableCodeObject else { return }

        if supportedCodeTypes.contains(metadataObj.type) {
            // If the found metadata is equal to the QR code metadata (or barcode) then update the status label's text and set the bounds
            let barCodeObject = videoPreviewLayer?.transformedMetadataObject(for: metadataObj)
            qrCodeFrameView?.frame = barCodeObject!.bounds

            if let decoded = metadataObj.stringValue {
                processQRCode(decoded)
            }
        }
    }

    func processQRCode(_ code: String) {

        captureSession.stopRunning()

        WSProvider.shared.wsRequest(.sendQRCode(code: code), success: { [weak self] (response: WSResponse<DecodableResponse>?) in
            if response?.isSuccess == true {
                self?.showBottomSheet(isSuccess: true)
            } else {
                self?.showBottomSheet(isSuccess: false)
            }
        }) { [weak self] (error) in
            self?.showBottomSheet(isSuccess: false)
        }
    }
}

// MARK: - Info Bottom Sheet

private extension BarcodeScannerViewController {

    func showBottomSheet(isSuccess: Bool) {

        if isSuccess {

            labelInfoTitle.text = "TXT_QR_CODE_POPUP_TITLE_SUCCESS".localized()
            labelInfoDesc.text = "TXT_QR_CODE_POPUP_DESC_SUCCESS".localized()
            buttonRetry.isHidden = true
        } else {
            labelInfoTitle.text = "TXT_QR_CODE_POPUP_TITLE_FAIL".localized()
            labelInfoDesc.text = "TXT_QR_CODE_POPUP_DESC_FAIL".localized()
            buttonRetry.isHidden = false
        }

        constInfoBottom.constant = 0
        view.layoutIfNeededAnimated()
    }

    func hideBottomSheet() {

        constInfoBottom.constant = -viewInfo.h
        view.layoutIfNeededAnimated()
    }
}
