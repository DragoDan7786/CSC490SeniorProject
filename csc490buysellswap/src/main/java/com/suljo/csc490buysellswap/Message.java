package com.suljo.csc490buysellswap;

import javafx.beans.property.*;

import java.time.LocalDateTime;
import java.util.Objects;

public class Message {
    private IntegerProperty messageID;
    private StringProperty fromUsername;
    private StringProperty toUsername;
    private ObjectProperty<LocalDateTime> datetimeSent;
    private StringProperty displayDatetimeSent;
    private IntegerProperty aboutListingID;
    private StringProperty subject;
    private StringProperty contents;
    private IntegerProperty replyToMessageID;
    private BooleanProperty hidden;

    public Message(Integer messageID, String fromUsername, String toUsername, LocalDateTime datetimeSent,
                   Integer aboutListingID, String subject, String contents, Integer replyToMessageID, boolean hidden) {
        this.messageID = new SimpleIntegerProperty(messageID);
        this.fromUsername = new SimpleStringProperty(fromUsername);
        this.toUsername = new SimpleStringProperty(toUsername);
        this.datetimeSent = new SimpleObjectProperty<>(datetimeSent);
        this.displayDatetimeSent = new SimpleStringProperty(DateTimeUtil.localDateTimeTo12HourClockString(datetimeSent));
        this.aboutListingID = new SimpleIntegerProperty(aboutListingID);
        this.subject = new SimpleStringProperty(subject);
        this.contents = new SimpleStringProperty(contents);
        this.replyToMessageID = new SimpleIntegerProperty(replyToMessageID);
        this.hidden = new SimpleBooleanProperty(hidden);
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
        displayDatetimeSent.set(DateTimeUtil.localDateTimeTo12HourClockString(datetimeSent));
    }

    public ObjectProperty<LocalDateTime> datetimeSentProperty(){
        return datetimeSent;
    }

    public String getDisplayDatetimeSent(){
        return displayDatetimeSent.get();
    }

    public StringProperty displayDatetimeSentProperty(){
        return displayDatetimeSent;
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

    public int getReplyToMessageID() {
        return replyToMessageID.get();
    }

    public IntegerProperty replyToMessageIDProperty() {
        return replyToMessageID;
    }

    public void setReplyToMessageID(int replyToMessageID) {
        this.replyToMessageID.set(replyToMessageID);
    }

    public boolean isHidden() {
        return hidden.get();
    }

    public BooleanProperty hiddenProperty() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden.set(hidden);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Message message = (Message) other;
        return Objects.equals(messageID, message.messageID) && Objects.equals(fromUsername, message.fromUsername) && Objects.equals(toUsername, message.toUsername) && Objects.equals(datetimeSent, message.datetimeSent) && Objects.equals(displayDatetimeSent, message.displayDatetimeSent) && Objects.equals(aboutListingID, message.aboutListingID) && Objects.equals(subject, message.subject) && Objects.equals(contents, message.contents) && Objects.equals(replyToMessageID, message.replyToMessageID) && Objects.equals(hidden, message.hidden);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageID, fromUsername, toUsername, datetimeSent, displayDatetimeSent, aboutListingID, subject, contents, replyToMessageID, hidden);
    }
}
