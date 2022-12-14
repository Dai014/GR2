package com.gr2.custom.utils;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.IOException;
import java.io.InputStream;

public class EncodingUtils {

    public static final String UTF_8 = "UTF-8";
    public static final String SHIFT_JIS = "Shift-JIS";

    public static String detectEncoding(InputStream stream) throws IOException {
        byte[] buf = new byte[4096];
        UniversalDetector detector = new UniversalDetector(null);
        int read;
        while ((read = stream.read(buf)) > 0 && !detector.isDone()) {
            detector.handleData(buf, 0, read);
        }
        detector.dataEnd();

        String encoding = detector.getDetectedCharset();
        detector.reset();
        return encoding;
    }
}
