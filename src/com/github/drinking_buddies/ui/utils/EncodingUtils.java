package com.github.drinking_buddies.ui.utils;

import java.io.IOException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class EncodingUtils {
    public static byte[] base64ToByteArray(String encodedBytes) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] decodedBytes = decoder.decodeBuffer(encodedBytes);
        return decodedBytes;
    }
    
    public static String byteArrayToBase64(byte[] data) {
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }
}
