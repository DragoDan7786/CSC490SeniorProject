package com.suljo.csc490buysellswap;

import javafx.beans.property.*;

import java.time.LocalDateTime;

public class Message {
    private IntegerProperty messageID;
    private StringProperty fromUsername;
    private StringProperty toUsername;
    private ObjectProperty<LocalDateTime> datetimeSent;
    private StringProperty readableDatetimeSent;
    private IntegerProperty aboutListingID;
    private StringProperty subject;
    private StringProperty contents;

    public Message(Integer messageID, String fromUsername, String toUsername, LocalDateTime datetimeSent,
                   Integer aboutListingID, String subject, String contents) {
        this.messageID = new SimpleIntegerProperty(messageID);
        this.fromUsername = new SimpleStringProperty(fromUsername);
        this.toUsername = new SimpleStringProperty(toUsername);
        this.datetimeSent = new SimpleObjectProperty<>(datetimeSent);
        this.readableDatetimeSent = new SimpleStringProperty(DateTimeUtil.localDateTimeTo12HourClockString(datetimeSent));
        this.aboutListingID = new SimpleIntegerProperty(aboutListingID);
        this.subject = new SimpleStringProperty(subject);
        this.contents = new SimpleStringProperty(contents);
    }

    public Integer getMessageID() {
        return messageID.get();
    }

    public void setMessageID(Integer messageID) {
        this.messageID.set(messageID);
    }

    public IntegerProperty messageIDProperty(){
        return messageID;
    }

    public String getFromUsername() {
        return fromUsername.get();
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername.set(fromUsername);
    }

    public StringProperty fromUsernameProperty(){
        return fromUsername;
    }

    public String getToUsername() {
        return toUsername.get();
    }

    public void setToUsername(String toUsername) {
        this.toUsername.set(toUsername);
    }

    public StringProperty toUsernameProperty(){
        return toUsername;
    }

    public LocalDateTime getDatetimeSent() {
        return datetimeSent.get();
    }

    public void setDatetimeSent(LocalDateTime datetimeSent) {
        this.datetimeSent.set(datetimeSent);
        readableDatetimeSent.set(DateTimeUtil.localDateTimeTo12HourClockString(datetimeSent));
    }

    public ObjectProperty<LocalDateTime> datetimeSentProperty(){
        return datetimeSent;
    }

    public String getReadableDatetimeSent(){
        return readableDatetimeSent.get();
    }

    public StringProperty readableDatetimeSentProperty(){
        return readableDatetimeSent;
    }

    public Integer getAboutListingID() {
        return aboutListingID.get();
    }

    public void setAboutListingID(Integer aboutListingID) {
        this.aboutListingID.set(aboutListingID);
    }

    public IntegerProperty aboutListingIDProperty(){
        return aboutListingID;
    }

    public String getSubject() {
        return subject.get();
    }

    public void setSubject(String subject) {
        this.subject.set(subject);
    }

    public StringProperty subjectProperty(){
        return subject;
    }

    public String getContents() {
        return contents.get();
    }

    public void setContents(String contents) {
        this.contents.set(contents);
    }

    public StringProperty contentsProperty() {
        return contents;
    }
}
