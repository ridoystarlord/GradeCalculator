package com.ridoy.gradecalculator.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ridoy.gradecalculator.ModelClass.Semester;

import java.util.List;

@Dao
public interface SemesterDao {

    @Insert
    void insertSemester(Semester semester);

    @Delete
    void deleteSemester(Semester semester);

    @Update
    void updateSemester(Semester semester);

    @Query("SELECT * FROM Semester ORDER BY id ASC")
    List<Semester> getAllSemester();

    @Query("DELETE FROM Semester")
    void deleteAllSemester();

}
