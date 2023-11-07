package com.botcamp.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GzipUtils {
    public static ByteArrayOutputStream compress(InputStream inputStream) {
        if (inputStream == null) return null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            GzipCompressorOutputStream gzipOut = new GzipCompressorOutputStream(baos);
            gzipOut.write(inputStream.readAllBytes());
            gzipOut.flush();
            gzipOut.close();
            return baos;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String decompress(InputStream inputStream) {
        if (inputStream == null) return null;
        try {
            StringBuilder sb = new StringBuilder();
            GzipCompressorInputStream gzipCompressorInputStream = new GzipCompressorInputStream(inputStream);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gzipCompressorInputStream, StandardCharsets.UTF_8));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
