package com.kamgm.doit;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimePickerFragmentNew extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    public static final String PREFS_NAME = "MyPrefsFile";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker.
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        // Create a new instance of DatePickerDialog and return it.
        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {
        // Set the activity to the Main Activity.
        NewTaskActivity activity = (NewTaskActivity) getActivity();
        // Invoke Main Activity's processDatePickerResult() method.
        activity.processTimePickerResult(hour, minute);
    }
}