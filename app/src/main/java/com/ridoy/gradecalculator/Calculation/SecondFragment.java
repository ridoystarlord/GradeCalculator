package com.ridoy.gradecalculator.Calculation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ridoy.gradecalculator.Adapter.CourseAdapter;
import com.ridoy.gradecalculator.DataController;
import com.ridoy.gradecalculator.ModelClass.Course;
import com.ridoy.gradecalculator.R;
import com.ridoy.gradecalculator.Repository.GradeRepository;

import java.util.ArrayList;
import java.util.List;

public class SecondFragment extends Fragment {

    DataController dataController;
    EditText couseName, courseCredit, courseSGPA;
    TextView CGPA;
    Button Add;
    RecyclerView courseRV;

    int totalCredit = 0;
    double productofCreditandSGPA = 0;

    GradeRepository gradeRepository;

    CourseAdapter courseAdapter;
    List<Course> courseList = new ArrayList<>();
    List<Course> deleteCourseList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        dataController = DataController.getInstance();
        gradeRepository = new GradeRepository(getActivity().getApplication());

        courseList=gradeRepository.getAllCourses(dataController.getCurrentSemester().getId());
        deleteCourseList=gradeRepository.getAllCourses(dataController.getCurrentSemester().getId());

        couseName = view.findViewById(R.id.courseName_ET);
        courseCredit = view.findViewById(R.id.courseCredit_ET);
        courseSGPA = view.findViewById(R.id.courseSGPA_ET);
        CGPA = view.findViewById(R.id.cgpa_TV);
        Add = view.findViewById(R.id.add_btn);
        courseRV = view.findViewById(R.id.courses_RV);
        courseRV.setHasFixedSize(true);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                deleteCourse(viewHolder.getAdapterPosition());

            }
        }).attachToRecyclerView(courseRV);

        courseAdapter = new CourseAdapter(getActivity(), courseList);
        courseRV.setAdapter(courseAdapter);

        if (deleteCourseList.size()>0){
            calculateCGPAList(deleteCourseList);
        }


        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (courseCredit.getText().toString().isEmpty()) {
                    courseCredit.setError("Course Credit Required");
                    courseCredit.requestFocus();
                    return;
                }
                if (courseSGPA.getText().toString().isEmpty()) {
                    courseSGPA.setError("Your Course SGPA Required");
                    courseSGPA.requestFocus();
                    return;
                }
                if (couseName.getText().toString().isEmpty()) {
                    couseName.setError("Course Name Required");
                    couseName.requestFocus();
                    return;
                }
                calculateCGPA(couseName.getText().toString(),
                        courseSGPA.getText().toString(),
                        courseCredit.getText().toString());
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.savefab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("Do you want to save Courses?")
                        .setTitle("Warning")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (courseList == null || courseList.size() == 0) {
                                    Toast.makeText(getActivity(), "Add a Course First", Toast.LENGTH_SHORT).show();
                                } else {
                                    gradeRepository.deleteallcourse(dataController.getCurrentSemester().getId());
                                    gradeRepository.insertCourseList(courseList);
                                    Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });

        return view;
    }

    private void calculateCGPAList(List<Course> CourseList) {
        for (int i=0;i<CourseList.size();i++){
            Course temp=CourseList.get(i);
            totalCredit+=temp.getCourseCredit();
            productofCreditandSGPA+=(temp.getCourseSGPA()*temp.getCourseCredit());
        }
        double cgpa = productofCreditandSGPA / totalCredit;
        CGPA.setText(String.format("CGPA: %.2f", cgpa));
    }

    private void calculateCGPA(String name, String sgpa, String credit) {

        double sgpavalue = Double.parseDouble(sgpa);
        int creditavalue = Integer.parseInt(credit);

        productofCreditandSGPA += sgpavalue * creditavalue;
        totalCredit += creditavalue;

        double cgpa = productofCreditandSGPA / totalCredit;
        CGPA.setText(String.format("CGPA: %.2f", cgpa));

        Course course = new Course(name, sgpavalue, creditavalue, dataController.getCurrentSemester().getId());
        courseList.add(course);
        deleteCourseList.add(course);
        courseAdapter.notifyDataSetChanged();
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void deleteCourse(int position){
        Course course=deleteCourseList.get(position);
        deleteCourseList.remove(course);
        gradeRepository.deleteCourse(course);
        courseAdapter.notifyDataSetChanged();

        deleteCourseList=gradeRepository.getAllCourses(dataController.getCurrentSemester().getId());
        courseList=gradeRepository.getAllCourses(dataController.getCurrentSemester().getId());
        if (deleteCourseList.size()>0){
            calculateCGPAList(deleteCourseList);
        }
    }
}