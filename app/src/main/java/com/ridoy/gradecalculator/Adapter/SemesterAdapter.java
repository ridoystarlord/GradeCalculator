package com.ridoy.gradecalculator.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ridoy.gradecalculator.DataController;
import com.ridoy.gradecalculator.ModelClass.Semester;
import com.ridoy.gradecalculator.R;

import java.util.List;

public class SemesterAdapter extends RecyclerView.Adapter<SemesterAdapter.SemesterViewHolder> {

    Context context;
    List<Semester> semesterList;

    public SemesterAdapter(Context context, List<Semester> semesterList) {
        this.context = context;
        this.semesterList = semesterList;
    }

    @NonNull
    @Override
    public SemesterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.semester_layout,parent,false);
        return new SemesterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SemesterViewHolder holder, int position) {
        Semester semester=semesterList.get(position);
        holder.semesterName.setText(semester.getSemesterName());
        holder.semesterCredit.setText("Credit: "+String.valueOf(semester.getSemesterCredit()));
        double tempcgpa=semester.getSemesterCGPA();
        holder.semesterCGPA.setText(String.format("%.2f", tempcgpa));
    }

    @Override
    public int getItemCount() {
        if (semesterList==null || semesterList.isEmpty()){
            return 0;
        }else {
            return semesterList.size();
        }
    }

    public class SemesterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView semesterName,semesterCredit,semesterCGPA;
        public SemesterViewHolder(@NonNull View itemView) {
            super(itemView);
            semesterName=itemView.findViewById(R.id.semesterName_TV);
            semesterCredit=itemView.findViewById(R.id.semesterCredit_TV);
            semesterCGPA=itemView.findViewById(R.id.semesterCGPA_TV);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            DataController.Instence.getHomefragmentinterface().onSemesterItemClicked(semesterList.get(getAdapterPosition()));
        }
    }
}
