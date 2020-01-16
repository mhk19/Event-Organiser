package com.example.eventorganiser;

public class Feedback {
    private String username;
    private String feedback;

    public Feedback(String username, String feedback) {
        this.username = username;
        this.feedback = feedback;
    }

    public Feedback() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
