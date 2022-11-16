package com.ui.home;

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

    public String getRoomicnumber() {
        return roomicnumber;
    }

    public void setRoomicnumber(String roomicnumber) {
        this.roomicnumber = roomicnumber;
    }

    public String getRoomphonenumber() {
        return roomphonenumber;
    }

    public void setRoomphonenumber(String roomphonenumber) {
        this.roomphonenumber = roomphonenumber;
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

    public String getRoomrentime() {
        return roomrentime;
    }

    public void setRoomrentime(String roomrentime) {
        this.roomrentime = roomrentime;
    }

    public String getRoomrentdate() {
        return roomrentdate;
    }

    public void setRoomrentdate(String roomrentdate) {
        this.roomrentdate = roomrentdate;
    }
}