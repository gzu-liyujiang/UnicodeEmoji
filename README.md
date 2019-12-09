# Unicode处理工具类。

含Emoji表情处理、中日韩字符判断、Unicode格式化表示等，可用于解决微信登录Emoji表情昵称乱码问题。

Emoji表情有很多种版本，其中包括Unified、DoCoMo、KDDI、SoftBank和Google，不同版本的Unicode代码并不一定相同。
<pre>
微信昵称中的Emoji表情截止2019.12.06已知支持三种版本：
1、SoftBank版本（网上一般称之为SB Unicode），如😂为E412；
2、Unified版本，如😂为\uD83D\uDE02；
3、自定义表情版本，如😂为[笑脸]。
😂(笑脸)的各种编码如下：
SoftBank：E412
Unified：1F602
DoCoMo：E72A
KDDI：EB64
Google：FE334
UTF-8：F09F9882
UTF-16BE：FEFFD83DDE02
UTF-16LE：FFFE3DD802DE
UTF-32BE：0000FEFF0001F602
UTF-32LE：FFFE000002F60100
Emoji表情代码表参阅：
http://punchdrunker.github.io/iOSEmoji/table_html/index.html
https://github.com/iamcal/emoji-data/blob/master/emoji.json
https://github.com/google/emoji4unicode/blob/master/data/emoji4unicode.xml
</pre>
