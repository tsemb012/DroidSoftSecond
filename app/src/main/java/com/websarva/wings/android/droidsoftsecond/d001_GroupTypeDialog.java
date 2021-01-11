package com.websarva.wings.android.droidsoftsecond;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

public class d001_GroupTypeDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final String[] items = {getString(R.string.seminar), getString(R.string.workshop),
                getString(R.string.mokumoku), getString(R.string.other)};//説明を入れれるようになりたい。
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());//ダイアログのビルダーを生成
        //入力されたダイアログをビューに表示。
        return builder
                .setTitle(R.string.group_name)
                .setIcon(R.drawable.ic_baseline_group_24)
                .setItems(items, (dialog, which) -> {
                    TextView v = getActivity().findViewById(R.id.group_type_input);
                    v.setText(items[which].toString());
                }).create();//変数の数と変数名だけで匿名クラスを省略し、ラムダ式に変換できるよということ？
    }

}
