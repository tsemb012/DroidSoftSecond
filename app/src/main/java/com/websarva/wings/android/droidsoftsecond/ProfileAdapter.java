package com.websarva.wings.android.droidsoftsecond;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.websarva.wings.android.droidsoftsecond.databinding.I002ItemViewProfileBinding;
import com.websarva.wings.android.droidsoftsecond.model.Profile;

public class ProfileAdapter extends FirestoreAdapter<ProfileAdapter.ViewHolder>{

    public interface OnProfileSelectedListener{
        void onProfileSelected(DocumentSnapshot profile, View view);
    }

    private OnProfileSelectedListener mListener;

    public ProfileAdapter(Query query, OnProfileSelectedListener listener){
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProfileAdapter.ViewHolder(I002ItemViewProfileBinding.inflate(
                LayoutInflater.from(parent.getContext()),parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.ViewHolder holder, int position) {
        holder.bind(getSnapshot(position),mListener);
    }


    static class ViewHolder extends RecyclerView.ViewHolder{

        private I002ItemViewProfileBinding binding;

        public ViewHolder(@NonNull I002ItemViewProfileBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ViewHolder(View itemView){super(itemView);}

        public void bind (final DocumentSnapshot snapshot,
                          final OnProfileSelectedListener listener){

            Profile profile = snapshot.toObject(Profile.class);

            GlideApp.with(binding.profileView)
                    .load(FirebaseStorage.getInstance().getReference(profile.getProfilePhotoPath()))
                    .into(binding.profileView);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onProfileSelected(snapshot, view);//よくわからない。
                    }
                }

            });
        }
    }

}
