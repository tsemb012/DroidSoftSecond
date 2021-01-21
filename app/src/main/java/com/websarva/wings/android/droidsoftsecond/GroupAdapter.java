package com.websarva.wings.android.droidsoftsecond;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.websarva.wings.android.droidsoftsecond.databinding.I001ItemViewGroupBinding;
import com.websarva.wings.android.droidsoftsecond.model.Group;

public class GroupAdapter extends FirestoreAdapter<GroupAdapter.ViewHolder> {

    public interface OnGroupSelectedListener{//Mainに継承させている。おそらく、画面表示ロジックを担う箇所に入れて、
        void onGroupSelected (DocumentSnapshot group, View view);
    }

    private OnGroupSelectedListener mListener;

    public GroupAdapter(Query query, OnGroupSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GroupAdapter.ViewHolder(I001ItemViewGroupBinding.inflate(
                LayoutInflater.from(parent.getContext()),parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private I001ItemViewGroupBinding binding;

        public ViewHolder(@NonNull I001ItemViewGroupBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }

        public ViewHolder(View itemView){super(itemView);}

        public void bind(final DocumentSnapshot snapshot,
                         final OnGroupSelectedListener listener){

            Group group = snapshot.toObject(Group.class);
            //Resources resource = itemView.getResources();

            GlideApp.with(binding.imageView)
                    .load(FirebaseStorage.getInstance().getReference(group.getPhotoRefPath()))
                    .into(binding.imageView);
            binding.groupNameI001.setText(group.getGroupName());
            //binding.communityPersonNum.setInt(group.getNumberPersonMax());
            //TODO G002 ↑のロジックを書き換える
            //TODO G003 スケジュール部分の設定が完成したら、引っ張ってくる
            binding.communityLocation.setText(group.getActivityAreaPrefecture());
            //TODO G004 ↑ロジックを書き換えて、市も追加する。
            binding.communityComment.setText(group.getGroupIntro());

            itemView.setOnClickListener(new View.OnClickListener(){//StaticのViewHolderからitemViewを引っ張る。
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onGroupSelected(snapshot, view);//よくわからない。
                    }
                }

            });




            //Listenerのロジックを入力する。

        }
    }
}
