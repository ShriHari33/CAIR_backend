package com.example.cair.Entity;

public class Student {
    private String usn;
    private String sname;
    private String mobile;
    private String semail;

    public Student() {
        // Default constructor required by Spring Data
    }

    public Student(String usn, String sname, String mobile, String semail) {
        this.usn = usn;
        this.sname = sname;
        this.mobile = mobile;
        this.semail = semail;
    }

    public String getUsn() {
        return usn;
    }

    public void setUsn(String usn) {
        this.usn = usn;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSemail() {
        return semail;
    }

    public void setSemail(String semail) {
        this.semail = semail;
    }

    @Override
    public String toString() {
        return "Student{" +
                "usn='" + usn + '\'' +
                ", sname='" + sname + '\'' +
                ", mobile='" + mobile + '\'' +
                ", semail='" + semail + '\'' +
                '}';
    }
}
