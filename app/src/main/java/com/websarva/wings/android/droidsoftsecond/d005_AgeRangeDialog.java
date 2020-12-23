package com.websarva.wings.android.droidsoftsecond;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.slider.RangeSlider;

import static java.lang.Math.round;

public class d005_AgeRangeDialog extends DialogFragment {

    private TextView tv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.d005_dialog_age_range,null,false);
        RangeSlider rs = view.findViewById(R.id.rangeSlider_d005);
        rs.setStepSize(1);
        rs.setThumbTintList(getResources().getColorStateList(R.color.range_slider));
        rs.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                tv = view.findViewById(R.id.tv_range_slider);
                tv.setText(String.format("%s~%s歳",round(rs.getValues().get(0)),round(rs.getValues().get(1))));
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        return builder
                .setTitle(R.string.age_range)
                .setIcon(R.drawable.ic_baseline_elevator_24)
                .setPositiveButton(R.string.done,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                TextView v = getActivity().findViewById(R.id.age_range_input);
                                v.setText(tv.getText().toString());
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
