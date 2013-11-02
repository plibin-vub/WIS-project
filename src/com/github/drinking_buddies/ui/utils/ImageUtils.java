package com.github.drinking_buddies.ui.utils;

import eu.webtoolkit.jwt.WMemoryResource;

public class ImageUtils {
    public static WMemoryResource createResource(byte[] data, String mimetype) {
        //create a memory resource with based on data and mimetype 
        WMemoryResource mr = new WMemoryResource();
        mr.setData(data);
        mr.setMimeType(mimetype);
        return mr;
    }
}
