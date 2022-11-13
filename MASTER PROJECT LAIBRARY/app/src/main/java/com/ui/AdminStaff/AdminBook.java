package com.ui.AdminStaff;

public class AdminBook {

    public String adminBookName;
    public String adminbookPrice;
    public String adminBookDescription;
    public String adminBookID;

    public AdminBook(){

    }

    public AdminBook(String adminBookName, String adminbookPrice, String adminBookDescription, String adminBookID) {
        this.adminBookName = adminBookName;
        this.adminbookPrice = adminbookPrice;
        this.adminBookDescription = adminBookDescription;
        this.adminBookID = adminBookID;
    }


    public String getAdminBookName() {
        return adminBookName;
    }

    public void setAdminBookName(String adminBookName) {
        this.adminBookName = adminBookName;
    }

    public String getAdminbookPrice() {
        return adminbookPrice;
    }

    public void setAdminbookPrice(String adminbookPrice) {
        this.adminbookPrice = adminbookPrice;
    }

    public String getAdminBookDescription() {
        return adminBookDescription;
    }

    public void setAdminBookDescription(String adminBookDescription) {
        this.adminBookDescription = adminBookDescription;
    }

    public String getAdminBookID() {
        return adminBookID;
    }

    public void setAdminBookID(String adminBookID) {
        this.adminBookID = adminBookID;
    }
}
