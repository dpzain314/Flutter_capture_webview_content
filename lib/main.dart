import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  final methodChannel = const MethodChannel("recapMethodChannel");

  MyApp({super.key});

  String htmlData = """
  <!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Callback Example</title>
    <style>
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
  """;

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        body: Center(
          child: TextButton(
            onPressed: () {
              methodChannel.invokeMethod('recap',htmlData);
            },
            child: const Text('Click'),
          ),
        ),
      ),
    );
  }
}
