import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  final methodChannel = const MethodChannel("recapMethodChannel");

  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        body: Center(
          child: TextButton(
            onPressed: () {
              methodChannel.invokeMethod('recap');
            },
            child: const Text('Click'),
          ),
        ),
      ),
    );
  }
}
