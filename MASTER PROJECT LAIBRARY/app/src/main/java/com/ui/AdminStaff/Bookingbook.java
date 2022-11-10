package com.ui.AdminStaff;

public class Bookingbook {

    private String bookingID;
    public String fullname,icnumber,phonenumber,namebook,quantity,rentdate,total, rentday, rentmonth, rentyear;

    public Bookingbook() {
    }

    public Bookingbook(String bookingID, String fullname, String icnumber, String phonenumber, String namebook, String quantity, String rentdate, String total, String rentday, String rentmonth, String rentyear) {
        this.bookingID = bookingID;
        this.fullname = fullname;
        this.icnumber = icnumber;
        this.phonenumber = phonenumber;
        this.namebook = namebook;
        this.quantity = quantity;
        this.rentdate = rentdate;
        this.total = total;
        this.rentday = rentday;
        this.rentmonth = rentmonth;
        this.rentyear = rentyear;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getIcnumber() {
        return icnumber;
    }

    public void setIcnumber(String icnumber) {
        this.icnumber = icnumber;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getNamebook() {
        return namebook;
    }

    public void setNamebook(String namebook) {
        this.namebook = namebook;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRentdate() {
        return rentdate;
    }

    public void setRentdate(String rentdate) {
        this.rentdate = rentdate;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getRentday() {
        return rentday;
    }

    public void setRentday(String rentday) {
        this.rentday = rentday;
    }

    public String getRentmonth() {
        return rentmonth;
    }

    public void setRentmonth(String rentmonth) {
        this.rentmonth = rentmonth;
    }

    public String getRentyear() {
        return rentyear;
    }

    public void setRentyear(String rentyear) {
        this.rentyear = rentyear;
    }
}
