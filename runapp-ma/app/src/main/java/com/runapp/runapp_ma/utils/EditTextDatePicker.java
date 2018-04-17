package com.runapp.runapp_ma.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.TimeZone;

public class EditTextDatePicker implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private EditText _editText;
    private int _day;
    private int _month;
    private int _year;
    private Context _context;
    private String toString;

    public EditTextDatePicker(Context context, int editTextViewID)
    {
        this._editText = ((Activity)context).findViewById(editTextViewID);
        this._editText.setOnClickListener(this);
        this._context = context;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        _year = year;
        _month = monthOfYear;
        _day = dayOfMonth;
        updateDisplay();
    }
    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        DatePickerDialog dialog = new DatePickerDialog(_context, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();


    }

    // updates the date in the birth date EditText
    private void updateDisplay() {
        toString = String.valueOf(_month + 1) + "/" +
                _day + "/" +
                _year + " ";
        _editText.setText(toString);
    }

    @Override
    public String toString() {
        if(toString == null){
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            toString = String.valueOf(calendar.get(Calendar.MONTH) +1) + "/" + calendar.get(Calendar.DAY_OF_MONTH)
                    + "/" + calendar.get(Calendar.YEAR);
        }
        return toString;
    }
}
