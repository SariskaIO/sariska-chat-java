package com.github.eoinsha.javaphoenixchannels.sample.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
class ChatMessage {
    private String content;
    private String userId;
    private Date insertedDate;

    public ChatMessage() {
    }

    public String getBody() {
        return content;
    }

    public String getInsertedDate() {
        return this.insertedDate;
    }

    boolean isFromMe(String userId) {
        return (this.userId===userId);
    }

    public String getUserId() {
        return this.userId;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                ", content='" + content + '\'' +
                '}';
    }
}
