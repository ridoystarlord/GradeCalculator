package com.ridoy.gradecalculator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ridoy.gradecalculator.BarChart.BarchartActivity;
import com.ridoy.gradecalculator.Repository.GradeRepository;

public class MainActivity extends AppCompatActivity {

    GradeRepository gradeRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gradeRepository = new GradeRepository(getApplication());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_main_share:
                try {
                    Intent intent=new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT,"CGPA Koto?");
                    intent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName());
                    startActivity(Intent.createChooser(intent,"Share With"));
                } catch (Exception e) {
                    Toast.makeText(this, "Unable to share this App\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.main_menu_clearalldata:
                gradeRepository.deleteAllsemester();
                gradeRepository.deleteAllcourse();
                finish();
                startActivity(getIntent());
                break;
            case R.id.menu_main_rate:
                Uri uri= Uri.parse("https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName());

                Intent i=new Intent(Intent.ACTION_VIEW,uri);
                try {
                    startActivity(i);
                } catch (Exception e) {
                    Toast.makeText(this, "Unable to share this App\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_main_barchat:
                startActivity(new Intent(MainActivity.this, BarchartActivity.class));
                break;

            default:
        }
        return true;
    }

}