package com.example.laibraryadminstaff.ui.home;

public class RoomBookingDetail {
    public String roomfullname, roomicnumber, roomphonenumber, roomtotalstudent, roomnumber, roomrentime, roomrentdate;

    public RoomBookingDetail() {

    }

    public RoomBookingDetail(String roomfullname, String roomicnum, String roomphonenum, String roomtotalstudent, String roomnumber, String roomrentime, String roomrentdate) {
        this.roomfullname = roomfullname;
        this.roomicnumber = roomicnum;
        this.roomphonenumber = roomphonenum;
        this.roomtotalstudent = roomtotalstudent;
        this.roomnumber = roomnumber;
        this.roomrentime = roomrentime;
        this.roomrentdate = roomrentdate;
    }

    public String getRoomfullname() {
        return roomfullname;
    }

    public void setRoomfullname(String roomfullname) {
        this.roomfullname = roomfullname;
    }

    public String getRoomicnum() {
        return roomicnumber;
    }

    public void setRoomicnum(String roomicnum) {
        this.roomicnumber = roomicnum;
    }

    public String getRoomphonenum() {
        return roomphonenumber;
    }

    public void setRoomphonenum(String roomphonenum) {
        this.roomphonenumber = roomphonenum;
    }

    public String getRoomtotalstudent() {
        return roomtotalstudent;
    }

    public void setRoomtotalstudent(String roomtotalstudent) {
        this.roomtotalstudent = roomtotalstudent;
    }

    public String getRoomnumber() {
        return roomnumber;
    }

    public void setRoomnumber(String roomnumber) {
        this.roomnumber = roomnumber;
    }

    public String getRoomrentdate() {
        return roomrentdate;
    }

    public void setRoomrentime(String roomrentime) {
        this.roomfullname = roomrentime;
    }

    public String getRoomrentime() {
        return roomrentime;
    }

    public void setRoomrentdate(String roomrentdate) {
        this.roomrentdate = roomrentdate;
    }
}