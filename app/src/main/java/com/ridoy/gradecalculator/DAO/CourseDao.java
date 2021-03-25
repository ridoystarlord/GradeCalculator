package com.ridoy.gradecalculator.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ridoy.gradecalculator.ModelClass.Course;

import java.util.List;

@Dao
public interface CourseDao {

    @Insert
    void insertCourse(Course course);

    @Delete
    void deleteCourse(Course course);

    @Update
    void updateCourse(Course course);

    @Query("SELECT * FROM Course WHERE semesterId LIKE:semesterId")
    List<Course> getCoursesBySemesterId(int semesterId);

    @Query("DELETE FROM Course")
    void deleteAllCourse();

    @Query("DELETE FROM Course WHERE semesterId LIKE:semesterId")
    void deleteAllCoursebysemesterId(int semesterId);

    @Insert
    void InsertCourseList(List<Course> courseList);
}
