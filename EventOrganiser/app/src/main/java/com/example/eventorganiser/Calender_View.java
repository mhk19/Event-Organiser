package com.example.eventorganiser;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Calender_View extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender__view);

        CalendarView view = new CalendarView(this);
        setContentView(view);
        view.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int date) {
                int month_ = month+1;
                String Date =  date + "/" + month_ + "/" + year;
                Bundle bDate = new Bundle();
                bDate.putString("date",Date);
                Intent intent = new Intent(Calender_View.this,Home.class);
                intent.putExtras(bDate);
                startActivity(intent);
                finish();
            }
        });

    }
}
