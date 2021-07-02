package com.websarva.wings.android.droidsoftsecond.ui.group_dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.websarva.wings.android.droidsoftsecond.R;

public class d004_LeaningFrequencyDialog extends DialogFragment {
    private int selected = 0;
    private DialogFragment dialog_next;
    private String[] list_FrequentlyBasis;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());//ダイアログのビルダーを生成
        list_FrequentlyBasis = getResources().getStringArray(R.array.frequently_basis);
        return builder.setTitle(R.string.learning_frequency)
                .setIcon(R.drawable.ic_baseline_date_range_24)
                .setSingleChoiceItems(list_FrequentlyBasis, selected, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        selected = which;
                    }
                }).setPositiveButton(getString(R.string.Next), new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(selected!=0){
                            dialog_next = new d004_LeaningFrequencyDialog_next();//オンラインおよび市町村選択ダイアログのインスタンス化
                            Bundle args = new Bundle();
                            args.putInt("learning_frequency",selected);
                            dialog_next.setArguments(args);
                            dialog_next.show(getActivity().getSupportFragmentManager(), "activity_area_next");}
                        else{
                            TextView v = getActivity().findViewById(R.id.learning_frequency_input);
                            v.setText(R.string.everyday);
                        }
                    }
                }).setNeutralButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create();}
}//変数の数と変数名だけで匿名クラスを省略し、ラムダ式に変換できるよということ





