package com.ui.AdminStaff;

public class DownModel {

    public String name, link,id;

    public DownModel(){

    }

    public DownModel(String name, String link, String id) {
        this.name = name ;
        this.link = link;
        this.id = id;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
