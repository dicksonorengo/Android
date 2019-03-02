package me.jfenn.alarmio.dialogs;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import com.afollestad.aesthetic.Aesthetic;

import io.reactivex.functions.Consumer;
import me.jfenn.alarmio.Alarmio;
import me.jfenn.timedatepickers.dialogs.TimeSheetPickerDialog;

public class AestheticTimeSheetPickerDialog extends TimeSheetPickerDialog {

    public AestheticTimeSheetPickerDialog(Context context) {
        super(context);
    }

    public AestheticTimeSheetPickerDialog(Context context, int hourOfDay, int minute) {
        super(context, hourOfDay, minute);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Aesthetic.Companion.get()
                .textColorPrimary()
                .take(1)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) {
                        setPrimaryTextColor(integer);
                    }
                });

        Aesthetic.Companion.get()
                .textColorSecondary()
                .take(1)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) {
                        setSecondaryTextColor(integer);
                    }
                });

        Aesthetic.Companion.get().colorPrimary()
                .take(1)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) {
                        setBackgroundColor(integer);
                        setPrimaryBackgroundColor(integer);
                        setSecondaryBackgroundColor(integer);
                    }
                });

        Aesthetic.Companion.get().colorAccent()
                .take(1)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) {
                        setSelectionColor(integer);
                        setSelectionTextColor(((Alarmio) getContext().getApplicationContext()).getActivityTheme() == Alarmio.THEME_AMOLED ? Color.BLACK : Color.WHITE);
                    }
                });
    }
}
