package com.triton.voxit.model;

public class SingupData {
    private String name;
    private String age;
    private String gender;
    private String emailid;
    private String phoneno;
    private String password;
    private Boolean is_facebook;
    private Boolean is_gmail;
    private String location;
    private String country;
    private String state;
    private String referred_code;

    public String getReferred_code() {
        return referred_code;
    }

    public void setReferred_code(String referred_code) {
        this.referred_code = referred_code;
    }

    public SingupData(String name, String age, String gender, String email, String phome_no,
                      String etPassword, boolean facebook, boolean gmail, String referred_code, String location, String country, String state) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.emailid = email;
        this.phoneno = phome_no;
        this.password = etPassword;
        this.is_facebook = facebook;
        this.is_gmail = gmail;
        this.referred_code = referred_code;
        this.location = location;
        this.country = country;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIs_facebook() {
        return is_facebook;
    }

    public void setIs_facebook(Boolean is_facebook) {
        this.is_facebook = is_facebook;
    }

    public Boolean getIs_gmail() {
        return is_gmail;
    }

    public void setIs_gmail(Boolean is_gmail) {
        this.is_gmail = is_gmail;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
