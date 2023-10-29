package com.example.admin_app;

public class Students {
    String email;
    String pass;

    public Students(String email, String pass) {
        this.email = email;
        this.pass=pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
