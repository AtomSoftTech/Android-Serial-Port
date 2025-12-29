package android_serialport_api;

import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class SerialPort {

    private static final String TAG = "SerialPort";

    static {
        System.loadLibrary("serial_port");
    }

    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;

    public SerialPort(File device, int baudRate, int flags) throws SecurityException, IOException {
        //Check access permissions.
        if (!device.canRead() || !device.canWrite()) {
            try {
                // No read/write permissions, attempting to elevate privileges for the file.
                Process su = Runtime.getRuntime().exec("/system/bin/su");
                String cmd = "chmod 777 " + device.getAbsolutePath() + "\n" + "exit\n";
                su.getOutputStream().write(cmd.getBytes());
                if ((su.waitFor() != 0) || !device.canRead() || !device.canWrite()) {
                    throw new SecurityException();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
                throw new SecurityException();
            }
        }
        //Do not delete or rename the field mFd: the native method close() uses this field.
        FileDescriptor mFd = open(device.getAbsolutePath(), baudRate, flags);
        if (mFd == null) {
            Log.i(TAG, "open method return null");
            throw new IOException();
        }
        mFileInputStream = new FileInputStream(mFd);
        mFileOutputStream = new FileOutputStream(mFd);
    }

    /**
    * Opens the serial port.
    *
    * @param path     Device path
    * @param baudRate Baud rate
    * @param flags    Flags
    * @return FileDescriptor
    */
    private native static FileDescriptor open(String path, int baudRate, int flags);

    /**
    * Closes the serial port.
    */
    public native void close();

    /**
     * Get the input and output streams.
     */
    public InputStream getInputStream() {
        return mFileInputStream;
    }

    public OutputStream getOutputStream() {
        return mFileOutputStream;
    }

    /**
     * Close the I/O stream.
     */
    public void closeIOStream() {
        try {
            mFileInputStream.close();
            mFileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        mFileInputStream = null;
        mFileOutputStream = null;
    }

}
