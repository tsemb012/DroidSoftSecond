package com.websarva.wings.android.droidsoftsecond.model;

import android.text.TextUtils;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Group {
    private String hostUserId;
    private String hostUserName;
    private String groupName;
    private String groupIntro;
    private String groupType;
    private String activityAreaPrefecture;
    private String activityAreaCity;
    private String facilityEnvironment;
    private String learningFrequency;
    private int ageRangeMin;
    private int ageRangeMax;
    private int numberPersonMin;
    private int numberPersonMax;
    private boolean genderRestriction;
    private @ServerTimestamp Date timestamp;

    //-----Constructor

    public Group(){};

    public Group(FirebaseUser user,
                 String groupName,
                 String groupIntro,
                 String groupType,
                 String activityAreaPrefecture,
                 String activityAreaCity,
                 String facilityEnvironment,
                 String learningFrequency,
                 int ageRangeMin,
                 int ageRangeMax,
                 int numberPersonMin,
                 int numberPersonMax,
                 boolean genderRestriction) {

        this.hostUserId = user.getUid();
        this.hostUserName = user.getDisplayName();
        if (TextUtils.isEmpty(this.hostUserName)) {
            this.hostUserId = user.getEmail();}
        this.groupName = groupName;
        this.groupIntro = groupIntro;
        this.groupType = groupType;
        this.activityAreaPrefecture = activityAreaPrefecture;
        this.activityAreaCity = activityAreaCity;
        this.facilityEnvironment = facilityEnvironment;
        this.learningFrequency = learningFrequency;
        this.ageRangeMin = ageRangeMin;
        this.ageRangeMax = ageRangeMax;
        this.numberPersonMin = numberPersonMin;
        this.numberPersonMax = numberPersonMax;
        this.genderRestriction = genderRestriction;

    }

    //-----Getter

    public String getHostUserId() {
        return hostUserId;
    }

    public String getHostUserName() {
        return hostUserName;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupIntro() {
        return groupIntro;
    }

    public String getGroupType() {
        return groupType;
    }

    public String getActivityAreaPrefecture() {
        return activityAreaPrefecture;
    }

    public String getActivityAreaCity() {
        return activityAreaCity;
    }

    public String getFacilityEnvironment() {
        return facilityEnvironment;
    }

    public String getLearningFrequency() {
        return learningFrequency;
    }

    public int getAgeRangeMin() {
        return ageRangeMin;
    }

    public int getAgeRangeMax() {
        return ageRangeMax;
    }

    public int getNumberPersonMin() {
        return numberPersonMin;
    }

    public int getNumberPersonMax() {
        return numberPersonMax;
    }

    public boolean isGenderRestriction() {
        return genderRestriction;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    /*public void setHostUserId(String hostUserId) {
        this.hostUserId = hostUserId;
    }

    public void setHostUserName(String hostUserName) {
        this.hostUserName = hostUserName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupIntro(String groupIntro) {
        this.groupIntro = groupIntro;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public void setActivityAreaPrefecture(String activityAreaPrefecture) {
        this.activityAreaPrefecture = activityAreaPrefecture;
    }

    public void setActivityAreaCity(String activityAreaCity) {
        this.activityAreaCity = activityAreaCity;
    }

    public void setFacilityEnvironment(String facilityEnvironment) {
        this.facilityEnvironment = facilityEnvironment;
    }

    public void setLearningFrequency(String learningFrequency) {
        this.learningFrequency = learningFrequency;
    }

    public void setAgeRangeMin(int ageRangeMin) {
        this.ageRangeMin = ageRangeMin;
    }

    public void setAgeRangeMax(int ageRangeMax) {
        this.ageRangeMax = ageRangeMax;
    }

    public void setNumberPersonMin(int numberPersonMin) {
        this.numberPersonMin = numberPersonMin;
    }

    public void setNumberPersonMax(int numberPersonMax) {
        this.numberPersonMax = numberPersonMax;
    }

    public void setGenderRestriction(boolean genderRestriction) {
        this.genderRestriction = genderRestriction;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }*/
}
