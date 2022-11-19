package com.rabiloo.custom.utils;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class StringUtils {
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static String convertKanJiToHiragana(String str) {
        Tokenizer tokenizer = new Tokenizer();
        List<Token> tokens = tokenizer.tokenize(str);
        StringBuilder sb = new StringBuilder();
        for (Token token : tokens) {
            sb.append(token.getReading());
        }
        return sb.toString();
    }

    public static <T> String exportForQr(T fieldValue) {
        if (fieldValue == null) {
            return Constants.EMPTY_STRING;
        }
        if (fieldValue instanceof String) {
            return (String) fieldValue;
        }
        return fieldValue.toString();
    }

    public static String fromArrayToString(String[] arrayStr) {
        List<String> dataFormatted = new ArrayList<>();
        for (String str : arrayStr) {
            dataFormatted.add(encodeSJIS(str));
        }
        return String.join(",", dataFormatted);
    }

    private static String encodeSJIS(String str) {
        try {
            str = str.trim()
                    .replaceAll(",", "，")
                    .replaceAll("\\?", "？");
            return utf8ToSjis(str);
        } catch (Exception e) {
            return Constants.EMPTY_STRING;
        }
    }

    public static String sjisToUtf8(String value) throws UnsupportedEncodingException {
        byte[] srcStream = value.getBytes("SJIS");
        byte[] destStream = (new String(srcStream, "SJIS")).getBytes(StandardCharsets.UTF_8);
        value = new String(destStream, StandardCharsets.UTF_8);
        value = convert(value, "SJIS", "UTF-8");
        return value;
    }

    private static String utf8ToSjis(String value) throws UnsupportedEncodingException {
        byte[] srcStream = value.getBytes(StandardCharsets.UTF_8);
        value = convert(new String(srcStream, StandardCharsets.UTF_8), "UTF-8", "SJIS");
        byte[] destStream = value.getBytes("SJIS");
        value = new String(destStream, "SJIS");
        return value.replaceAll("\\?", "■");
    }

    private static String convert(String value, String src, String dest) throws UnsupportedEncodingException {
        Map<String, String> conversion = createConversionMap(src, dest);
        char oldChar;
        char newChar;
        String key;
        for (Iterator<String> itr = conversion.keySet().iterator(); itr.hasNext(); ) {
            key = itr.next();
            oldChar = toChar(key);
            newChar = toChar(conversion.get(key));
            value = value.replace(oldChar, newChar);
        }
        return value;
    }

    private static Map<String, String> createConversionMap(String src, String dest) throws UnsupportedEncodingException {
        Map<String, String> conversion = new HashMap<>();
        if ((src.equals("UTF-8")) && (dest.equals("SJIS"))) {
            // －（全角マイナス）
            conversion.put("U+FF0D", "U+2212");
            // ～（全角チルダ）
            conversion.put("U+FF5E", "U+301C");
            // ￠（セント）
            conversion.put("U+FFE0", "U+00A2");
            // ￡（ポンド）
            conversion.put("U+FFE1", "U+00A3");
            // ￢（ノット）
            conversion.put("U+FFE2", "U+00AC");
            // ―（全角マイナスより少し幅のある文字）
            conversion.put("U+2015", "U+2014");
            // ∥（半角パイプが2つ並んだような文字）
            conversion.put("U+2225", "U+2016");

        } else if ((src.equals("SJIS")) && (dest.equals("UTF-8"))) {
            // －（全角マイナス）
            conversion.put("U+2212", "U+FF0D");
            // ～（全角チルダ）
            conversion.put("U+301C", "U+FF5E");
            // ￠（セント）
            conversion.put("U+00A2", "U+FFE0");
            // ￡（ポンド）
            conversion.put("U+00A3", "U+FFE1");
            // ￢（ノット）
            conversion.put("U+00AC", "U+FFE2");
            // ―（全角マイナスより少し幅のある文字）
            conversion.put("U+2014", "U+2015");
            // ∥（半角パイプが2つ並んだような文字）
            conversion.put("U+2016", "U+2225");

        } else {
            throw new UnsupportedEncodingException("This character code is not supported・src=" + src + ",dest=" + dest);
        }
        return conversion;
    }

    private static char toChar(String value) {
        return (char) Integer.parseInt(value.trim().substring("U+".length()), 16);
    }

    public static String[] autoFillArrayIfNotFitSize(int numberColumnWantToSelect, String[] arrayValue) {
        int sizeOfOriginalArray = arrayValue.length;
        if (sizeOfOriginalArray == numberColumnWantToSelect) {
            return arrayValue;
        }
        String[] result = new String[numberColumnWantToSelect];
        for (int i = 0; i < numberColumnWantToSelect; i++) {
            if (i >= sizeOfOriginalArray) {
                result[i] = "";
            } else {
                result[i] = arrayValue[i].trim();
            }
        }
        return result;
    }


}


