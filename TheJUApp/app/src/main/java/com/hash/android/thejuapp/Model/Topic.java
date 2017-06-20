package com.hash.android.thejuapp.Model;


public class Topic {
    private int image;
    private String topicName;

    public Topic(int image, String topicName) {
        this.image = image;
        this.topicName = topicName;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
}
