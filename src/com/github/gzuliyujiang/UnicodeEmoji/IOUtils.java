package com.github.gzuliyujiang.UnicodeEmoji;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * IO工具类
 * Created by liyujiang on 2019/6/28
 *
 * @author 大定府羡民
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class IOUtils {

    protected IOUtils() {
        throw new UnsupportedOperationException("You can't instantiate me");
    }

    public static void closeSilently(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException ignore) {
            // do nothing
        }
    }

    public static byte[] readBytes(InputStream is) {
        try {
            return readBytesThrown(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] readBytes(String filepath) {
        try {
            return readBytesThrown(filepath);
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[]{};
        }
    }

    public static byte[] readBytesThrown(String filepath) throws IOException {
        return readBytesThrown(new FileInputStream(filepath));
    }

    public static byte[] readBytesThrown(InputStream is) throws IOException {
        if (is == null) {
            return null;
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buff = new byte[524288];
        while (true) {
            int len = is.read(buff);
            if (len == -1) {
                break;
            } else {
                os.write(buff, 0, len);
            }
        }
        byte[] bytes = os.toByteArray();
        closeSilently(os);
        closeSilently(is);
        return bytes;
    }

    public static String readString(InputStream is) {
        try {
            return readStringThrown(is, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String readString(String filepath) {
        try {
            return readStringThrown(filepath, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String readStringThrown(InputStream is, String charset) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset));
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            } else {
                sb.append(line).append("\n");
            }
        }
        closeSilently(reader);
        closeSilently(is);
        return sb.toString();
    }

    public static String readStringThrown(String filepath, String charset) throws IOException {
        return readStringThrown(new FileInputStream(filepath), charset);
    }

    public static boolean writeBytes(String filepath, byte[] data) {
        try {
            writeBytesThrown(filepath, data);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void writeBytesThrown(String filepath, byte[] data) throws IOException {
        File file = new File(filepath);
        if (!file.exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.getParentFile().mkdirs();
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(filepath);
        fos.write(data);
        closeSilently(fos);
    }

    public static void writeStringThrown(String filepath, String content, String charset) throws IOException {
        writeBytesThrown(filepath, content.getBytes(charset));
    }

    public static boolean writeString(String filepath, String content) {
        try {
            writeStringThrown(filepath, content, "utf-8");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean appendString(String path, String content) {
        try {
            appendStringThrown(path, content);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void appendStringThrown(String path, String content) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
        }
        FileWriter writer = new FileWriter(file, true);
        writer.write(content);
        closeSilently(writer);
    }

}
