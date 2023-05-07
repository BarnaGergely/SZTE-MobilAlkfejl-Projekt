package com.example.fallcode;

public class Article {
    private String title;
    private String content;
    private boolean state;
    private String id;

    public Article(String title, String content, boolean state) {
        this.title = title;
        this.content = content;
        this.state = state;
    }

    public String _getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Article() {
        this.title = "";
        this.content = "";
        this.state = false;
        id = "";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
