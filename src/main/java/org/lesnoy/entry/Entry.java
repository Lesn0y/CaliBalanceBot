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
