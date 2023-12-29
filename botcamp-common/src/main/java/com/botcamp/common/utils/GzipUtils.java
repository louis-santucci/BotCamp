package com.botcamp.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GzipUtils {
    public static byte[] compress(String str) throws IOException {
        if (str == null || str.isEmpty()) {
            return new byte[0];
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(baos);
        gzip.write(str.getBytes(StandardCharsets.UTF_8));
        gzip.flush();
        gzip.close();
        return baos.toByteArray();
    }

    public static String decompress(final byte[] str) throws IOException {
        if (str == null || str.length == 0) {
            return "";
        }
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(str));
        BufferedReader bf = new BufferedReader(new InputStreamReader(gis, StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bf.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}
