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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ridoy.gradecalculator.Adapter.SemesterAdapter;
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
    List<Semester> semesterList = new ArrayList<>();
    DataController controller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        gradeRepository = new GradeRepository(getActivity().getApplication());
        semesterList = gradeRepository.getAllSemesters();

        semesterAdapter = new SemesterAdapter(getActivity(), semesterList);
        semesterRV = view.findViewById(R.id.semester_RV);
        semesterRV.setHasFixedSize(true);
        semesterRV.setAdapter(semesterAdapter);

        controller = DataController.getInstance();
        controller.setHomefragmentinterface(this);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                deleteSemester(viewHolder.getAdapterPosition());

            }
        }).attachToRecyclerView(semesterRV);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.semester_user_input_dialog);

                EditText semesterName_ET = dialog.findViewById(R.id.semesterName_ET);
                EditText semesterCredit_ET = dialog.findViewById(R.id.semesterCredit_ET);
                Button createSemester_btn = dialog.findViewById(R.id.createSemester_btn);

                createSemester_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (semesterName_ET.getText().toString().isEmpty()) {
                            semesterName_ET.setError("Please Enter Semester Name");
                            semesterName_ET.requestFocus();
                            return;
                        }
                        if (semesterCredit_ET.getText().toString().isEmpty()) {
                            semesterCredit_ET.setError("Please Enter Semester Total Credit");
                            semesterCredit_ET.requestFocus();
                            return;
                        }
                        insertSemester(semesterName_ET.getText().toString(),semesterCredit_ET.getText().toString());
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        return view;
    }

    private void deleteSemester(int adapterPosition) {

        Semester semester=semesterList.get(adapterPosition);
        semesterList.remove(semester);
        gradeRepository.deleteSemester(semester);
        gradeRepository.deleteallcoursebysemesterid(semester.getId());
        semesterAdapter.notifyDataSetChanged();
    }

    private void insertSemester(String name, String credit) {
        Semester semester = new Semester(name, Integer.parseInt(credit), 0.0);
        semesterList.add(semester);
        gradeRepository.insertSemester(semester);
        semesterAdapter.notifyDataSetChanged();
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
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