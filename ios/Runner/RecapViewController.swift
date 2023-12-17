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
    var htmlData = """
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Callback Example</title>
    <style>
//        body {
//            height: 200vh; /* Add enough content to enable scrolling */
//        }
        .long-content {
            margin: 20px;
            padding: 20px;
            border: 1px solid #ccc;
        }
    </style>
</head>
<body>
    <h1>Scrollable Content</h1>
    
    <button id="callbackButton" onclick="sendCallback()">Send Callback</button>

    <div class="long-content">
        <p>This is a long piece of content to increase the length of the page.</p>
        <!-- Add more content as needed -->
        <p>This is another paragraph.</p>
        <p>More content goes here...</p>
        <p>And so on...</p>
    </div>
    <div class="long-content">
        <p>This is a long piece of content to increase the length of the page.</p>
        <!-- Add more content as needed -->
        <p>This is another paragraph.</p>
        <p>More content goes here...</p>
        <p>And so on...</p>
    </div>
    <div class="long-content">
        <p>This is a long piece of content to increase the length of the page.</p>
        <!-- Add more content as needed -->
        <p>This is another paragraph.</p>
        <p>More content goes here...</p>
        <p>And so on...</p>
    </div>
    <div class="long-content">
        <p>This is a long piece of content to increase the length of the page.</p>
        <!-- Add more content as needed -->
        <p>This is another paragraph.</p>
        <p>More content goes here...</p>
        <p>And so on...</p>
    </div>
    <div class="long-content">
        <p>This is a long piece of content to increase the length of the page.</p>
        <!-- Add more content as needed -->
        <p>This is another paragraph.</p>
        <p>More content goes here...</p>
        <p>And so on...</p>
    </div>

    <script>
        function sendCallback() {
            // Check if the native interface is available
            if (window.webkit && window.webkit.messageHandlers) {
                // iOS
                window.webkit.messageHandlers.jsMessageHandler.postMessage("Callback from WebView");
            } else if (window.Android) {
                // Android
                window.Android.sendCallbackToAndroid("Callback from WebView");
            }
        }
    </script>
</body>
</html>

"""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        print("Received message from JavaScript:")
        let configuration = WKWebViewConfiguration()
        configuration.userContentController.add(self, name: mNativeToWebHandler)
        
        webView = WKWebView(frame: view.frame, configuration: configuration)
        webView.navigationDelegate = self
        view.addSubview(webView)
        
        //        if let url = URL(string: "https://www.instagram.com") {
        //            let request = URLRequest(url: url)
        //            webView.load(request)
        //        }
        webView.loadHTMLString(htmlData, baseURL: nil)
        print("Received message from JavaScript:")
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
        //        let renderer = UIGraphicsImageRenderer(bounds: webView.bounds)
        //
        //        renderer.image { (context) in
        //            webView.layer.render(in: context.cgContext)
        //        }.pngData()
        //
        //        completion(renderer.image { context in
        //            webView.layer.render(in: context.cgContext)
        //        })
        
        
        // Get the content size of the webView
        let contentSize = webView.scrollView.contentSize
        
        // Set the frame size of the webView to the content size
        webView.frame.size = contentSize
        
        // Begin the image context
        UIGraphicsBeginImageContextWithOptions(contentSize, true, 0.0)
        
        // Save the current offset of the scrollView
        let savedContentOffset = webView.scrollView.contentOffset
        
        // Render the entire content of the scrollView
        webView.scrollView.contentOffset = .zero
        webView.scrollView.layer.render(in: UIGraphicsGetCurrentContext()!)
        
        // Get the captured image from the context
        let capturedImage = UIGraphicsGetImageFromCurrentImageContext()
        
        // End the image context
        UIGraphicsEndImageContext()
        
        // Restore the scrollView's content offset to its original value
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
        let toast = UIAlertController(title: nil, message: message, preferredStyle: .alert)
        present(toast, animated: true, completion: nil)
        
        // Ẩn toast sau 2 giây
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + 2.0) {
            toast.dismiss(animated: true, completion: nil)
        }
    }
    
}
