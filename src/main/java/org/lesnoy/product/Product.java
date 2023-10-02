package org.lesnoy.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {

    private int id;
    private String name;
    @JsonProperty("product_type")
    private ProductType productType;
    private int grams;
    private float cal;
    private float prot;
    private float fats;
    private float carbs;

    public Product() {
    }

    public Product(String name, String productType, int grams, float cal, float prot, float fats, float carbs) {
        this.name = name;
        this.productType = ProductType.valueOf(productType);
        this.grams = grams;
        this.cal = cal;
        this.prot = prot;
        this.fats = fats;
        this.carbs = carbs;
    }

    public Product(int id, String name, String productType, int grams, float cal, float prot, float fats, float carbs) throws Exception {
        this.id = id;
        this.name = name;
        this.productType = ProductType.getTypeByName(productType);
        this.grams = grams;
        this.cal = cal;
        this.prot = prot;
        this.fats = fats;
        this.carbs = carbs;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public Product(String name) {
        this.name = name;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductTypeByTypeName(String typeName) throws Exception {
        this.productType = ProductType.getTypeByName(typeName);
    }

    public int getId() {
        return id;
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