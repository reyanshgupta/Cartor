package com.example.cartor;

public class HelperClass {
    String name, username, email, password;
    Integer credits, treeplanted, points, carbonemitted;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getTreePlanted(){
        return treeplanted;
    }

    public void setTreeplanted(int treeplanted){
        this.treeplanted = treeplanted;
    }

    public int getPoints(int points){
        return points;
    }

    public void setPoints(int points){
        this.points = points;
    }

    public int getCarbonEmitted(int carbonemitted){
        return carbonemitted;
    }

    public void setCarbonEmitted(int carbonemitted){
        this.carbonemitted = carbonemitted;
    }


    public HelperClass(String name, String username, String email, String password, Integer credits, Integer treeplanted, Integer points, Integer carbonemitted) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.credits = credits;
        this.treeplanted = treeplanted;
        this.points = points;
    }

    public HelperClass() {
    }
}
