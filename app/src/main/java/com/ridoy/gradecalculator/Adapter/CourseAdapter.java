package com.ridoy.gradecalculator.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ridoy.gradecalculator.ModelClass.Course;
import com.ridoy.gradecalculator.R;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    Context context;
    List<Course> courseList;

    public CourseAdapter(Context context, List<Course> courseList) {
        this.context = context;
        this.courseList = courseList;
    }


    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.course_layout,parent,false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {

        Course course=courseList.get(position);
        holder.courseName.setText(course.getCourseName());
        holder.courseCredit.setText("Credit: "+course.getCourseCredit());
        holder.courseSGPA.setText(String.valueOf(course.getCourseSGPA()));

    }

    @Override
    public int getItemCount() {
        if (courseList==null || courseList.isEmpty()){
            return 0;
        }else {
            return courseList.size();
        }
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView courseName,courseCredit,courseSGPA;
        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName=itemView.findViewById(R.id.courseName_TV);
            courseCredit=itemView.findViewById(R.id.courseCredit_TV);
            courseSGPA=itemView.findViewById(R.id.courseSGPA_TV);
        }
    }
}
