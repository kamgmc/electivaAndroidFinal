package com.kamgm.doit;

import android.database.Cursor;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class NewTaskActivity extends AppCompatActivity {

    private EditText taskTitulo ;
    private EditText taskFecha ;
    private EditText taskHora ;
    private EditText taskDetalle ;
    private CheckBox taskCompletado ;
    private String id;

    private TaskDbHelper mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        taskTitulo = findViewById(R.id.inputTituloNew);
        taskFecha = findViewById(R.id.inputFechaNew);
        taskHora = findViewById(R.id.inputHoraNew);
        taskDetalle = findViewById(R.id.inputDetailNew);
        taskCompletado = findViewById(R.id.completadoNew);

        //crear base de datos
        mDB = new TaskDbHelper(this);
    }

    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragmentNew();
        newFragment.show(getSupportFragmentManager(),getString(R.string.date_picker));
    }

    public void showTimePickerDialog(View view) {
        DialogFragment newFragment = new TimePickerFragmentNew();
        newFragment.show(getSupportFragmentManager(),
                getString(R.string.time_picker));
    }

    public void processDatePickerResult(int year, int  month, int day) {
        String month_string;
        String day_string;
        if (month < 10) {
            month_string = "0" + Integer.toString(month + 1);
        }else {
            month_string = Integer.toString(month + 1);
        }
        if (day < 10) {
            day_string= "0"+ Integer.toString(day);
        }else {
            day_string = Integer.toString(day);
        }

        String year_string = Integer.toString(year);
        String dateMessage = (year_string + "-" + month_string + "-" + day_string );

        taskFecha.setText(dateMessage);
    }

    public void saveDataTask(View view) {
        String fecha= taskFecha.getText().toString();
        String hora= taskHora.getText().toString()+":00";
        Log.d("*** fecha y hora ***", fecha+" "+hora);
        mDB.saveTask(taskTitulo.getText().toString(),taskDetalle.getText().toString(),fecha+" "+hora,taskCompletado.isChecked());
        Toast.makeText(this, "Tarea Guardadas Exitosamente", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

    public void processTimePickerResult(int hour, int minute) {
        String hour_string = Integer.toString(hour);
        String minute_string = Integer.toString(minute);
        String timeMessage = (hour_string + ":" + minute_string);
        taskHora.setText(timeMessage);
    }

}
