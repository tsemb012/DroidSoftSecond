package com.websarva.wings.android.droidsoftsecond.ui.group_dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.websarva.wings.android.droidsoftsecond.R;

public class d004_LeaningFrequencyDialog_next extends DialogFragment {
    private int selected_previous;
    private int selected_current=0;
    private String basis;
    private int frequency;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        selected_previous = args.getInt("learning_frequency");

    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.d004_dialog_learning_frequency, null, false);
        NumberPicker np = (NumberPicker) view.findViewById(R.id.numberPicker);
        TextView tv = view.findViewById(R.id.basis);
        switch (selected_previous) {
            case 1:
                tv.setText(R.string.week);
                np.setMaxValue(7);
                np.setMinValue(1);
                break;
            case 2:
                tv.setText(R.string.month);
                np.setMaxValue(31);
                np.setMinValue(1);
                break;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());//ダイアログのビルダーを生成
        //入力されたダイアログをビューに表示。
        return builder
                .setTitle(R.string.learning_frequency)
                .setIcon(R.drawable.ic_baseline_date_range_24)
                /*.setSingleChoiceItems(items, selected_current, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selected_current = which;
                    }
                })*/
                .setPositiveButton(R.string.done,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            basis = tv.getText().toString();
                            frequency = np.getValue();
                            TextView v = getActivity().findViewById(R.id.learning_frequency_input);
                            v.setText(String.format("%s%d回",basis.toString(),frequency));
                        }
                    }).setNeutralButton(R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }})
                .setView(view)
                .create();//変数の数と変数名だけで匿名クラスを省略し、ラムダ式に変換できるよということ？
        }
    }

        /*
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {




        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Number Picker");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // OKクリック時の処理
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.setView(view);
        return builder.create();
    }
}/*

 /*   // Dialogの表示
    d004_LeaningFrequencyDialog dialog = new d004_LeaningFrequencyDialog();
        dialog.show(getFragmentManager(), "span_setting_dialog");
                }*/

