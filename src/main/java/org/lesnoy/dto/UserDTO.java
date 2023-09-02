package org.lesnoy.dto;

public final class UserDTO {
    private final String login;
    private int age;
    private float height;
    private float weight;
    private String sex;
    private String goal;
    private String activity;

    public UserDTO(String login) {
        this.login = login;
    }

    public UserDTO(String login, int age, float height, float weight, String sex, String goal, String activity) {
        this.login = login;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.sex = sex;
        this.goal = goal;
        this.activity = activity;
    }

    public String getLogin() {
        return login;
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

    @Override
    public String toString() {
        return "UserDTO{" +
                "login='" + login + '\'' +
                ", age=" + age +
                ", height=" + height +
                ", weight=" + weight +
                ", sex='" + sex + '\'' +
                ", goal='" + goal + '\'' +
                ", activity='" + activity + '\'' +
                '}';
    }
}
