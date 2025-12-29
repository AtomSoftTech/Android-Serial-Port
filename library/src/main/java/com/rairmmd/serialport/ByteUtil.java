package com.rairmmd.serialport;

public class ByteUtil {

    private static char[] hexArray = "0123456789ABCDEF".toCharArray();

    /**
    * Converts a string to a byte array.
    *
    * @param s The string
    * @return The byte array
    */
    public static byte[] hexStringToByteArray(String s) {
        if (s.length() < 2) {
            s = "0" + s;
        }
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    /**
    * Converts a byte array to a string.
    *
    * @param hexBytes The byte array.
    * @return The resulting string.
    */
    public static String hexBytesToString(byte[] hexBytes) {
        char[] hexChars = new char[hexBytes.length * 2];
        for (int j = 0; j < hexBytes.length; j++) {
            int v = hexBytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
    * Converts a hexadecimal string to an integer.
    *
    * @param hexString The hexadecimal string.
    * @return The integer value.
    */
    public static int hexStringToInt(String hexString) {
        return Integer.parseInt(hexString, 16);
    }

    /**
    * Converts a hexadecimal string to a byte array.
    *
    * @param hexString the hexadecimal string
    * @return byte[]
    */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
    * Converts a char to a byte.
    *
    * @param c the char to convert
    * @return the resulting byte
    */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

}
