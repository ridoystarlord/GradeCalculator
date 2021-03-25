package com.ridoy.gradecalculator.ModelClass;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;
@Entity
public class Semester {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String semesterName;
    public int semesterCredit;

    public Semester(String semesterName, int semesterCredit) {
        this.semesterName = semesterName;
        this.semesterCredit = semesterCredit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSemesterName() {
        return semesterName;
    }

    public void setSemesterName(String semesterName) {
        this.semesterName = semesterName;
    }

    public int getSemesterCredit() {
        return semesterCredit;
    }

    public void setSemesterCredit(int semesterCredit) {
        this.semesterCredit = semesterCredit;
    }
}

