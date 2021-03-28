package com.ridoy.gradecalculator.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ridoy.gradecalculator.DAO.CourseDao;
import com.ridoy.gradecalculator.DAO.SemesterDao;
import com.ridoy.gradecalculator.ModelClass.Course;
import com.ridoy.gradecalculator.ModelClass.Semester;
@Database(entities = {Course.class,Semester.class}, version = 3, exportSchema = false)
public abstract class GradeDatabase extends RoomDatabase {

    public abstract CourseDao courseDao();
    public abstract SemesterDao semesterDao();
    public static volatile GradeDatabase INSTANCE;

    public static GradeDatabase getDatabase(Context context){
        if (INSTANCE==null){
            INSTANCE= Room.databaseBuilder(context.getApplicationContext(),
                    GradeDatabase.class,
                    "GRADEDATABASE")
                    .fallbackToDestructiveMigration().build();

        }
        return INSTANCE;
    }
}
