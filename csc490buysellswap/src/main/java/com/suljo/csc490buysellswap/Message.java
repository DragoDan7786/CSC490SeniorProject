package com.suljo.csc490buysellswap;

import java.time.LocalDateTime;

public class Message {
    private Integer messageID;
    private String fromUsername;
    private String toUsername;
    private LocalDateTime datetimeSent;
    private Integer aboutListingID;
    private String subject;
    private String contents;

    public Message(Integer messageID, String fromUsername, String toUsername, LocalDateTime datetimeSent,
                   Integer aboutListingID, String subject, String contents) {
        this.messageID = messageID;
        this.fromUsername = fromUsername;
        this.toUsername = toUsername;
        this.datetimeSent = datetimeSent;
        this.aboutListingID = aboutListingID;
        this.subject = subject;
        this.contents = contents;
    }

    public Integer getMessageID() {
        return messageID;
    }

    public void setMessageID(Integer messageID) {
        this.messageID = messageID;
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    public LocalDateTime getDatetimeSent() {
        return datetimeSent;
    }

    public void setDatetimeSent(LocalDateTime datetimeSent) {
        this.datetimeSent = datetimeSent;
    }

    public Integer getAboutListingID() {
        return aboutListingID;
    }

    public void setAboutListingID(Integer aboutListingID) {
        this.aboutListingID = aboutListingID;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
