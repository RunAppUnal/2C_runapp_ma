package com.runapp.runapp_ma.utils;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class EditTextTimePicker implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {
    private EditText _editText;
    private int _minute;
    private int _hour;
    private Context _context;
    private String toString;

    public EditTextTimePicker(Context context, int editTextViewID)
    {
        this._editText = ((Activity)context).findViewById(editTextViewID);
        this._editText.setOnClickListener(this);
        this._context = context;
    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {
        _hour = hour;
        _minute = minute;

        updateDisplay();
    }
    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        TimePickerDialog dialog = new TimePickerDialog(_context, this,
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),false);
        dialog.show();


    }

    // updates the date in the birth date EditText
    private void updateDisplay() {
        DecimalFormat df = new DecimalFormat("00");
        toString = df.format(_hour) + ":" + df.format(_minute);
        _editText.setText(toString);
    }

    @Override
    public String toString() {
        if(toString == null){
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            toString = calendar.get(Calendar.HOUR_OF_DAY)
                    + ":" + calendar.get(Calendar.MINUTE);
        }
        return toString;
    }
}
