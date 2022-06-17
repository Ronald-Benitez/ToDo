package com.example.login.models;

public class user {
    private String uid;
    private String proyectos;

    public user(){

    }

    public user(String uid,String proyectos){
        this.uid = uid;
        this.proyectos = proyectos;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProyectos() {
        return proyectos;
    }

    public void setProyectos(String proyectos) {
        this.proyectos = proyectos;
    }
}
