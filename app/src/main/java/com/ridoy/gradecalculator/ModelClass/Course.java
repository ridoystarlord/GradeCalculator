package com.ridoy.gradecalculator.ModelClass;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Course {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String courseName;
    public Double courseSGPA;
    public int courseCredit;
    public int semesterId;

    public Course(String courseName, Double courseSGPA, int courseCredit, int semesterId) {
        this.courseName = courseName;
        this.courseSGPA = courseSGPA;
        this.courseCredit = courseCredit;
        this.semesterId = semesterId;
    }

    public int getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(int semesterId) {
        this.semesterId = semesterId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Double getCourseSGPA() {
        return courseSGPA;
    }

    public void setCourseSGPA(Double courseSGPA) {
        this.courseSGPA = courseSGPA;
    }

    public int getCourseCredit() {
        return courseCredit;
    }

    public void setCourseCredit(int courseCredit) {
        this.courseCredit = courseCredit;
    }
}
