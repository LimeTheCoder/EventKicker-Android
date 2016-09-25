package com.example.limethecoder.eventkicker.model;

import com.google.gson.annotations.SerializedName;

public class Comment {
  @SerializedName("id")
  public long id;

  @SerializedName("content")
  public String content;

  @SerializedName("timeCreated")
  public String timeCreated;

  @SerializedName("eventId")
  public long eventId;

  @SerializedName("authorId")
  public long authorId;

  @SerializedName("authorName")
  public String authorName;

  public  String getParsedDate() {
    String year = timeCreated.substring(2, 4);
    String month = timeCreated.substring(4, 6);
    String day = timeCreated.substring(6, 8);
    return day + "/" + month + "/" + year;
  }
}
