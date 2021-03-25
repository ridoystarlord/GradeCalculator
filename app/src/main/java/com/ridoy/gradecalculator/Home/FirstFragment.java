package com.ridoy.gradecalculator.Home;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.ridoy.gradecalculator.Adapter.SemesterAdapter;
import com.ridoy.gradecalculator.Calculation.SecondFragment;
import com.ridoy.gradecalculator.DataController;
import com.ridoy.gradecalculator.Homefragmentinterface;
import com.ridoy.gradecalculator.ModelClass.Semester;
import com.ridoy.gradecalculator.R;
import com.ridoy.gradecalculator.Repository.GradeRepository;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment implements Homefragmentinterface {

    GradeRepository gradeRepository;
    SemesterAdapter semesterAdapter;
    RecyclerView semesterRV;
    List<Semester> semesterList=new ArrayList<>();
    DataController controller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        gradeRepository = new GradeRepository(getActivity().getApplication());
        semesterList=gradeRepository.getAllSemesters();

        semesterAdapter=new SemesterAdapter(getActivity(),semesterList);
        semesterRV=view.findViewById(R.id.semester_RV);
        semesterRV.setHasFixedSize(true);
        semesterRV.setAdapter(semesterAdapter);

        controller=DataController.getInstance();
        controller.setHomefragmentinterface(this);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.custom_user_input_dialog);

                EditText semesterName_ET = dialog.findViewById(R.id.semesterName_ET);
                Button createSemester_btn = dialog.findViewById(R.id.createSemester_btn);

                createSemester_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (semesterName_ET.getText().toString().isEmpty()) {
                            semesterName_ET.setError("Please Enter Semester Name");
                            semesterName_ET.requestFocus();
                            return;
                        } else {
                            insertSemester(semesterName_ET.getText().toString());
                            dialog.dismiss();
                        }
                    }
                });

                dialog.show();
            }
        });

        return view;
    }

    private void insertSemester(String name) {
        Semester semester = new Semester(name, 0);
        semesterList.add(semester);
        semesterAdapter.notifyDataSetChanged();
        gradeRepository.insertSemester(semester);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSemesterItemClicked(Semester semester) {
        controller.setCurrentSemester(semester);
        NavHostFragment.findNavController(FirstFragment.this)
                .navigate(R.id.action_FirstFragment_to_SecondFragment);
    }
}