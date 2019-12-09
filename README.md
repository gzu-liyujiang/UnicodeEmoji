# Unicode处理工具类。

含Emoji表情处理、中日韩字符判断、Unicode格式化表示等，可用于解决微信登录Emoji表情昵称乱码问题。

```text
str = UnicodeUtils.emojiEncode(false, str);
```
微信昵称Emoji表情解码前：
```json
{
    "nickname": "@测试。。",
    "sex": 1,
    "language": "zh_CN",
    "city": "",
    "province": "",
    "country": "AD",
    "headimgurl": "http://thirdwx.qlogo.cn/mmopen/vi_32/LX7aSR1brjexPRicvmib0jumlFsDt1gLuGS43rzmialiaqfGJyxIaHgVr0xIFQbfGiaChZGZmvu8ZA5fjmciciaoFnMbg/132",
    "privilege": []
}
```
微信昵称Emoji表情解码后：
```json
{
    "city": "",
    "country": "AD",
    "headimgurl": "http://thirdwx.qlogo.cn/mmopen/vi_32/LX7aSR1brjexPRicvmib0jumlFsDt1gLuGS43rzmialiaqfGJyxIaHgVr0xIFQbfGiaChZGZmvu8ZA5fjmciciaoFnMbg/132",
    "language": "zh_CN",
    "nickname": "@测试。。😂",
    "privilege": [],
    "province": "",
    "sex": 1
}
```

Emoji表情有很多种版本，其中包括Unified、DoCoMo、KDDI、SoftBank和Google，不同版本的Unicode代码并不一定相同。

<pre>
微信昵称中的Emoji表情截止2019.12.06已知支持三种版本：
1、SoftBank版本（网上一般称之为SB Unicode），如😂为E412；
2、Unified版本，如😂为1F602；
3、自定义表情版本，如[捂脸]。

😂(喜极而泣)的各种编码如下：
SoftBank：0000E412
Unified：0001F602（U+1F602）
DoCoMo：0000E72A
KDDI：0000EB64
Google：000FE334
UTF-8：F09F9882（%F0%9F%98%82）
UTF-16BE：FEFFD83DDE02（\uD83D\uDE02）
UTF-16LE：FFFE3DD802DE
UTF-32BE：0000FEFF0001F602
UTF-32LE：FFFE000002F60100

Emoji表情代码表参阅：
http://punchdrunker.github.io/iOSEmoji/table_html/index.html
</pre>

## [SoftBank版本编码与Unified版本编码对应关系](/softbank_unified.json)

```text
{
  "E150": "0001F68F",
  "E030": "0001F338",
  "E151": "0001F6BB",
  "E152": "0001F46E",
  "E031": "0001F531",
  "E032": "0001F339",
  "E153": "0001F3E3",
...省略...
}
```

## [SoftBank版本编码与标准Unicode编码对应关系](/softbank_unicode.json)

```text
{
    "E150": "\\uD83D\\uDE8F",
    "E030": "\\uD83C\\uDF38",
    "E151": "\\uD83D\\uDEBB",
    "E152": "\\uD83D\\uDC6E",
    "E031": "\\uD83D\\uDD31",
    "E032": "\\uD83C\\uDF39",
    "E153": "\\uD83C\\uDFE3",
...省略...
}
```

## [SoftBank版本编码与标准的Emoji表情的对应关系](/softbank_decode.json)

```text
{
  "E150": "🚏",
  "E030": "🌸",
  "E151": "🚻",
  "E152": "👮",
  "E031": "🔱",
  "E032": "🌹",
  "E153": "🏣",
...省略...
}
```
