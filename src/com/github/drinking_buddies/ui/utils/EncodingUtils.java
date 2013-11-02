package com.github.drinking_buddies.ui.utils;

import java.io.IOException;

import sun.misc.BASE64Decoder;

public class EncodingUtils {
    public static byte[] base64ToByteArray(String encodedBytes) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] decodedBytes = decoder.decodeBuffer(encodedBytes);
        return decodedBytes;
    }
}
