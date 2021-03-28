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
        new InsertSemesterTask(semesterDao).execute(semester);
    }
    public void updateSemester(Semester semester) {
        new updateSemesterTask(semesterDao).execute(semester);
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

    public void deleteAllsemester(){
        new deleteSemesterTask(semesterDao).execute();
    }

    public void deleteSemester(Semester semester){
        new deleteoneSemesterTask(semesterDao).execute(semester);
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

    public void deleteallcoursebysemesterid(int semesterid){
        new deleteallcoursebysemesteridTask(courseDao).execute(semesterid);
    }

    public void deleteAllcourse(){
        new deleteAllcourseTask(courseDao).execute();
    }

    //background Class

    private static class InsertSemesterTask extends AsyncTask<Semester, Void, Void> {

        private SemesterDao dao;

        public InsertSemesterTask(SemesterDao semesterDao) {
            dao = semesterDao;
        }

        @Override
        protected Void doInBackground(Semester... semesters) {

            dao.insertSemester(semesters[0]);
            return null;
        }
    }
    private static class updateSemesterTask extends AsyncTask<Semester, Void, Void> {

        private SemesterDao dao;

        public updateSemesterTask(SemesterDao semesterDao) {
            dao = semesterDao;
        }

        @Override
        protected Void doInBackground(Semester... semesters) {

            dao.updateSemester(semesters[0]);
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

    private static  class deleteSemesterTask extends AsyncTask<Void,Void,Void>{

        SemesterDao dao;

        public deleteSemesterTask(SemesterDao semesterDao) {
            dao=semesterDao;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAllSemester();
            return null;
        }
    }
    private static class deleteallcoursebysemesteridTask extends AsyncTask<Integer,Void,Void>{

        CourseDao dao;

        public deleteallcoursebysemesteridTask(CourseDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
           dao.deleteAllCoursebysemesterId(integers[0]);
            return null;
        }
    }

    private static  class deleteAllcourseTask extends AsyncTask<Void,Void,Void>{

        CourseDao dao;

        public deleteAllcourseTask(CourseDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAllCourse();
            return null;
        }
    }

    private static class deleteoneSemesterTask extends AsyncTask<Semester,Void,Void> {

        SemesterDao dao;
        public deleteoneSemesterTask(SemesterDao semesterDao) {
            dao=semesterDao;
        }

        @Override
        protected Void doInBackground(Semester... semesters) {
            dao.deleteSemester(semesters[0]);
            return null;
        }
    }
}
