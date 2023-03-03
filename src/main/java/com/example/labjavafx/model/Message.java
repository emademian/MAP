package com.example.labjavafx.model;

import java.time.LocalDateTime;

public class Message extends Entity<Long> {
    private Long userFrom;
    private Long userTo;
    private String text;
    private LocalDateTime msgTime;
    private String msgStatus;

    public Message(Long idMsg, Long userFrom, Long userTo, String text, LocalDateTime msgTime, String msgStatus) {
        super.setId(idMsg);
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.text = text;
        this.msgTime = msgTime;
        this.msgStatus = msgStatus;
    }

    public Long getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(Long userFrom) {
        this.userFrom = userFrom;
    }

    public Long getUserTo() {
        return userTo;
    }

    public void setUserTo(Long userTo) {
        this.userTo = userTo;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(LocalDateTime msgTime) {
        this.msgTime = msgTime;
    }

    public String getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(String msgStatus) {
        this.msgStatus = msgStatus;
    }
}
