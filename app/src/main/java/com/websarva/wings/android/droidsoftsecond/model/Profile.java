package com.websarva.wings.android.droidsoftsecond.model;

public class Profile {
    private String userId;
    private String profilePhotoPath;
    private String backgroundPhotoPath;
    private String userName;
    private String comment;
    private String gender;
    private int age;
    private String residentialArea;


    public Profile(){}
    public Profile(String userId, String profilePhotoPath, String userName, String comment, String gender, int age, String residentialArea) {
        this.userId = userId;
        this.profilePhotoPath = profilePhotoPath;
        this.userName = userName;
        this.comment = comment;
        this.gender = gender;
        this.age = age;
        this.residentialArea = residentialArea;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    

    public String getProfilePhotoPath() {
        return profilePhotoPath;
    }

    public void setProfilePhotoPath(String profilePhotoPath) {
        this.profilePhotoPath = profilePhotoPath;
    }

    public String getBackgroundPhotoPath() {
        return backgroundPhotoPath;
    }

    public void setBackgroundPhotoPath(String backgroundPhotoPath) {
        this.backgroundPhotoPath = backgroundPhotoPath;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getResidentialArea() {
        return residentialArea;
    }

    public void setResidentialArea(String residentialArea) {
        this.residentialArea = residentialArea;
    }

}

