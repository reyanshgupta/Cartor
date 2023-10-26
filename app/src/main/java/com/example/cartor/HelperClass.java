package com.example.cartor;

public class HelperClass {
    String name, username, email, password;
    Integer credits, treeplanted, points, carbonemitted, mailemission, socialemission, callemission;

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

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public Integer getTreePlanted() {
        return treeplanted;
    }

    public void setTreePlanted(Integer treeplanted) {
        this.treeplanted = treeplanted;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getCarbonEmitted() {
        return carbonemitted;
    }

    public void setCarbonEmitted(Integer carbonemitted) {
        this.carbonemitted = carbonemitted;
    }

    public Integer getMailEmission() {
        return mailemission;
    }

    public void setMailEmission(Integer mailemission) {
        this.mailemission = mailemission;
    }

    public Integer getCallEmission() {
        return callemission;
    }

    public void setCallEmission(Integer callemission) {
        this.callemission = callemission;
    }

    public Integer getSocialEmission() {
        return socialemission;
    }

    public void setSocialEmission(Integer socialemission) {
        this.socialemission = socialemission;
    }

    public HelperClass(String name, String username, String email, String password, Integer credits, Integer treeplanted, Integer points, Integer carbonemitted, Integer mailemission, Integer callemission, Integer socialemission) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.credits = credits;
        this.treeplanted = treeplanted;
        this.points = points;
        this.carbonemitted = carbonemitted;
        this.mailemission = mailemission;
        this.callemission = callemission;
        this.socialemission = socialemission;
    }

    public HelperClass() {
    }
}
