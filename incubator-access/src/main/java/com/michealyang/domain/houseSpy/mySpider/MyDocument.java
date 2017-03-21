package com.michealyang.domain.houseSpy.mySpider;

import org.jsoup.nodes.Document;

/**
 * Created by michealyang on 17/3/17.
 */
public class MyDocument {

    private String html;

    private String title;

    private Document jsoupDoucument;

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Document getJsoupDoucument() {
        return jsoupDoucument;
    }

    public void setJsoupDoucument(Document jsoupDoucument) {
        this.jsoupDoucument = jsoupDoucument;
    }
}
