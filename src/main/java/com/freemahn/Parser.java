package com.freemahn;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class Parser {


//    public static void main(String[] args) {
//        System.out.println(id2TinyUrl("13045735"));
//        System.out.println(tinyUrl2Id("/x/5w-H"));
//    }

    public static List<String> tinyUrl2Id(List<String> list) {
        return list.stream().map(Parser::tinyUrl2Id).collect(Collectors.toList());
    }

    public static List<String> id2TinyUrl(List<String> list) {
        return list.stream().map(Parser::id2TinyUrl).collect(Collectors.toList());
    }

    private static String id2TinyUrl(String id) {
        String result = encode(Integer.parseInt(id));
        return id + "," + result;

    }

    private static String encode(Integer id) {
        byte[] bytes = ByteBuffer.allocate(4).putInt(id).array();

        for(int i = 0; i < bytes.length / 2; i++)
        {
            byte temp = bytes[i];
            bytes[i] = bytes[bytes.length - i - 1];
            bytes[bytes.length - i - 1] = temp;
        }

        String result = Base64.getEncoder().encodeToString(bytes);
        result = result.replace("+", "_").replace("/", "-");
        result = "/x/" + result.split("AA==")[0].split("==")[0];
        return result;
    }

    private static String tinyUrl2Id(String url) {
        String t = url.substring(3).replace("_", "+").replace("-", "/");
        String result = "";
        try {
            result = decode(t);
        } catch (Exception e) {
            System.err.println(url);
        }
        return url + "," + result;
    }


    private static String decode(String url) {
        url = padRight(url, 8).replace(' ', 'A');
        byte[] bytes = Base64.getDecoder().decode(url);
        ByteBuffer byteBuf = ByteBuffer.wrap(bytes);

        // then turn it into an IntBuffer, using big-endian ("Network") byte order:
        byteBuf.order(ByteOrder.LITTLE_ENDIAN);
        IntBuffer intBuf = byteBuf.asIntBuffer();
        // finally, dump the contents of the IntBuffer into an array
        int[] integers = new int[intBuf.remaining()];
        intBuf.get(integers);
        return String.valueOf(integers[0]);

    }

    private static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }



    public static byte[] stringToBytes(final String input) {
        // unlike Stirng.toByteArray(), we ignore any high-byte values of the characters.
        byte[] ret = new byte[input.length()];
        for (int i = input.length() - 1; i >=0; i--) {
            ret[i] = (byte)input.charAt(i);
        }
        return ret;
    }
}
