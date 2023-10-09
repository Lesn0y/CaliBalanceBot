package org.lesnoy.entry;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EntryDTO {

    private String username;
    @JsonProperty("product_id")
    private int productId;
    private int grams;

    public EntryDTO() {
    }

    public EntryDTO(String username, int productId, int grams) {
        this.username = username;
        this.productId = productId;
        this.grams = grams;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getGrams() {
        return grams;
    }

    public void setGrams(int grams) {
        this.grams = grams;
    }
}
