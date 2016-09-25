package com.example.limethecoder.eventkicker.model;

public class EventItem {

    private String authorName;
    private String timeCreated;
    private String timeScheduled;
    private int authorId;
    private String description;
    private String name;
    private int id;
    private String pictureUrl;

    public EventItem(){}

    public EventItem(String eventName, String authorName, String imgUrl, String description, String target_time)  {
        name = eventName;
        this.authorName = authorName;
        this.description = description;
        pictureUrl = imgUrl;
        timeScheduled = target_time;
    }

    public String getSchParsedDate() {
        return getParsedDate(timeScheduled);
    }

    public String getParsedCreateDate() {
        return getParsedDate(timeCreated);
    }

    public  String getParsedDate(String date) {
        String year = date.substring(2, 4);
        String month = date.substring(4, 6);
        String day = date.substring(6, 8);
        return day + "/" + month + "/" + year;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getTimeScheduled() {
        return timeScheduled;
    }

    public void setTimeScheduled(String timeScheduled) {
        this.timeScheduled = timeScheduled;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}