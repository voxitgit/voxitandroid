package com.triton.voxit.model;

public class LoginFacebookUsersResponse {

    /**
     * status : true
     * message : Authentication Success
     * accessid : 239
     * user : {"id":9,"Name":"thirumeni","CountryCode":"91","MobileNumber":"9688542626","Email":"thirumeni.mca@gmail.com","Password":"QSivl.u5","isGoogleLoggedIn":1,"isFacebookLogin":1,"updated_at":"2019-03-08 15:41:50","created_at":"2019-03-08 15:41:50"}
     */

    private boolean status;
    private String message;
    private int accessid;
    private UserBean user;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getAccessid() {
        return accessid;
    }

    public void setAccessid(int accessid) {
        this.accessid = accessid;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean {
        /**
         * id : 9
         * Name : thirumeni
         * CountryCode : 91
         * MobileNumber : 9688542626
         * Email : thirumeni.mca@gmail.com
         * Password : QSivl.u5
         * isGoogleLoggedIn : 1
         * isFacebookLogin : 1
         * updated_at : 2019-03-08 15:41:50
         * created_at : 2019-03-08 15:41:50
         */

        private int id;
        private String Name;
        private String CountryCode;
        private String MobileNumber;
        private String Email;
        private String Password;
        private int isGoogleLoggedIn;
        private int isFacebookLogin;
        private String updated_at;
        private String created_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getCountryCode() {
            return CountryCode;
        }

        public void setCountryCode(String CountryCode) {
            this.CountryCode = CountryCode;
        }

        public String getMobileNumber() {
            return MobileNumber;
        }

        public void setMobileNumber(String MobileNumber) {
            this.MobileNumber = MobileNumber;
        }

        public String getEmail() {
            return Email;
        }

        public void setEmail(String Email) {
            this.Email = Email;
        }

        public String getPassword() {
            return Password;
        }

        public void setPassword(String Password) {
            this.Password = Password;
        }

        public int getIsGoogleLoggedIn() {
            return isGoogleLoggedIn;
        }

        public void setIsGoogleLoggedIn(int isGoogleLoggedIn) {
            this.isGoogleLoggedIn = isGoogleLoggedIn;
        }

        public int getIsFacebookLogin() {
            return isFacebookLogin;
        }

        public void setIsFacebookLogin(int isFacebookLogin) {
            this.isFacebookLogin = isFacebookLogin;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }
}
