package com.example.daygram;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by yubin on 2016/9/15.
 */
public class ContentList implements Serializable {

    private static final long serialVersionUID = 1L;

    private String week;

    private String date;

    private String content;

    public String getWeek() {
        return this.week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}


