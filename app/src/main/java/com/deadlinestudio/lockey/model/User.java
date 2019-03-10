package com.deadlinestudio.lockey.model;

/**
 * @brief Model Class with user data
 */
public class User {
    private static User user = new User();

    private String id;
    private String nickname;
    private int age;
    private String job;
    private boolean isUser;

    private User() {
        this.id = "";
        this.nickname = "";
        this.age = 0;
        this.job = "";
        this.isUser = false;
    }

    /**
     * @biref Constructor
     * @param id
     * @param nickname
     * @param age
     * @param job
     */
    public void setData(String id, String nickname, int age, String job) {
        this.id = id;
        this.nickname = nickname;
        this.age = age;
        this.job = job;
    }

    public void resetData() {
        this.id = "";
        this.nickname = "";
        this.age = 0;
        this.job = "";
        this.isUser = false;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setisUser(boolean user) { this.isUser = user; }

    public String getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public int getAge() {
        return age;
    }

    public String getJob() {
        return job;
    }

    public boolean getisUser() { return isUser; }

    public static User getInstance() { return user; }
}
