package com.github.gzuliyujiang.UnicodeEmoji;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

/**
 * [description]
 * Created by liyujiang on 2019/12/9
 *
 * @author 大定府羡民
 */
@SuppressWarnings("unused")
class Main {

    public static void main(String[] args) throws Exception {
        String str = "English!@#中文，￥？\uD83D\uDE02😂\uE412";
        //String str = "English!@#中文，￥？😂";
        System.out.println("str=" + str);
        System.out.println("emojiUnicodeEncode=" + UnicodeUtils.emojiEncode(false, str));
        System.out.println("allUnicodeEncode=" + UnicodeUtils.toUnicodeFormal(str));
        //testEncoding();
        //downloadJson();
        mapSBToUnified();
        decodeSBToEmoji();
    }

    /**
     * <pre>
     * str = "\uD83D\uDE02"
     * str=😂
     * len=2
     * Default=4  HEX=F09F9882
     * UTF-8=4  HEX=F09F9882
     * UTF-16=6  HEX=FEFFD83DDE02
     * UTF-16BE=4  HEX=D83DDE02
     * UTF-16LE=4  HEX=3DD802DE
     * UTF-32BE=4  HEX=0001F602
     * UTF-32LE=4  HEX=02F60100
     * Unicode=6  HEX=FEFFD83DDE02
     * </pre>
     */
    private static void testEncoding() throws Exception {
        String str = "\uD83D\uDE02";
        System.out.println(str);
        System.out.println("str=" + str);
        System.out.println("len=" + str.length());
        System.out.println("Default=" + str.getBytes().length + "" +
                "  HEX=" + UnicodeUtils.bytesToHexString(str.getBytes()));
        System.out.println("Unicode=" + str.getBytes(Charset.forName("UNICODE")).length + "" +
                "  HEX=" + UnicodeUtils.bytesToHexString(str.getBytes(Charset.forName("UNICODE"))));
        System.out.println("US-ASCII=" + str.getBytes(StandardCharsets.US_ASCII).length + "" +
                "  HEX=" + UnicodeUtils.bytesToHexString(str.getBytes(StandardCharsets.US_ASCII)));
        System.out.println("ISO-8859-1=" + str.getBytes(StandardCharsets.ISO_8859_1).length + "" +
                "  HEX=" + UnicodeUtils.bytesToHexString(str.getBytes(StandardCharsets.ISO_8859_1)));
        System.out.println("UTF-8=" + str.getBytes(StandardCharsets.UTF_8).length + "" +
                "  HEX=" + UnicodeUtils.bytesToHexString(str.getBytes(StandardCharsets.UTF_8)));
        System.out.println("UTF-16=" + str.getBytes(StandardCharsets.UTF_16).length + "" +
                "  HEX=" + UnicodeUtils.bytesToHexString(str.getBytes(StandardCharsets.UTF_16)));
        System.out.println("UTF-16BE=" + str.getBytes(StandardCharsets.UTF_16BE).length + "" +
                "  HEX=" + UnicodeUtils.bytesToHexString(str.getBytes(StandardCharsets.UTF_16BE)));
        System.out.println("UTF-16LE=" + str.getBytes(StandardCharsets.UTF_16LE).length + "" +
                "  HEX=" + UnicodeUtils.bytesToHexString(str.getBytes(StandardCharsets.UTF_16LE)));
        System.out.println("UTF-32BE=" + str.getBytes("UTF-32BE").length + "" +
                "  HEX=" + UnicodeUtils.bytesToHexString(str.getBytes("UTF-32BE")));
        System.out.println("UTF-32LE=" + str.getBytes("UTF-32LE").length + "" +
                "  HEX=" + UnicodeUtils.bytesToHexString(str.getBytes("UTF-32LE")));
    }

    private static void downloadJson() throws IOException {
        File file = new File(System.getProperty("user.dir"), "emoji.json");
        String json = IOUtils.readStringThrown(new URL("https://raw.githubusercontent.com/iamcal/emoji-data/master/emoji.json").openStream(), "utf-8");
        //IOUtils.writeString(file.getAbsolutePath(), json);
        Gson gson = new Gson().newBuilder().disableHtmlEscaping().create();
        List<Emoji> emojiList = gson.fromJson(json, new TypeToken<List<Emoji>>() {
        }.getType());
        String content = new Gson().toJson(emojiList);
        IOUtils.writeStringThrown(file.getAbsolutePath(), content, "utf-8");
        System.out.println("Downloaded: " + file);
    }

