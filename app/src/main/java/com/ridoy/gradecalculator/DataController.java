package com.ridoy.gradecalculator;

import com.ridoy.gradecalculator.ModelClass.Semester;

public class DataController {

    public static DataController Instence;

    public static DataController getInstance(){
        if (Instence==null){
            Instence=new DataController();
        }
        return Instence;
    }

    Homefragmentinterface homefragmentinterface;
    Semester currentSemester;

    public Semester getCurrentSemester() {
        return currentSemester;
    }

    public void setCurrentSemester(Semester currentSemester) {
        this.currentSemester = currentSemester;
    }

    public Homefragmentinterface getHomefragmentinterface() {
        return homefragmentinterface;
    }

    public void setHomefragmentinterface(Homefragmentinterface homefragmentinterface) {
        this.homefragmentinterface = homefragmentinterface;
    }
}
