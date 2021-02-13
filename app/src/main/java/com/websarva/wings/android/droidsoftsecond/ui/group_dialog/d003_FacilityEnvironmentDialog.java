package com.websarva.wings.android.droidsoftsecond.ui.group_dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.websarva.wings.android.droidsoftsecond.R;

public class d003_FacilityEnvironmentDialog extends DialogFragment {
    private int selected = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] items = {
                getString(R.string.library),
                getString(R.string.cafe_restaurant),
                getString(R.string.rental_space),
                getString(R.string.study_place),
                getString(R.string.park),
                getString(R.string.online),
                getString(R.string.other)
        };//説明を入れれるようになりたい。
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());//ダイアログのビルダーを生成
        //入力されたダイアログをビューに表示。
        return builder
                .setTitle(R.string.facility_environment)
                .setIcon(R.drawable.ic_baseline_location_city_24)
                .setSingleChoiceItems(items, selected, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        selected = which;
                    }
                })
                .setPositiveButton(getString(R.string.done), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TextView v = getActivity().findViewById(R.id.facility_environment_input);
                        v.setText(items[selected].toString());
                    }
                })
                .setNeutralButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                }).create();//変数の数と変数名だけで匿名クラスを省略し、ラムダ式に変換できるよということ？
    }
}