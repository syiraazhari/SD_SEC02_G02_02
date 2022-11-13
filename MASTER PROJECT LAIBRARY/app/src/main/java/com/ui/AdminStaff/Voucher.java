package com.ui.AdminStaff;

public class Voucher {

    public String voucherCode;
    public String voucherDis;
    public String voucherNum;
    public String voucherID;

    public Voucher(){

    }

    public Voucher(String voucherCode, String voucherDis, String voucherNum, String voucherID){
        this.voucherCode =voucherCode;
        this.voucherDis = voucherDis;
        this.voucherNum = voucherNum;
        this.voucherID = voucherID;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getVoucherDis() {
        return voucherDis;
    }

    public void setVoucherDis(String voucherDis) {
        this.voucherDis = voucherDis;
    }

    public String getVoucherNum() {
        return voucherNum;
    }

    public void setVoucherNum(String voucherNum) {
        this.voucherNum = voucherNum;
    }

    public String getVoucherID() {
        return voucherID;
    }

    public void setVoucherID(String voucherID) {
        this.voucherID = voucherID;
    }
}