    private static void mapSBToUnified() throws IOException {
        File file = new File(System.getProperty("user.dir"), "softbank_unified.json");
        String json = IOUtils.readStringThrown(new File(System.getProperty("user.dir"), "emoji.json").getAbsolutePath(), "utf-8");
        Gson gson = new Gson().newBuilder().disableHtmlEscaping().create();
        List<Emoji> emojiList = gson.fromJson(json, new TypeToken<List<Emoji>>() {
        }.getType());
        HashMap<String, String> map = new HashMap<>();
        for (Emoji emoji : emojiList) {
            String softbank = emoji.getSoftbank();
            if (softbank != null && softbank.length() > 0) {
                String unified = emoji.getUnified();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                if (unified.contains("-")) {
                    String[] us = unified.split("-");
                    for (String u : us) {
                        byte[] bytes = UnicodeUtils.singleHexStringToBytes(u);
                        baos.write(bytes, 0, bytes.length);
                    }
                } else {
                    byte[] bytes = UnicodeUtils.singleHexStringToBytes(unified);
                    baos.write(bytes, 0, bytes.length);
                }
                byte[] unicodeBytes = baos.toByteArray();
                map.put(softbank, UnicodeUtils.bytesToHexString(unicodeBytes));
                baos.close();
            }
        }
        String content = new Gson().toJson(map);
        IOUtils.writeStringThrown(file.getAbsolutePath(), content, "utf-8");
        System.out.println("mapped: " + file);
    }

    private static void decodeSBToEmoji() throws IOException {
        File asCharacterFile = new File(System.getProperty("user.dir"), "softbank_decode.json");
        File asUnicodeFile = new File(System.getProperty("user.dir"), "softbank_unicode.json");
        File asMapFile = new File(System.getProperty("user.dir"), "softbank_map.java");
        String json = IOUtils.readStringThrown(new File(System.getProperty("user.dir"), "emoji.json").getAbsolutePath(), "utf-8");
        Gson gson = new Gson().newBuilder().disableHtmlEscaping().create();
        List<Emoji> emojiList = gson.fromJson(json, new TypeToken<List<Emoji>>() {
        }.getType());
        HashMap<String, String> characterMap = new HashMap<>();
        HashMap<String, String> unicodeMap = new HashMap<>();
        StringBuilder mapBuilder = new StringBuilder();
        mapBuilder.append("private static final HashMap<String, String> SB_UNICODE = new HashMap<>();\n\n");
        mapBuilder.append("/**\n");
        mapBuilder.append("* SoftBank版本的Emoji表情与Unified版本的Emoji表情的UNICODE对应关系\n");
        mapBuilder.append("*/\n");
        mapBuilder.append("private static String obtainUnicodeBySoftBank(String softbank) {\n");
        mapBuilder.append("    if (SB_UNICODE.size() == 0) {\n");
        for (Emoji emoji : emojiList) {
            String softbank = emoji.getSoftbank();
            if (softbank != null && softbank.length() > 0) {
                String unified = emoji.getUnified();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                if (unified.contains("-")) {
                    String[] us = unified.split("-");
                    for (String u : us) {
                        byte[] bytes = UnicodeUtils.singleHexStringToBytes(u);
                        baos.write(bytes, 0, bytes.length);
                    }
                } else {
                    byte[] bytes = UnicodeUtils.singleHexStringToBytes(unified);
                    baos.write(bytes, 0, bytes.length);
                }
                byte[] unicodeBytes = baos.toByteArray();
                String str = new String(unicodeBytes, Charset.forName("UTF-32BE"));
                baos.close();
                String hexStr = UnicodeUtils.toUnicodeFormal(str);
                mapBuilder.append("        SB_UNICODE.put(\"").append(softbank).append("\", \"");
                mapBuilder.append(hexStr.replace("\\u", "\\\\u")).append("\");\n");
                characterMap.put(softbank, str);
                unicodeMap.put(softbank, hexStr);
            }
        }
        mapBuilder.append("    }\n");
        mapBuilder.append("    return SB_UNICODE.get(softbank);\n");
        mapBuilder.append("}\n");
        IOUtils.writeStringThrown(asCharacterFile.getAbsolutePath(), new Gson().toJson(characterMap), "utf-8");
        System.out.println("mapped as character: " + asCharacterFile);
        IOUtils.writeStringThrown(asUnicodeFile.getAbsolutePath(), new Gson().toJson(unicodeMap), "utf-8");
        System.out.println("mapped as unicode: " + asUnicodeFile);
        IOUtils.writeStringThrown(asMapFile.getAbsolutePath(), mapBuilder.toString(), "utf-8");
        System.out.println("mapped as map: " + asMapFile);
    }

}
