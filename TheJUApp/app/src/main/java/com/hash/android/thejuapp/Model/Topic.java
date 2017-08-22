package com.hash.android.thejuapp.Model;


public class Topic {
    private int image;
    private String topicName;
    private int tag;

    public Topic(int image, String topicName, int tag) {
        this.image = image;
        this.topicName = topicName;
        this.tag = tag;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
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
