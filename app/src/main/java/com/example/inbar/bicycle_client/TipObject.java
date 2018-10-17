package com.example.inbar.bicycle_client;

public class TipObject {

    private String name;
    private String title;
    private String content;

    public TipObject(String name, String content, String title) {
        this.name = name;
        this.content = content;
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitel(String title) {
        this.title = title;
    }

}
