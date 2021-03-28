package com.ridoy.gradecalculator.BarChart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.ridoy.gradecalculator.ModelClass.Semester;
import com.ridoy.gradecalculator.R;
import com.ridoy.gradecalculator.Repository.GradeRepository;

import java.util.ArrayList;
import java.util.List;

public class BarchartActivity extends AppCompatActivity {

    BarChart barChart;
    List<BarEntry> barEntryList;
    List<Semester> barchatItemsList;
    List<String> labelname;
    GradeRepository gradeRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barchart);

        gradeRepository = new GradeRepository(getApplication());
        barChart=findViewById(R.id.barchat);
        barEntryList=new ArrayList<>();
        barchatItemsList=new ArrayList<>();
        labelname=new ArrayList<>();
        barchatItemsList=gradeRepository.getAllSemesters();

        barEntryList.clear();
        labelname.clear();

        for (int i=0;i<barchatItemsList.size();i++){
            String semesterName=barchatItemsList.get(i).getSemesterName();
            double semesterCGPA=barchatItemsList.get(i).getSemesterCGPA();
            barEntryList.add(new BarEntry(i, (float) semesterCGPA));
            labelname.add(semesterName);
        }

        BarDataSet barDataSet=new BarDataSet(barEntryList,"Semester");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        Description description=new Description();
        description.setText("Result");
        barChart.setDescription(description);

        BarData barData=new BarData(barDataSet);
        barChart.setData(barData);

        XAxis xAxis=barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelname));
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(labelname.size());
        xAxis.setLabelRotationAngle(0);
        barChart.animateY(1000);
        barChart.invalidate();
    }
}