package com.example.sariska_chat_app_java;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public
class ChatMessage {
    private String content;
    private String id;
    private Date insertedDate;
    private String created_by;

    public ChatMessage() {
    }

    public String getBody() {
        return content;
    }

    public Date getInsertedDate() {
        return this.insertedDate;
    }

    public boolean isFromMe(String userId) {
        return (this.id==userId);
    }

    public String getUserId() {
        return this.id;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                ", content='" + content + '\'' +
                ", created_by='" + created_by + '\'' +
                '}';
    }

}
