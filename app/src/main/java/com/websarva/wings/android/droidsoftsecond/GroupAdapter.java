package com.websarva.wings.android.droidsoftsecond;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.websarva.wings.android.droidsoftsecond.databinding.I001ItemViewGroupBinding;
import com.websarva.wings.android.droidsoftsecond.model.Group;


public class GroupAdapter extends FirestoreAdapter<GroupAdapter.ViewHolder> {

    public interface OnGroupSelectedListener{//Mainに継承させている。おそらく、画面表示ロジックを担う箇所に入れて、
        void onGroupSelected (DocumentSnapshot group);
    }

    private OnGroupSelectedListener mListener;

    public GroupAdapter(Query query, OnGroupSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(I001ItemViewGroupBinding.inflate(
                LayoutInflater.from(parent.getContext()),parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private I001ItemViewGroupBinding binding;

        public ViewHolder(I001ItemViewGroupBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }

        public ViewHolder(View itemView){super(itemView);}

        public void bind(final DocumentSnapshot snapshot,
                         final OnGroupSelectedListener listener){

            Group group = snapshot.toObject(Group.class);
            Resources resource = itemView.getResources();

            //TODO G001 ここに画像表示ロジックを埋め込む。
            binding.groupNameI001.setText(group.getGroupName());
            //binding.communityPersonNum.setInt(group.getNumberPersonMax());
            //TODO G002 ↑のロジックを書き換える
            //TODO G003 スケジュール部分の設定が完成したら、引っ張ってくる
            binding.communityLocation.setText(group.getActivityAreaPrefecture());
            //TODO G004 ↑ロジックを書き換えて、市も追加する。
            binding.communityComment.setText(group.getGroupIntro());

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onGroupSelected(snapshot);//よくわからない。
                    }
                }

            });




            //Listenerのロジックを入力する。

        }
    }
}
