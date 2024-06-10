package com.example.cair.Entity;

import java.util.List;

public class Project {
    private String projectType;
    private String selectedSemester;
    private String pname;
    private String year;
    private String domain;
    private List<Student> students; // Array of students
    // File related fields
    private List<FileUpload> file; // List of file names associated with the project
    private Boolean published; // Boolean to indicate if the project is published or not
    private String publicationSite; // Description of the project


    public Project() {
        // Default constructor required by Spring Data
    }

    public Project(String projectType, String selectedSemester, String pname, String year, String domain,  List<Student> students,List<FileUpload> file) {
        this.projectType = projectType;
        this.selectedSemester = selectedSemester;
        this.pname = pname;
        this.year = year;
        this.domain = domain;
        this.students = students;
        this.file=file;

        this.published = false;
        this.publicationSite = "";
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public String getPublicationSite() {
        return publicationSite;
    }

    public void setPublicationSite(String publicationSite) {
        this.publicationSite = publicationSite;
    }


    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getSelectedSemester() {
        return selectedSemester;
    }

    public void setSelectedSemester(String selectedSemester) {
        this.selectedSemester = selectedSemester;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<FileUpload> getFile() {
        return file;
    }

    public void setFile(List<FileUpload> file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "Project{" +
                "projectType='" + projectType + '\'' +
                ", selectedSemester='" + selectedSemester + '\'' +
                ", pname='" + pname + '\'' +
                ", year='" + year + '\'' +
                ", domain='" + domain + '\'' +
                ", students=" + students +
                ", file=" + file +
                '}';
    }
}
