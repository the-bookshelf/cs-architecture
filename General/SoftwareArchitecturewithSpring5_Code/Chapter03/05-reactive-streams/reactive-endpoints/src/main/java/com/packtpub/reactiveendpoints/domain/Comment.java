package com.packtpub.reactiveendpoints.domain;

import java.util.Date;

public class Comment {

    private String comment;
    private Date date;

    public Comment(String comment, Date date) {
        this.comment = comment;
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
