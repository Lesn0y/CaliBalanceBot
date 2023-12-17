package org.lesnoy.user;

public class UserCallInfoDTO {
    private float cal;
    private float prot;
    private float fats;
    private float carbs;

    public UserCallInfoDTO() {
    }

    public UserCallInfoDTO(float cal, float prot, float fats, float carbs) {
        this.cal = cal;
        this.prot = prot;
        this.fats = fats;
        this.carbs = carbs;
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
