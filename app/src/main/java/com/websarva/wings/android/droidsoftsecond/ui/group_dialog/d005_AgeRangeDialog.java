package com.websarva.wings.android.droidsoftsecond.ui.group_dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.slider.RangeSlider;
import com.websarva.wings.android.droidsoftsecond.R;
import com.websarva.wings.android.droidsoftsecond.viewmodel.MainViewModel;

import static java.lang.Math.round;

public class d005_AgeRangeDialog extends DialogFragment {

    private TextView tv;
    private RangeSlider rs;
    private int minAge;
    private int maxAge;
    private MainViewModel model;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.d005_dialog_age_range,null,false);
        rs = view.findViewById(R.id.rangeSlider_d005);
        rs.setStepSize(1);
        rs.setThumbTintList(getResources().getColorStateList(R.color.range_slider));
        minAge = round(rs.getValues().get(0));
        maxAge = round(rs.getValues().get(1));

        model = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        model.setMinAge(minAge);
        model.setMaxAge(maxAge);


        rs.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                tv = view.findViewById(R.id.tv_range_slider);
                tv.setText(String.format("%s~%s歳",round(rs.getValues().get(0)),round(rs.getValues().get(1))));
                minAge = round(rs.getValues().get(0));
                maxAge = round(rs.getValues().get(1));

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

                                /*NOTE Bundleでは受け渡しが不可→受け渡し後の値が0になってしまう。
                                    Bundle args = new Bundle();
                                    args.putInt("age_min",minAge);
                                    args.putInt("age_max",maxAge);
                                    f002_AddGroupFragment fragment = new f002_AddGroupFragment();
                                    fragment.setArguments(args);
                                    →代替案としてSafeArgを設定。
                                    →おそらくフラグメントやアクティビティのライフサイクルに関わっていると考えられる。*/

                                /*NOTE SafeArgを用いた値の受け渡し。しかし、受け渡し後の値が0になってしまう。
                                   d005_AgeRangeDialogDirections.ActionD005AgeRangeDialogToF002AddGroupFragment2 action
                                    = d005_AgeRangeDialogDirections.actionD005AgeRangeDialogToF002AddGroupFragment2();
                                    action.setNum(minAge);
                                    /*受け取り側の処理　
                                        Bundle args = getArguments();
                                        minAge = f002_AddGroupFragmentArgs.fromBundle(args).getNum();*/

                                model.setMinAge(minAge);
                                model.setMaxAge(maxAge);
                                Log.i("check17", String.valueOf(minAge));
                                Log.i("check18", String.valueOf(maxAge));

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
