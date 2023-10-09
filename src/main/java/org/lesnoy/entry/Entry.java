package org.lesnoy.entry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Entry {

    private String username;
    private Date date;
    @JsonProperty("cal_left")
    private float calLeft;
    @JsonProperty("prot_left")
    private float protLeft;
    @JsonProperty("fats_left")
    private float fatsLeft;
    @JsonProperty("carbs_left")
    private float carbsLeft;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getCalLeft() {
        return calLeft;
    }

    public void setCalLeft(float calLeft) {
        this.calLeft = calLeft;
    }

    public float getProtLeft() {
        return protLeft;
    }

    public void setProtLeft(float protLeft) {
        this.protLeft = protLeft;
    }

    public float getFatsLeft() {
        return fatsLeft;
    }

    public void setFatsLeft(float fatsLeft) {
        this.fatsLeft = fatsLeft;
    }

    public float getCarbsLeft() {
        return carbsLeft;
    }

    public void setCarbsLeft(float carbsLeft) {
        this.carbsLeft = carbsLeft;
    }

    public String getCaloriesInfo() {
        StringBuilder builder = new StringBuilder();

        builder.append("Вам ещё нужно добрать ");
        builder.append(calLeft);
        builder.append(" каллорий.");
        builder.append("\nВам ещё нужно добрать ");
        builder.append(protLeft);
        builder.append(" г. белков.");
        builder.append("\nВам ещё нужно добрать ");
        builder.append(fatsLeft);
        builder.append(" г. жиров.");
        builder.append("\nВам ещё нужно добрать ");
        builder.append(carbsLeft);
        builder.append(" г. углеводов.");

        return builder.toString();
    }
}
