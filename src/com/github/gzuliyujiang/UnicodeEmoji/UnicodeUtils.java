package com.github.gzuliyujiang.UnicodeEmoji;

import java.nio.charset.Charset;

/**
 * Unicode处理工具类。
 * 含Emoji表情处理、中日韩字符判断、Unicode格式化表示等，可用于解决微信登录Emoji表情昵称乱码问题。
 * <p>
 * Created by liyujiang on 2019/11/21
 *
 * @author 大定府羡民
 */
@SuppressWarnings({"unused", "CharsetObjectCanBeUsed", "WeakerAccess"})
public class UnicodeUtils {

    /**
     * Emoji表情有很多种版本，其中包括Unified、DoCoMo、KDDI、SoftBank和Google，不同版本的Unicode代码并不一定相同。
     * <pre>
     * 微信昵称中的Emoji表情截止2019.12.06已知支持三种版本：
     * 1、SoftBank版本（网上一般称之为SB Unicode），如😂为E412；
     * 2、Unified版本，如😂为1F602；
     * 3、自定义表情版本，如[捂脸]。
     * 😂(喜极而泣)的各种编码如下：
     * SoftBank：0000E412
     * Unified：0001F602（U+1F602）
     * DoCoMo：0000E72A
     * KDDI：0000EB64
     * Google：000FE334
     * UTF-8：F09F9882（%F0%9F%98%82）
     * UTF-16BE：FEFFD83DDE02（\uD83D\uDE02）
     * UTF-16LE：FFFE3DD802DE
     * UTF-32BE：0000FEFF0001F602
     * UTF-32LE：FFFE000002F60100
     * Emoji表情代码表参阅：
     * http://punchdrunker.github.io/iOSEmoji/table_html/index.html
     * https://github.com/iamcal/emoji-data/blob/master/emoji.json
     * https://github.com/google/emoji4unicode/blob/master/data/emoji4unicode.xml
     * </pre>
     */
    public static String emojiEncode(boolean encodeCJK, String str) {
        if (str == null || "".equals(str.trim())) {
            return "";
        }
        StringBuilder sb = new StringBuilder(str.length());
        char[] chars = str.toCharArray();
        for (int i = 0, n = chars.length; i < n; i++) {
            int codePoint = str.codePointAt(i);
            char aChar = str.charAt(i);
            if ((encodeCJK && isCJKCharacter(codePoint)) || isEmojiCharacter(codePoint)) {
                String unicodeFormal = toUnicodeFormal(aChar);
                sb.append(unicodeFormal);
            } else {
                sb.append(aChar);
            }
        }
        return sb.toString();
    }

    public static String toUnicodeFormal(String str) {
        StringBuilder sb = new StringBuilder();
        byte[] utf16beBytes = str.getBytes(Charset.forName("UTF-16BE"));
        String hexString = bytesToHexString(utf16beBytes);
        for (int i = 0, n = hexString.length(); i < n; i = i + 4) {
            sb.append("\\u").append(hexString.charAt(i)).append(hexString.charAt(i + 1)).append(hexString.charAt(i + 2)).append(hexString.charAt(i + 3));
        }
        return sb.toString();
    }

    public static boolean containsCJK(String str) {
        return hasMultiCharacter(str, true, false);
    }

    public static boolean containsEmoji(String str) {
        return hasMultiCharacter(str, false, true);
    }

