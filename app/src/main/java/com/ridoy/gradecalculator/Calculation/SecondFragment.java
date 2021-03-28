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
import com.ridoy.gradecalculator.ModelClass.Semester;
import com.ridoy.gradecalculator.R;
import com.ridoy.gradecalculator.Repository.GradeRepository;

import java.util.ArrayList;
import java.util.List;

public class SecondFragment extends Fragment {

    DataController dataController;
    TextView CGPA;
    RecyclerView courseRV;

    int totalCredit = 0;
    double productofCreditandSGPA = 0.0;

    GradeRepository gradeRepository;
    CourseAdapter courseAdapter;
    List<Course> courseList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        CGPA = view.findViewById(R.id.cgpa_TV);
        courseRV = view.findViewById(R.id.courses_RV);

        dataController = DataController.getInstance();
        gradeRepository = new GradeRepository(getActivity().getApplication());

        courseList = gradeRepository.getAllCourses(dataController.getCurrentSemester().getId());

        if (courseList.size() > 0) {
            calculateCGPAList(courseList);
        } else {
            Semester updatesemester = new Semester();
            updatesemester.setId(dataController.getCurrentSemester().getId());
            updatesemester.setSemesterName(dataController.getCurrentSemester().getSemesterName());
            updatesemester.setSemesterCredit(dataController.getCurrentSemester().getSemesterCredit());
            updatesemester.setSemesterCGPA(0.0);
            gradeRepository.updateSemester(updatesemester);
            CGPA.setText("CGPA: 0.0");
        }

        courseRV.setHasFixedSize(true);
        courseAdapter = new CourseAdapter(getActivity(), courseList);
        courseRV.setAdapter(courseAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                deleteCourse(viewHolder.getAdapterPosition());

            }
        }).attachToRecyclerView(courseRV);

        FloatingActionButton addfab = view.findViewById(R.id.addcoursefab);
        addfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.course_user_input_dialog);

                EditText couseName = dialog.findViewById(R.id.courseName_ET);
                EditText courseCredit = dialog.findViewById(R.id.courseCredit_ET);
                EditText courseSGPA = dialog.findViewById(R.id.courseSGPA_ET);
                Button Add_btn = dialog.findViewById(R.id.add_btn);

                Add_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (courseCredit.getText().toString().isEmpty()) {
                            courseCredit.setError("Course Credit Required");
                            courseCredit.requestFocus();
                            return;
                        }
                        if (courseSGPA.getText().toString().isEmpty()) {
                            courseSGPA.setError("Course SGPA Required");
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
                        dialog.dismiss();
                    }
                });
                dialog.show();
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
                                    insertCourseList(courseList);
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

    private void insertCourseList(List<Course> CourseList) {
        gradeRepository.deleteallcoursebysemesterid(dataController.getCurrentSemester().getId());
        calculateCGPAList(CourseList);
        gradeRepository.insertCourseList(CourseList);
        Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }

    private void calculateCGPAList(List<Course> list) {
        for (int i = 0; i < list.size(); i++) {
            Course temp = list.get(i);
            totalCredit += temp.getCourseCredit();
            productofCreditandSGPA += (temp.getCourseSGPA() * temp.getCourseCredit());
        }
        double cgpa = productofCreditandSGPA / totalCredit;
        CGPA.setText(String.format("CGPA: %.2f", cgpa));

        Semester updatesemester = new Semester();
        updatesemester.setId(dataController.getCurrentSemester().getId());
        updatesemester.setSemesterName(dataController.getCurrentSemester().getSemesterName());
        updatesemester.setSemesterCredit(dataController.getCurrentSemester().getSemesterCredit());
        updatesemester.setSemesterCGPA(cgpa);
        gradeRepository.updateSemester(updatesemester);

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
        courseAdapter.notifyDataSetChanged();
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void deleteCourse(int position) {

        Course course = courseList.get(position);
        courseList.remove(course);
        courseAdapter.notifyDataSetChanged();
        gradeRepository.deleteallcoursebysemesterid(dataController.getCurrentSemester().getId());
        gradeRepository.insertCourseList(courseList);
        totalCredit = 0;
        productofCreditandSGPA = 0.0;

        if (courseList.size() <= 0) {
            Semester updatesemester = new Semester();
            updatesemester.setId(dataController.getCurrentSemester().getId());
            updatesemester.setSemesterName(dataController.getCurrentSemester().getSemesterName());
            updatesemester.setSemesterCredit(dataController.getCurrentSemester().getSemesterCredit());
            updatesemester.setSemesterCGPA(0.0);
            gradeRepository.updateSemester(updatesemester);
            CGPA.setText("CGPA: 0.0");
        } else {
            calculateCGPAList(courseList);
        }

    }
}