package com.example.laibraryadminstaff.ui.home;

public class BookingForm extends bookingActivity{

    public String userFullName;
    public String userIcNum;
    public String userPhoneNum;
    public String userTotalstudent;

    public BookingForm(String userFullName, String userIcNum, String userPhoneNum, String userTotalstudent){
        this.userFullName = userFullName;
        this.userIcNum = userIcNum;
        this.userPhoneNum = userPhoneNum;
        this.userTotalstudent = userTotalstudent;
    }

    public String getUserFullName() {return userFullName;}

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserIcNum() {return userIcNum;}

    public void setUserIcNum(String userIcNum) {
        this.userIcNum = userIcNum;
    }

    public String getUserPhoneNum(String userPhoneNum) {return userIcNum;}

    public void setUserPhoneNum(String userPhoneNum) { this.userPhoneNum = userPhoneNum; }

    public String getUserTotalstudent() { return userTotalstudent; }

    public void setUserTotalstudent(String userTotalstudent) { this.userTotalstudent = userTotalstudent; }



}
