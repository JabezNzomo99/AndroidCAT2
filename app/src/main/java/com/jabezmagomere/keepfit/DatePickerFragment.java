package com.jabezmagomere.keepfit;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends DialogFragment {


    DatePickerDialog.OnDateSetListener ondateSet;
    private int year, month, day;
    public void setCallBack(DatePickerDialog.OnDateSetListener ondate){
        ondateSet=ondate;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        year=args.getInt("year");
        month=args.getInt("month");
        day=args.getInt("day");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new DatePickerDialog(getContext(),ondateSet,year,month,day);
    }
}
