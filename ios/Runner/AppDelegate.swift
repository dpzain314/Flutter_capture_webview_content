import UIKit
import Flutter
import WebKit

@UIApplicationMain
@objc class AppDelegate: FlutterAppDelegate {
    
    override func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    ) -> Bool {
        let controller : FlutterViewController = window?.rootViewController as! FlutterViewController
        let channel = FlutterMethodChannel(name: "recapMethodChannel",binaryMessenger: controller.binaryMessenger)
        
        channel.setMethodCallHandler({(call: FlutterMethodCall, result: @escaping FlutterResult) -> Void in
            if(call.method == "recap"){
                if let args = call.arguments as? String{
                    self.openRecapScreen(from: controller,args)
                }
            }
            
        })
        
        GeneratedPluginRegistrant.register(with: self)
        return super.application(application, didFinishLaunchingWithOptions: launchOptions)
    }
    
    func openRecapScreen(from viewController: UIViewController, _ recapUrl: String) {
        let recapViewController = RecapViewController()
        recapViewController.modalPresentationStyle = .fullScreen
        recapViewController.receiveParameter(parameter: recapUrl)
        
        if let navigationController = viewController.navigationController {
            navigationController.pushViewController(recapViewController, animated: true)
        } else {
            viewController.present(recapViewController, animated: true, completion: nil)
        }
        //        viewController.present(recapViewController, animated: true, completion: nil)
    }
    
}
