package com.websarva.wings.android.droidsoftsecond;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

public class d002_ActivityAreaDialog extends DialogFragment {
    private int selected = 0;
    private DialogFragment dialog_next;
    private String[] list_prefecture;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());//ダイアログのビルダーを生成
        list_prefecture = getResources().getStringArray(R.array.online_and_prefectures);
        return builder.setTitle(R.string.activity_area)
                .setIcon(R.drawable.ic_baseline_location_on_24)
                .setSingleChoiceItems(list_prefecture, selected, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        selected = which;
                    }
                }).setPositiveButton(getString(R.string.Next), new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(selected!=0){
                                dialog_next = new d002_ActivityAreaDialog_next();//オンラインおよび市町村選択ダイアログのインスタンス化
                                    Bundle args = new Bundle();
                                    args.putInt("prefecture",selected);
                                    dialog_next.setArguments(args);
                                dialog_next.show(getActivity().getSupportFragmentManager(), "activity_area_next");}
                                else{
                                    TextView v = getActivity().findViewById(R.id.activity_area_input);
                                    v.setText(list_prefecture[0].toString());
                                }
                            }
                }).setNeutralButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                }).create();}}//変数の数と変数名だけで匿名クラスを省略し、ラムダ式に変換できるよということ
