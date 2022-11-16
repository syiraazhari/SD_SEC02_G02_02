package com.ui.AdminStaff;

public class BookingRoom {

    private String roomfullname, roomicnumber, roomphonenumber, roomtotalstudent, roomnumber, roomrentime, roomrentdate;

    public BookingRoom() {
    }

    public BookingRoom(String roomfullname, String roomicnumber, String roomphonenumber, String roomtotalstudent, String roomnumber, String roomrentime, String roomrentdate) {
        this.roomfullname = roomfullname;
        this.roomicnumber = roomicnumber;
        this.roomphonenumber = roomphonenumber;
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
