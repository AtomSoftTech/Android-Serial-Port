package android_serialport_api;

import android.util.Log;
import android.view.ViewManager;

import com.rairmmd.serialport.OnDataReceiverListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPortManager {

    private static final String TAG = "SerialPortManager";

    private SerialPortFinder mSerialPortFinder;

    private String mDeviceName;
    private int mBaudRate;
    private SerialPort mSerialPort;

    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadCOMThread mReadCOMThread;

    private OnDataReceiverListener onDataReceiverListener;

    /**
    * Machine control
    *
    * @param devName  Serial port device name
    * @param baudRate Baud rate
    *
    * For example, devName = "/dev/ttyS3", baudRate = 9600.
    */
    public SerialPortManager(String devName, int baudRate) {
        mSerialPortFinder = new SerialPortFinder();
        mDeviceName = devName;
        mBaudRate = baudRate;
        mSerialPort = null;
    }

//    private SerialPortManager() {
//        mSerialPortFinder = new SerialPortFinder();
//        mSerialPort = null;
//    }
//
//    private static class SerialPortManagerHolder {
//        private static SerialPortManager instance = new SerialPortManager();
//    }
//
//    public static SerialPortManager getInstance() {
//        return SerialPortManagerHolder.instance;
//    }
//
//    public void setDeviceName(String deviceName) {
//        this.mDeviceName = deviceName;
//    }
//
//    public void setBaudRate(int baudRate) {
//        this.mBaudRate = baudRate;
//    }

    /**
    * Enumerates the device names of all serial ports. 
    *
    * @return An array of serial port device names.
    */
    private String[] getCOMList() {
        return mSerialPortFinder.getAllDevicesPath();
    }

    /**
    * Opens the serial port.
    *
    * @return Whether the operation was successful.
    */
    public boolean openCOM() {
        String[] comList = getCOMList();
        for (String comname : comList) {
            Log.i(TAG, "All serial port device names：" + comname);
        }
        if (mSerialPort == null) {
            try {
                mSerialPort = new SerialPort(new File(mDeviceName), mBaudRate, 0);
                mOutputStream = mSerialPort.getOutputStream();
                mInputStream = mSerialPort.getInputStream();
                // Start the thread for reading serial port data.
                mReadCOMThread = new ReadCOMThread();
                mReadCOMThread.start();
                return true;
            } catch (Exception e) {
                Log.i(TAG, e.getMessage());
                mSerialPort = null;
            }
        }
        return false;
    }

    /**
     * Close the serial port.
     */
    public void closeCOM() {
        if (mSerialPort != null) {
            mReadCOMThread.interrupt();
            mSerialPort.closeIOStream();
            mSerialPort.close();
            mSerialPort = null;
        }
    }

    /**
    * Sends a message.
    *
    * @param data The message to send.
    * @return Whether the operation was successful.
    */
    public boolean sendCMD(byte[] data) {
        try {
            if (mOutputStream != null) {
                mOutputStream.write(data);
                mOutputStream.flush();
            } else {
                return false;
            }
        } catch (IOException e) {
            Log.i(TAG, e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Read serial port data
     */
    private class ReadCOMThread extends Thread {

        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    byte[] buffer = new byte[24];
                    if (mInputStream == null) {
                        break;
                    }
                    int size = mInputStream.read(buffer);
                    if (size > 0) {
                        Thread.sleep(500);
                        onDataReceiverListener.onDataReceiver(buffer, size);
                    }
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                    break;
                } catch (InterruptedException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
    }

    /**
     * Set up a callback listener.
     *
     * @param onDataReceiverListener onDataReceiverListener
     */
    public void setOnDataReceiverListener(OnDataReceiverListener onDataReceiverListener) {
        this.onDataReceiverListener = onDataReceiverListener;
    }

}
