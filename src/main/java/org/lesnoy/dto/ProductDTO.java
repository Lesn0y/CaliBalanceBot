package org.lesnoy.dto;

public class ProductDTO {
    private String name;
    private int grams;
    private float cal;
    private float prot;
    private float fats;
    private float carbs;

    public ProductDTO() {
    }

    public ProductDTO(String name, int grams, float cal, float prot, float fats, float carbs) {
        this.name = name;
        this.grams = grams;
        this.cal = cal;
        this.prot = prot;
        this.fats = fats;
        this.carbs = carbs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGrams() {
        return grams;
    }

    public void setGrams(int grams) {
        this.grams = grams;
    }

    public float getCal() {
        return cal;
    }

    public void setCal(float cal) {
        this.cal = cal;
    }

    public float getProt() {
        return prot;
    }

    public void setProt(float prot) {
        this.prot = prot;
    }

    public float getFats() {
        return fats;
    }

    public void setFats(float fats) {
        this.fats = fats;
    }

    public float getCarbs() {
        return carbs;
    }

    public void setCarbs(float carbs) {
        this.carbs = carbs;
    }
}