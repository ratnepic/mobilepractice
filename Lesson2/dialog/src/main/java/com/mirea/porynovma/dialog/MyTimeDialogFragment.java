package com.mirea.porynovma.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class MyTimeDialogFragment extends DialogFragment {

    Calendar dttm = Calendar.getInstance();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        TimePickerDialog dialog = new TimePickerDialog(getActivity(), t,
                dttm.get(Calendar.HOUR_OF_DAY),
                dttm.get(Calendar.MINUTE), true
        );
        return dialog;
    }

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dttm.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dttm.set(Calendar.MINUTE, minute);
        }
    };
}