    public static String bytesToHexString(byte[] bArray) {
        int length = bArray.length;
        StringBuilder sb = new StringBuilder(length);
        String sTemp;
        for (byte b : bArray) {
            sTemp = Integer.toHexString(0xFF & b);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public static byte[] singleHexStringToBytes(String hexString) {
        if (hexString == null || hexString.length() == 0 || hexString.length() > 8) {
            throw new IllegalArgumentException();
        }
        int anInt = Integer.parseInt(hexString, 16);
        return intToBytes(anInt);
    }

    private static byte[] intToBytes(int integer) {
        int byteNum = (40 - Integer.numberOfLeadingZeros(integer < 0 ? ~integer : integer)) / 8;
        byte[] byteArray = new byte[4];
        for (int n = 0; n < byteNum; n++) {
            byteArray[3 - n] = (byte) (integer >>> (n * 8));
        }
        return byteArray;
    }

    private static String toUnicodeFormal(char aChar) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\\u");
        String unicode = Integer.toHexString(aChar);
        if (unicode.length() <= 2) {
            stringBuilder.append("00");
        }
        stringBuilder.append(unicode.toUpperCase());
        return stringBuilder.toString();
    }

    private static boolean hasMultiCharacter(String str, boolean containsCJK, boolean containEmoji) {
        if (str == null || "".equals(str.trim())) {
            return false;
        }
        int cpCount = str.codePointCount(0, str.length());
        int firCodeIndex = str.offsetByCodePoints(0, 0);
        int lstCodeIndex = str.offsetByCodePoints(0, cpCount - 1);
        for (int index = firCodeIndex; index <= lstCodeIndex; index++) {
            int codePoint = str.codePointAt(index);
            if (containsCJK && isCJKCharacter(codePoint)) {
                return true;
            }
            if (containEmoji && isEmojiCharacter(codePoint)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是中日韩字符
     * <p>
     * Unicode编码范围：
     * 汉字：[0x4e00,0x9fa5]（或十进制[19968,40869]）
     * 数字：[0x30,0x39]（或十进制[48, 57]）
     * 小写字母：[0x61,0x7a]（或十进制[97, 122]）
     * 大写字母：[0x41,0x5a]（或十进制[65, 90]）
     * <p>
     * UTF-8编码范围：
     * 中文：[\u4e00-\u9fa5]
     * 日文：[\u0800-\u4e00]
     * 韩文：[\uac00-\ud7ff]
     */
    private static boolean isCJKCharacter(int codePoint) {
        //noinspection ConstantConditions
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            // Determines if the specified character (Unicode code point) is a CJKV
            // (Chinese, Japanese, Korean and Vietnamese) ideograph, as defined by
            // the Unicode Standard.
            return Character.isIdeographic(codePoint);
        }
        boolean isChinese = 0x4e00 <= codePoint && codePoint < 0x9fa5;
        boolean isJapanese = 0x0800 <= codePoint && codePoint < 0x4e00;
        boolean isKorean = 0xac00 <= codePoint && codePoint < 0xd7ff;
        return isChinese || isJapanese || isKorean;
    }

    /**
     * 判断是否是Emoji表情符号，参阅 https://www.cnblogs.com/hahahjx/p/4522913.html
     * <p>
     * 杂项象形符号:1F300-1F5FF
     * 表情符号：1F600-1F64F
     * 交通和地图符号:1F680-1F6FF
     * 杂项符号：2600-26FF
     * 符号字体:2700-27BF
     * 国旗：1F100-1F1FF
     * 箭头：2B00-2BFF 2900-297F
     * 各种技术符号：2300-23FF
     * 字母符号: 2100–214F
     * 中文符号： 303D 3200–32FF 2049 203C
     * Private Use Area:E000-F8FF;
     * High Surrogates D800..DB7F;
     * High Private Use Surrogates  DB80..DBFF
     * Low Surrogates DC00..DFFF  D800-DFFF E000-F8FF
     * 标点符号：2000-200F 2028-202F 205F 2065-206F
     * 变异选择器：IOS独有 FE00-FE0F
     */
    private static boolean isEmojiCharacter(int codePoint) {
        return (codePoint >= 0x2600 && codePoint <= 0x27BF) // 杂项符号与符号字体
                || codePoint == 0x303D
                || codePoint == 0x2049
                || codePoint == 0x203C
                || (codePoint >= 0x2000 && codePoint <= 0x200F)//
                || (codePoint >= 0x2028 && codePoint <= 0x202F)//
                || codePoint == 0x205F //
                || (codePoint >= 0x2065 && codePoint <= 0x206F)//
                || (codePoint >= 0x2100 && codePoint <= 0x214F)// 字母符号
                || (codePoint >= 0x2300 && codePoint <= 0x23FF)// 各种技术符号
                || (codePoint >= 0x2B00 && codePoint <= 0x2BFF)// 箭头A
                || (codePoint >= 0x2900 && codePoint <= 0x297F)// 箭头B
                || (codePoint >= 0x3200 && codePoint <= 0x32FF)// 中文符号
                || (codePoint >= 0xD800 && codePoint <= 0xDFFF)// 高低位替代符保留区域
                || (codePoint >= 0xE000 && codePoint <= 0xF8FF)// 私有保留区域
                || (codePoint >= 0xFE00 && codePoint <= 0xFE0F)// 变异选择器
                || codePoint >= 0x10000; // Plane在第二平面以上的，char都不可以存，全部都转
    }

}
