//
//  AppFeedbackViewController.swift
//  MBTBiziz
//
//  Created by Nermy on 2.12.2023.
//  Copyright © 2023 Serkut Yegin. All rights reserved.
//

import UIKit

class AppFeedbackViewController: MBTBaseViewController, UITextViewDelegate
{
  
    @IBOutlet weak var textView: UITextView!
    @IBOutlet weak var bottomConstraint: NSLayoutConstraint!
    
    var keyboardHeight = CGFloat()
    
    override func viewDidLoad() {

        NotificationCenter.default.addObserver(self, selector: #selector(keyBoardWillShow(notification:)), name: .UIKeyboardWillShow, object: nil)
            NotificationCenter.default.addObserver(self, selector: #selector(keyBoardWillHide(notification:)), name: .UIKeyboardWillHide, object: nil)
        
        textView.text = "TXT_APP_FEEDBACK_PLACEHOLDER".localized()
        textView.font = UIFont.mbtRegular(20)
        textView.textColor = UIColor.gray
        textView.returnKeyType = .done
        textView.delegate = self
    }
    override func viewDidAppear(_ animated: Bool) {
        textView.becomeFirstResponder()
    }

    @objc func keyBoardWillShow(notification: NSNotification) {
            //handle appearing of keyboard here
        
        if bottomConstraint.constant == 20, keyboardHeight == 0, let keyboardSize = (notification.userInfo?[UIKeyboardFrameEndUserInfoKey] as? NSValue)?.cgRectValue {
            
            var _kbSize:CGSize!
            let screenSize = UIScreen.main.bounds
            //Calculating actual keyboard displayed size, keyboard frame may be different when hardware keyboard is attached (Bug ID: #469) (Bug ID: #381)
            let intersectRect = keyboardSize.intersection(screenSize)
            
            if intersectRect.isNull {
                _kbSize = CGSize(width: screenSize.size.width, height: 0)
            } else {
                _kbSize = intersectRect.size
            }
            keyboardHeight = _kbSize.height
        }
        bottomConstraint.constant = keyboardHeight + 10
    }


    @objc func keyBoardWillHide(notification: NSNotification) {
              //handle dismiss of keyboard here
        bottomConstraint.constant = 20;
     }
    @IBAction func submitClicked(_ sender: Any) {
        if textView.text.count < 3 || textView.text == "TXT_APP_FEEDBACK_PLACEHOLDER".localized() {
            showBasicAlert(message: "Geri bildiriminiz 3 karakterden daha az olamaz")
        } else if textView.text.count > 500 {
            showBasicAlert(message: "Geri bildiriminiz 500 karakterden daha fazla olamaz")
        } else {
            
            WSProvider.shared.wsRequest(.submitFeedback(text: textView.text), success: { (response: MarshalResponse?) in
                self.showBasicAlert(message: "Geri Bildiriminiz gönderildi!",  cancelHandler:{ () in
                    self.navigationController?.popViewController(animated: true)
                })
            }) { (error) in
                self.showBasicAlert(message: "Geri bildirim verebilmek için uygulamaya giriş yapınız.",  cancelHandler:{ () in
                })
            }
        }
    }

    //MARK:- UITextViewDelegates
    
    func textViewDidBeginEditing(_ textView: UITextView) {
        if textView.text == "TXT_APP_FEEDBACK_PLACEHOLDER".localized() {
            textView.text = ""
            textView.textColor = UIColor.white
        }
    }
    
    func textView(_ textView: UITextView, shouldChangeTextIn range: NSRange, replacementText text: String) -> Bool {
        if(text == "\n") {
            textView.resignFirstResponder()
            return false
        }
        return textView.text.length + (text.length - range.length) <= 500;
    }
    
    func textViewDidEndEditing(_ textView: UITextView) {
        if textView.text == "" {
            textView.text = "TXT_APP_FEEDBACK_PLACEHOLDER".localized()
            textView.textColor = UIColor.gray
        }
        textView.resignFirstResponder()
    }
}



