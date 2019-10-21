package com.triton.voxit.responsepojo;

import java.util.List;

public class UpdateJockeyProfilePicResponse {


    /**
     * data : {"jockey_id":373,"jockey_type":"silver","image_path":"http://tritontutebox.com/voxit/image_upload/User_Image.png","name":"ahalya ","dob":"","age":"","gender":"","emailid":"ahalyatriton@gmail.com","phoneno":"1234567899","password":"1234","is_gmail":false,"is_facebook":false,"jockey_unique_id":null,"user_mode":"JK","language":[287,288],"genre":[83,84,82,86,80],"created_on":"2019-08-20,1:18:04,PM","updated_on":"10/17/2019, 1:37:17 PM","isLoggedIn":null,"isLoginStatus":true,"lastLogin":"10/17/2019, 1:27:29 PM","country":"","state":"","termsandconditions":true,"city":"","display_name":"ahalya","referral_code":"aha79633","referred_code":"tes6677","firebase_token":"d5s3iHx6DEo:APA91bHE5GKtnJ1XbXXtHCI0MkNVYEkBT9YKu3xdH9DVNXH3rJf6Pnk0O-ygydYdhO8Nol5KoSgCcajpEoocExHcLTSAVYp1c1ijMn_gCQVzXHpvLH9rfPLWxJdSQVcs0q9vxOjSuA-6","latitude":13.0546439,"longitude":80.1998631,"appVersionName":"2.4","isAppliedForJockey":false,"isDeleted":false,"isDisabled":false}
     * status : Success
     * code : 200
     */

    private DataBean data;
    private String status;
    private int code;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class DataBean {
        /**
         * jockey_id : 373
         * jockey_type : silver
         * image_path : http://tritontutebox.com/voxit/image_upload/User_Image.png
         * name : ahalya
         * dob :
         * age :
         * gender :
         * emailid : ahalyatriton@gmail.com
         * phoneno : 1234567899
         * password : 1234
         * is_gmail : false
         * is_facebook : false
         * jockey_unique_id : null
         * user_mode : JK
         * language : [287,288]
         * genre : [83,84,82,86,80]
         * created_on : 2019-08-20,1:18:04,PM
         * updated_on : 10/17/2019, 1:37:17 PM
         * isLoggedIn : null
         * isLoginStatus : true
         * lastLogin : 10/17/2019, 1:27:29 PM
         * country :
         * state :
         * termsandconditions : true
         * city :
         * display_name : ahalya
         * referral_code : aha79633
         * referred_code : tes6677
         * firebase_token : d5s3iHx6DEo:APA91bHE5GKtnJ1XbXXtHCI0MkNVYEkBT9YKu3xdH9DVNXH3rJf6Pnk0O-ygydYdhO8Nol5KoSgCcajpEoocExHcLTSAVYp1c1ijMn_gCQVzXHpvLH9rfPLWxJdSQVcs0q9vxOjSuA-6
         * latitude : 13.0546439
         * longitude : 80.1998631
         * appVersionName : 2.4
         * isAppliedForJockey : false
         * isDeleted : false
         * isDisabled : false
         */

        private int jockey_id;
        private String jockey_type;
        private String image_path;
        private String name;
        private String dob;
        private String age;
        private String gender;
        private String emailid;
        private String phoneno;
        private String password;
        private boolean is_gmail;
        private boolean is_facebook;
        private Object jockey_unique_id;
        private String user_mode;
        private String created_on;
        private String updated_on;
        private Object isLoggedIn;
        private boolean isLoginStatus;
        private String lastLogin;
        private String country;
        private String state;
        private boolean termsandconditions;
        private String city;
        private String display_name;
        private String referral_code;
        private String referred_code;
        private String firebase_token;
        private double latitude;
        private double longitude;
        private String appVersionName;
        private boolean isAppliedForJockey;
        private boolean isDeleted;
        private boolean isDisabled;
        private List<Integer> language;
        private List<Integer> genre;

        public int getJockey_id() {
            return jockey_id;
        }

        public void setJockey_id(int jockey_id) {
            this.jockey_id = jockey_id;
        }

        public String getJockey_type() {
            return jockey_type;
        }

        public void setJockey_type(String jockey_type) {
            this.jockey_type = jockey_type;
        }

        public String getImage_path() {
            return image_path;
        }

        public void setImage_path(String image_path) {
            this.image_path = image_path;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
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

        public boolean isIs_gmail() {
            return is_gmail;
        }

        public void setIs_gmail(boolean is_gmail) {
            this.is_gmail = is_gmail;
        }

        public boolean isIs_facebook() {
            return is_facebook;
        }

        public void setIs_facebook(boolean is_facebook) {
            this.is_facebook = is_facebook;
        }

        public Object getJockey_unique_id() {
            return jockey_unique_id;
        }

        public void setJockey_unique_id(Object jockey_unique_id) {
            this.jockey_unique_id = jockey_unique_id;
        }

        public String getUser_mode() {
            return user_mode;
        }

        public void setUser_mode(String user_mode) {
            this.user_mode = user_mode;
        }

        public String getCreated_on() {
            return created_on;
        }

        public void setCreated_on(String created_on) {
            this.created_on = created_on;
        }

        public String getUpdated_on() {
            return updated_on;
        }

        public void setUpdated_on(String updated_on) {
            this.updated_on = updated_on;
        }

        public Object getIsLoggedIn() {
            return isLoggedIn;
        }

        public void setIsLoggedIn(Object isLoggedIn) {
            this.isLoggedIn = isLoggedIn;
        }

        public boolean isIsLoginStatus() {
            return isLoginStatus;
        }

        public void setIsLoginStatus(boolean isLoginStatus) {
            this.isLoginStatus = isLoginStatus;
        }

        public String getLastLogin() {
            return lastLogin;
        }

        public void setLastLogin(String lastLogin) {
            this.lastLogin = lastLogin;
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

        public boolean isTermsandconditions() {
            return termsandconditions;
        }

        public void setTermsandconditions(boolean termsandconditions) {
            this.termsandconditions = termsandconditions;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDisplay_name() {
            return display_name;
        }

        public void setDisplay_name(String display_name) {
            this.display_name = display_name;
        }

        public String getReferral_code() {
            return referral_code;
        }

        public void setReferral_code(String referral_code) {
            this.referral_code = referral_code;
        }

        public String getReferred_code() {
            return referred_code;
        }

        public void setReferred_code(String referred_code) {
            this.referred_code = referred_code;
        }

        public String getFirebase_token() {
            return firebase_token;
        }

        public void setFirebase_token(String firebase_token) {
            this.firebase_token = firebase_token;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public String getAppVersionName() {
            return appVersionName;
        }

        public void setAppVersionName(String appVersionName) {
            this.appVersionName = appVersionName;
        }

        public boolean isIsAppliedForJockey() {
            return isAppliedForJockey;
        }

        public void setIsAppliedForJockey(boolean isAppliedForJockey) {
            this.isAppliedForJockey = isAppliedForJockey;
        }

        public boolean isIsDeleted() {
            return isDeleted;
        }

        public void setIsDeleted(boolean isDeleted) {
            this.isDeleted = isDeleted;
        }

        public boolean isIsDisabled() {
            return isDisabled;
        }

        public void setIsDisabled(boolean isDisabled) {
            this.isDisabled = isDisabled;
        }

        public List<Integer> getLanguage() {
            return language;
        }

        public void setLanguage(List<Integer> language) {
            this.language = language;
        }

        public List<Integer> getGenre() {
            return genre;
        }

        public void setGenre(List<Integer> genre) {
            this.genre = genre;
        }
    }
}
