## Android-Serial-Port
Android serial communication, based on Google's official build, for convenient future use.

## Instructions for the library
* libs
SO files corresponding to various CPU architectures

* src/main/android_serialport_api
Some control-related operations and operations for opening and closing serial ports.

* ByteUtil
Utility class, byte array to string conversion.

* CRC16Verify
CRC16 checksum algorithm

* BCCVerify
BCC XOR checksum

* OnDataReceiverListener
Callback listener after receiving a response.

## Use
1. Import the library as a dependency.
2. If you encounter an error indicating a missing .so file during use, please copy the .so file to the libs folder and configure accordingly.
  
```
ndk {
    // Select the corresponding .so library for the CPU type you want to add.
    abiFilters 'armeabi', 'armeabi-v7a', 'armeabi-v8a', 'x86', 'mips'
}
```

```
sourceSets {
    main {
        jniLibs.srcDirs = ['libs']
}
```
3. Add this to the module's build.gradle file.
```
dependencies {
    implementation(':library')
}
```
### SerialPort
This is a serial port operation class, corresponding to JNI methods.
It is used for opening and closing serial ports, obtaining input and output streams, and sending and receiving messages through these streams.

### SerialPortManager
Control class: for opening and closing the serial port, and sending and receiving messages.

It's usually implemented as a singleton, allowing you to open or close the serial port within the app without needing to open and close it frequently.
```
public SerialPortManager(String devName, int baudRate) Constructor method (serial port device name, baud rate)

boolean openCOM()  Open the serial port.

void setOnDataReceiverListener(OnDataReceiverListener onDataReceiverListener) Set up a listener to receive the response message and data length.

void closeCOM() Close the serial port.

boolean sendCMD(byte[] data) Send message
```
### SerialFinder Optional (can be omitted)
Serial port operation class
Enumerate all device serial ports.

## License
Apache2.0
