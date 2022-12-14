package com.ui.profile;

public class UserProfile {
    public String userName;
    public String userAge;
    public String userEmail;
    public String userRole;
    public String userId;
    public String userStudent;

    public UserProfile(){

    }

    public UserProfile(String userName, String userAge, String userEmail, String userRole, String userId, String userStudent) {
        this.userName = userName;
        this.userAge = userAge;
        this.userEmail = userEmail;
        this.userRole = userRole;
        this.userId = userId;
        this.userStudent = userStudent;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserStudent() {
        return userStudent;
    }

    public void setUserStudent(String userStudent) {
        this.userStudent = userStudent;
    }
}
