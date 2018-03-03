import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Parser {
    private static Map<String, String> result = new TreeMap<>();

//    public static void main(String[] args) throws IOException {
//        String input = "C:\\Users\\Freemahn\\Downloads\\tiny_url.txt";
//        String output = "C:\\Users\\Freemahn\\Downloads\\tiny_url2.txt";
//
//        BufferedWriter writer = new BufferedWriter(new FileWriter(output));
//
//        Files.lines(Paths.get(input)).forEach(Main::parse);
//        result.forEach((k, v) -> {
//            try {
//                writer.write(k + "," + v + "\n");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//        writer.close();
//    }

    public static List<String> tinyUrl2Id(List<String> list) {
        return list.stream().map(Parser::tinyUrl2Id).collect(Collectors.toList());
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
}
