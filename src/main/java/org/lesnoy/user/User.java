package org.lesnoy.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class User {

    private int id;
    private String username;
    private int age;
    private float height;
    private float weight;
    private String sex;
    private String goal;
    private String activity;
    private float cal;
    private float prot;
    private float fats;
    private float carbs;

    public User() {
    }

    public User(int id, String username, int age, float height, float weight, String sex, String goal, String activity, float cal, float prot, float fats, float carbs) {
        this.id = id;
        this.username = username;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.sex = sex;
        this.goal = goal;
        this.activity = activity;
        this.cal = cal;
        this.prot = prot;
        this.fats = fats;
        this.carbs = carbs;
    }

    public User(String username) {
        this.username = username;
    }

    public String getCaloriesInfo() {
        StringBuilder builder = new StringBuilder();

        builder.append("Ваша норма каллорий: ");
        builder.append(cal);
        builder.append("\nВаша норма белков: ");
        builder.append(prot);
        builder.append("\nВаша норма жиров: ");
        builder.append(fats);
        builder.append("\nВаша норма углеводов: ");
        builder.append(carbs);

        return builder.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public float getCarbs() {
        return carbs;
    }

    public void setCarbs(float carbs) {
        this.carbs = carbs;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", login='" + username + '\'' +
                ", age=" + age +
                ", height=" + height +
                ", weight=" + weight +
                ", sex='" + sex + '\'' +
                ", goal='" + goal + '\'' +
                ", activity='" + activity + '\'' +
                ", cal=" + cal +
                ", prot=" + prot +
                ", fats=" + fats +
                ", carbc=" + carbs +
                '}';
    }
}
