package com.example.eventorganiser;

public class UserDetails {
    private String username;
    private String groupName;
    private String emailId;
    private String enrollNo;
    private String mobileNo;
    private String userType;

    public UserDetails(String username, String emailId, String enrollNo, String mobileNo,String userType) {
        this.username = username;
        this.emailId = emailId;
        this.enrollNo = enrollNo;
        this.mobileNo = mobileNo;
        this.userType = userType;
    }

    public UserDetails() {
    }

    public UserDetails(String username, String groupName, String emailId,String userType) {
        this.username = username;
        this.groupName = groupName;
        this.emailId = emailId;
        this.userType = userType;
    }

    public String getEnrollNo() {
        return enrollNo;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setEnrollNo(String enrollNo) {
        this.enrollNo = enrollNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
