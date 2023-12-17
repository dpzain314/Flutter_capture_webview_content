//
//  RecapViewController.swift
//  Runner
//
//  Created by Vuong Hung on 17/12/2023.
//

import UIKit
import WebKit

class RecapViewController: UIViewController, WKNavigationDelegate, WKScriptMessageHandler {
    var webView: WKWebView!
    var mNativeToWebHandler : String = "jsMessageHandler"
    
    var recapUrl: String?
    var backButton: UIButton!
    
    func receiveParameter(parameter: String) {
        recapUrl = parameter
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        buildWebview()
    }
    
    func buildWebview(){
        let configuration = WKWebViewConfiguration()
        configuration.userContentController.add(self, name: mNativeToWebHandler)
        webView = WKWebView(frame: view.frame, configuration: configuration)
        webView.navigationDelegate = self
        view.addSubview(webView)
        
        //        if let url = URL(string: "https://www.instagram.com") {
        //            let request = URLRequest(url: url)
        //            webView.load(request)
        //        }
        webView.loadHTMLString(recapUrl ?? "", baseURL: nil)
    }
    
    func userContentController(_ userContentController: WKUserContentController, didReceive message: WKScriptMessage) {
        
        if message.name == mNativeToWebHandler {
            snapshotWKWebView(webView) { [self] (image) in
                if let image = image {
                    self.saveImageToLibrary(image)
                } else {
                    showToast("Failed to capture the image.")
                }
            }
        }
    }
    
    func snapshotWKWebView(_ webView: WKWebView, completion: @escaping (UIImage?) -> Void) {

        let contentSize = webView.scrollView.contentSize
        webView.frame.size = contentSize
        UIGraphicsBeginImageContextWithOptions(contentSize, true, 0.0)
        
        let savedContentOffset = webView.scrollView.contentOffset
        
        webView.scrollView.contentOffset = .zero
        webView.scrollView.layer.render(in: UIGraphicsGetCurrentContext()!)
        
        let capturedImage = UIGraphicsGetImageFromCurrentImageContext()
        
        UIGraphicsEndImageContext()
        
        webView.scrollView.contentOffset = savedContentOffset
        
        completion(capturedImage)
    }
    
    func saveImageToLibrary(_ image: UIImage) {
        UIImageWriteToSavedPhotosAlbum(image, self, #selector(image(_:didFinishSavingWithError:contextInfo:)), nil)
    }
    
    @objc func image(_ image: UIImage, didFinishSavingWithError error: Error?, contextInfo: UnsafeRawPointer) {
        if let error = error {
            self.showToast("Error saving image: \(error.localizedDescription)")
        } else {
            self.showToast("Image saved successfully.")
        }
    }
    
    func showToast(_ message: String) {
        let toast = UIAlertController(title: nil, message: message, preferredStyle: .actionSheet)
        
        let paragraphStyle = NSMutableParagraphStyle()
        paragraphStyle.alignment = .left // Có thể thay đổi alignment theo yêu cầu của bạn
        paragraphStyle.firstLineHeadIndent = 16.0 // Padding từ bên trái
        
        let attributedMessage = NSAttributedString(string: message, attributes: [
            NSAttributedString.Key.foregroundColor : UIColor.blue, // Màu sắc của nội dung
            NSAttributedString.Key.font : UIFont.systemFont(ofSize: 13),    // Font của nội dung
            NSAttributedString.Key.paragraphStyle : paragraphStyle
        ])
        
        toast.setValue(attributedMessage, forKey: "attributedMessage")
        
        present(toast, animated: true, completion: nil)
        
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + 1.0) {
            toast.dismiss(animated: true, completion: nil)
        }
    }
    
}
