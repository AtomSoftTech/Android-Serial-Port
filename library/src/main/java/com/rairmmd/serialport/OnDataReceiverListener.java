package com.rairmmd.serialport;

/**
 * @author Rair
 * @date 2017/10/25
 * <p>
 * desc: Data reception callback
 */

public interface OnDataReceiverListener {

    /**
     * Receiving data
     *
     * @param buffer The received byte array
     * @param size The length
     */
    void onDataReceiver(byte[] buffer, int size);
}
