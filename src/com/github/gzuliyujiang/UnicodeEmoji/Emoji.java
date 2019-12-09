package com.github.gzuliyujiang.UnicodeEmoji;

import java.io.Serializable;

/**
 * 五个版本（Unified、DoCoMo、KDDI、SoftBank和Google）的Emoji表情对应关系
 * 数据来源: https://github.com/iamcal/emoji-data/blob/master/emoji.json
 * <p>
 * Created by liyujiang on 2019/12/9
 *
 * @author 大定府羡民
 */
public class Emoji implements Serializable {
    private String name;
    private String unified;
    private String docomo;
    private String au;
    private String softbank;
    private String google;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnified() {
        return unified;
    }

    public void setUnified(String unified) {
        this.unified = unified;
    }

    public String getDocomo() {
        return docomo;
    }

    public void setDocomo(String docomo) {
        this.docomo = docomo;
    }

    public String getAu() {
        return au;
    }

    public void setAu(String au) {
        this.au = au;
    }

    public String getSoftbank() {
        return softbank;
    }

    public void setSoftbank(String softbank) {
        this.softbank = softbank;
    }

    public String getGoogle() {
        return google;
    }

    public void setGoogle(String google) {
        this.google = google;
    }

}
