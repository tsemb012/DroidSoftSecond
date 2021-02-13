package com.websarva.wings.android.droidsoftsecond.ui.group_dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.websarva.wings.android.droidsoftsecond.R;

public class d002_ActivityAreaDialog_next extends DialogFragment {

    private int selected_previous;
    private int selected_current=0;
    //List<String> items = new ArrayList<String>();
    String[] list_prefecture;
    String[] items;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        selected_previous = args.getInt("prefecture");
        list_prefecture= getResources().getStringArray(R.array.online_and_prefectures);

        switch (selected_previous){
            case 0: break;
            case 1: items = getResources().getStringArray(R.array.Hokkaido);break;
            case 2: items = getResources().getStringArray(R.array.Aomori);break;
            case 3: items = getResources().getStringArray(R.array.Iwate);break;
            case 4: items = getResources().getStringArray(R.array.Miyagi);break;
            case 5: items = getResources().getStringArray(R.array.Akita);break;
            case 6: items = getResources().getStringArray(R.array.Yamagata);break;
            case 7: items = getResources().getStringArray(R.array.Fukushima);break;
            case 8: items = getResources().getStringArray(R.array.Ibaraki);break;
            case 9: items = getResources().getStringArray(R.array.Tochigi);break;
            case 10: items = getResources().getStringArray(R.array.Gunma);break;
            case 11: items = getResources().getStringArray(R.array.Saitama);break;
            case 12: items = getResources().getStringArray(R.array.Chiba);break;
            case 13: items = getResources().getStringArray(R.array.Tokyo);break;
            case 14: items = getResources().getStringArray(R.array.Kanagawa);break;
            case 15: items = getResources().getStringArray(R.array.Niigata);break;
            case 16: items = getResources().getStringArray(R.array.Toyama);break;
            case 17: items = getResources().getStringArray(R.array.Ishikawa);break;
            case 18: items = getResources().getStringArray(R.array.Fukui);break;
            case 19: items = getResources().getStringArray(R.array.Yamanashi);break;
            case 20: items = getResources().getStringArray(R.array.Nagano);break;
            case 21: items = getResources().getStringArray(R.array.Gifu);break;
            case 22: items = getResources().getStringArray(R.array.Shizuoka);break;
            case 23: items = getResources().getStringArray(R.array.Aichi);break;
            case 24: items = getResources().getStringArray(R.array.Mie);break;
            case 25: items = getResources().getStringArray(R.array.Shiga);break;
            case 26: items = getResources().getStringArray(R.array.Kyoto);break;
            case 27: items = getResources().getStringArray(R.array.Osaka);break;
            case 28: items = getResources().getStringArray(R.array.Hyogo);break;
            case 29: items = getResources().getStringArray(R.array.Nara);break;
            case 30: items = getResources().getStringArray(R.array.Wakayama);break;
            case 31: items = getResources().getStringArray(R.array.Tottori);break;
            case 32: items = getResources().getStringArray(R.array.Shimane);break;
            case 33: items = getResources().getStringArray(R.array.Okayama);break;
            case 34: items = getResources().getStringArray(R.array.Hiroshima);break;
            case 35: items = getResources().getStringArray(R.array.Yamaguchi);break;
            case 36: items = getResources().getStringArray(R.array.Tokuhsima);break;
            case 37: items = getResources().getStringArray(R.array.Kagawa);break;
            case 38: items = getResources().getStringArray(R.array.Ehime);break;
            case 39: items = getResources().getStringArray(R.array.Kochi);break;
            case 40: items = getResources().getStringArray(R.array.Fukuoka);break;
            case 41: items = getResources().getStringArray(R.array.Saga);break;
            case 42: items = getResources().getStringArray(R.array.Nagasaki);break;
            case 43: items = getResources().getStringArray(R.array.Kumamoto);break;
            case 44: items = getResources().getStringArray(R.array.Ooita);break;
            case 45: items = getResources().getStringArray(R.array.Miyazaki);break;
            case 46: items = getResources().getStringArray(R.array.Kagoshima);break;
            case 47: items = getResources().getStringArray(R.array.Okinawa);break;
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());//ダイアログのビルダーを生成
        //入力されたダイアログをビューに表示。
        return builder
                .setTitle(R.string.activity_area)
                .setIcon(R.drawable.ic_baseline_location_on_24)
                .setSingleChoiceItems(items,selected_current,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selected_current = which;
                    }
                }).setPositiveButton(R.string.done,
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){
                                TextView v = getActivity().findViewById(R.id.activity_area_input);
                                v.setText(String.format("%s、%s",list_prefecture[selected_previous].toString(),items[selected_current].toString()));
                            }
                }).setNeutralButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })

                .create();//変数の数と変数名だけで匿名クラスを省略し、ラムダ式に変換できるよということ？
    }
}
