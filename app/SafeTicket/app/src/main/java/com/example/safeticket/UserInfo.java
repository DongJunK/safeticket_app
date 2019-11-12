package com.example.safeticket;

public class UserInfo {
    private String infoType;
    private String issueNumber;
    private String issuer;
    private String issueDate;

    UserInfo() {}
    UserInfo(String infoType, String issueNumber, String issuer, String issueDate)
    {
        this.infoType = infoType;
        this.issueNumber = issueNumber;
        this.issuer = issuer;
        this.issueDate = issueDate;
    }

    public String getInfoType() { return this.infoType; }
    public String getIssueNumber() { return this.issueNumber; }
    public String getIssuer() { return this.issuer; }
    public String getIssueDate() { return this.issueDate; }
}
