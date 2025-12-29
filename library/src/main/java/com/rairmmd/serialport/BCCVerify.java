package com.rairmmd.serialport;

/**
 * @author Rair
 * @date 2018/1/5
 * <p>
 * desc: BCC checksum
 */
public class BCCVerify {

    /**
    * Calculates the BCC (Block Check Character)
    *
    * @param data The data message
    */
    private static String bccVal(byte[] data) {
        String ret = "";
        byte[] BCC = new byte[1];
        for (byte datum : data) {
            BCC[0] = (byte) (BCC[0] ^ datum);
        }
        String hex = Integer.toHexString(BCC[0] & 0xFF);
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        ret += hex.toUpperCase();
        return ret;
    }

    /**
    * Calculates the BCC and converts it to a byte array.
    *
    * @param data The data message
    */
    public static byte[] calcBccBytes(byte[] data) {
        String bccVal = bccVal(data);
        return ByteUtil.hexStringToByteArray(bccVal);
    }
}
