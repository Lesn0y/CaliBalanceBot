package org.lesnoy.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = UserDTODeserializer.class)
public final class UserDTO {

    private int id;
    private String login;
    private int age;
    private float height;
    private float weight;
    private String sex;
    private String goal;
    private String activity;
    private float cal;
    private float prot;
    private float fats;
    private float carbc;


    public UserDTO() {
    }

    public UserDTO(int id, String login, int age, float height, float weight, String sex, String goal, String activity, float cal, float prot, float fats, float carbc) {
        this.id = id;
        this.login = login;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.sex = sex;
        this.goal = goal;
        this.activity = activity;
        this.cal = cal;
        this.prot = prot;
        this.fats = fats;
        this.carbc = carbc;
    }

    public UserDTO(String login) {
        this.login = login;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public float getFats() {
        return fats;
    }

    public void setFats(float fats) {
        this.fats = fats;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
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

    public float getCarbc() {
        return carbc;
    }

    public void setCarbc(float carbc) {
        this.carbc = carbc;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", age=" + age +
                ", height=" + height +
                ", weight=" + weight +
                ", sex='" + sex + '\'' +
                ", goal='" + goal + '\'' +
                ", activity='" + activity + '\'' +
                ", cal=" + cal +
                ", prot=" + prot +
                ", fats=" + fats +
                ", carbc=" + carbc +
                '}';
    }
}
