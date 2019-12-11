# Unicode处理工具类。

含Emoji表情处理、中日韩字符判断、Unicode格式化表示等，可用于解决微信登录Emoji表情昵称乱码问题。

微信原始昵称含三种Emoji表情：![](/screenshot/wechat_nick_original.jpg)   
微信授权昵称未处理Emoji表情：![](/screenshot/wechat_nick_normal.jpg)   
微信授权昵称已处理Emoji表情：![](/screenshot/wechat_nick_handler.jpg)   

```java
        String url = String.format("https://api.weixin.qq.com/sns/userinfo?" +
                "access_token=%s&openid=%s", wxToken.getAccess_token(), wxToken.getOpenid());
        HttpClient.get(url, new TextCallback() {
            @Override
            public void onSuccess(Map<String, List<String>> headers, String result) {
                Logger.debug("获取微信用户信息UTF8-Emoji编码前:" + result);
                result = UnicodeUtils.emojiEncode(false, result);
                Logger.debug("获取微信用户信息UTF8-Emoji编码后:" + result);
                WXUserInfo wxUserInfo = new Gson().fromJson(result, WXUserInfo.class);
                WXAuthCallback callback = WeChatSDK.getAuthCallback();
                if (callback != null) {
                    callback.onUserInfoReceived(wxUserInfo);
                }
            }

            @Override
            public void onError(int code, Throwable throwable) {
                if (callback != null) {
                    callback.onTokenCheckFailed("微信用户信息获取出错: " + code);
                }
            }
        });
```
![](/screenshot/wechat_nick_compare.jpg)

Emoji表情有很多种版本，其中包括Unified、DoCoMo、KDDI、SoftBank和Google，不同版本的Unicode代码并不一定相同。

<pre>
微信昵称中的Emoji表情截止2019.12.06已知支持三种版本：
1、SoftBank版本（网上一般称之为SB Unicode），如😂为E412；
2、Unified版本，如😂为1F602；
3、自定义表情版本，如[捂脸]。

举个例子，😂(喜极而泣)的各种编码如下：
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

## [Unified、DoCoMo、KDDI、SoftBank和Google等个版本之间的编码对应关系](/emoji.json)

```text
[
  {
    "name": "HASH KEY",
    "unified": "0023-FE0F-20E3",
    "docomo": "E6E0",
    "au": "EB84",
    "softbank": "E210",
    "google": "FE82C"
  },
  {
    "unified": "002A-FE0F-20E3"
  },
  {
    "name": "KEYCAP 0",
    "unified": "0030-FE0F-20E3",
    "docomo": "E6EB",
    "au": "E5AC",
    "softbank": "E225",
    "google": "FE837"
  },
  {
    "name": "KEYCAP 1",
    "unified": "0031-FE0F-20E3",
    "docomo": "E6E2",
    "au": "E522",
    "softbank": "E21C",
    "google": "FE82E"
  },
...省略...
]
```

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
    "E150": "\uD83D\uDE8F",
    "E030": "\uD83C\uDF38",
    "E151": "\uD83D\uDEBB",
    "E152": "\uD83D\uDC6E",
    "E031": "\uD83D\uDD31",
    "E032": "\uD83C\uDF39",
    "E153": "\uD83C\uDFE3",
...省略...
}
```

## [SoftBank版本编码与标准的Emoji字符表情的对应关系](/softbank_decode.json)

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
