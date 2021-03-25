package com.ridoy.gradecalculator.Repository;

import android.app.Application;
import android.os.AsyncTask;

import com.ridoy.gradecalculator.DAO.CourseDao;
import com.ridoy.gradecalculator.DAO.SemesterDao;
import com.ridoy.gradecalculator.Database.GradeDatabase;
import com.ridoy.gradecalculator.ModelClass.Course;
import com.ridoy.gradecalculator.ModelClass.Semester;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GradeRepository {

    private CourseDao courseDao;
    private SemesterDao semesterDao;
    List<Semester> semesterList = new ArrayList<>();
    List<Course> courseList = new ArrayList<>();

    public GradeRepository(Application application) {
        GradeDatabase database = GradeDatabase.getDatabase(application);
        courseDao = database.courseDao();
        semesterDao = database.semesterDao();
    }

    public void insertSemester(Semester semester) {

        new InsertTask(semesterDao).execute(semester);
    }

    public void insertCourseList(List<Course> courseList) {
        new courseListTask(courseDao).execute(courseList);
    }

    public List<Semester> getAllSemesters() {
        try {
            semesterList = new GetAllSemesterTask(semesterDao).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return semesterList;
    }

    public void deleteCourse(Course course){
        new deleteCourseTask(courseDao).execute(course);
    }

    public List<Course> getAllCourses(int semesterid) {
        try {
            courseList = new GetAllCourseTask(courseDao).execute(semesterid).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return courseList;
    }

    public void deleteallcourse(int semesterid){
        new deleteallcourseTask(courseDao).execute(semesterid);
    }

    //background Class

    private static class InsertTask extends AsyncTask<Semester, Void, Void> {

        private SemesterDao dao;

        public InsertTask(SemesterDao semesterDao) {
            dao = semesterDao;
        }

        @Override
        protected Void doInBackground(Semester... semesters) {

            dao.insertSemester(semesters[0]);

            return null;
        }
    }

    private static class GetAllSemesterTask extends AsyncTask<Void, Void, List<Semester>> {

        SemesterDao dao;

        public GetAllSemesterTask(SemesterDao semesterDao) {
            dao = semesterDao;
        }

        @Override
        protected List<Semester> doInBackground(Void... voids) {
            return dao.getAllSemester();
        }
    }

    private static class courseListTask extends AsyncTask<List<Course>, Void, Void> {
        CourseDao dao;

        public courseListTask(CourseDao courseDao) {
            dao = courseDao;
        }

        @Override
        protected Void doInBackground(List<Course>... lists) {
            dao.InsertCourseList(lists[0]);
            return null;
        }
    }

    private static class GetAllCourseTask extends AsyncTask<Integer, Void, List<Course>> {
        CourseDao dao;

        public GetAllCourseTask(CourseDao courseDao) {
            dao = courseDao;
        }
        @Override
        protected List<Course> doInBackground(Integer... integers) {
            return dao.getCoursesBySemesterId(integers[0]);
        }
    }

    private static  class deleteCourseTask extends AsyncTask<Course,Void,Void>{

        CourseDao dao;

        public deleteCourseTask(CourseDao courseDao) {
            dao=courseDao;
        }

        @Override
        protected Void doInBackground(Course... courses) {
            dao.deleteCourse(courses[0]);
            return null;
        }
    }
    private static class deleteallcourseTask extends AsyncTask<Integer,Void,Void>{

        CourseDao dao;

        public deleteallcourseTask(CourseDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
           dao.deleteAllCoursebysemesterId(integers[0]);
            return null;
        }
    }
}
