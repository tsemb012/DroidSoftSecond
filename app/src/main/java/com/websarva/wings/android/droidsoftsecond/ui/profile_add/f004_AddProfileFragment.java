package com.websarva.wings.android.droidsoftsecond.ui.profile_add;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.websarva.wings.android.droidsoftsecond.GlideApp;
import com.websarva.wings.android.droidsoftsecond.R;
import com.websarva.wings.android.droidsoftsecond.databinding.F004FragmentProfileAddBinding;
import com.websarva.wings.android.droidsoftsecond.model.Profile;
import com.websarva.wings.android.droidsoftsecond.ui.group_dialog.d008_ResidentialAreaDialog;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static java.lang.String.valueOf;

public class f004_AddProfileFragment extends Fragment implements View.OnClickListener {

    private static final int RC_TAKE_PICTURE = 102;
    private FirebaseFirestore mFirestore;
    private F004FragmentProfileAddBinding mBinding;
    private FirebaseUser user;
    private StorageReference mStorageRef;
    private String profileName;
    private Uri profilePhotoUri;
    private String uid;
    private StorageReference profilePhotoRef;
    private String profilePhotoPath;
    private DialogFragment dialog;
    private FragmentManager manager;
    private boolean isPhotoTaken;
    private Profile profile;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getActivity().getSupportFragmentManager();
        mFirestore = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        profileName = user.getDisplayName();
        profilePhotoUri = user.getPhotoUrl();
        uid = user.getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = F004FragmentProfileAddBinding.inflate(inflater, container, false);
        mBinding.editProfileView.setOnClickListener(this);
        mBinding.btnResidentialArea.setOnClickListener(this);
        mBinding.btnAddProfile.setOnClickListener(this);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.editProfileNickname.setText(profileName);
        GlideApp.with(mBinding.editProfileView)
                .load(profilePhotoUri)
                .into(mBinding.editProfileView);
        mBinding.seekBarAge.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {}
            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
                mBinding.editAge.setText(valueOf(seekBar.getProgress()));
            }
        });
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.editProfileView:
                launchPhotoTaking();
                break;
            case R.id.btnResidentialArea:
                dialog = new d008_ResidentialAreaDialog();//オンラインおよび市町村選択ダイアログのインスタンス化
                dialog.show(manager, "activity_area");
                break;
            case R.id.btnAddProfile:
                uploadFromUri(profilePhotoUri);//TODO 不具合が発生しないか確認する。
                String nickname = mBinding.editProfileNickname.getText().toString();
                String comment = mBinding.editComment.getText().toString();
                RadioButton radioButton = (RadioButton)getActivity().findViewById(mBinding.radioGroup.getCheckedRadioButtonId());
                String gender = radioButton.getText().toString();
                int age = mBinding.seekBarAge.getProgress();
                String residentialArea = mBinding.editResidentialArea.getText().toString();
                profile = new Profile(
                        uid,
                        profilePhotoPath,
                        nickname,
                        comment,
                        gender,
                        age,
                        residentialArea
                );
                mFirestore.collection("profiles").document(uid).set(profile)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Check2021/01/20", "ProfileAdded");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Check2021/01/20", "ProfileAddFiled");
                            }
                        });

                //トースト作成
                Toast.makeText(getActivity(),R.string.profile_add,Toast.LENGTH_SHORT).show();
                //画面遷移
                NavDirections action = f004_AddProfileFragmentDirections.actionF004AddProfileFragmentToBnf001Search();
                Navigation.findNavController(v).navigate(action);
                break;
        }
    }

    private void launchPhotoTaking(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);//暗黙的インテント//DocumentProviderを開く//https://developer.android.com/guide/topics/providers/document-provider?hl=ja
        intent.setType("image/*");//MIMEタイプ:ファイルの種類を表す情報//写真を取得する場合"image/*"//https://developer.android.com/guide/components/intents-common?hl=ja
        startActivityForResult(intent, RC_TAKE_PICTURE);//requestCode:エントリポイントの目印//DocumentProviderに遷移
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_TAKE_PICTURE) {
            if (resultCode == RESULT_OK) {
                profilePhotoUri = data.getData();//IntentからURIの取り出し。→
                if (profilePhotoUri != null) {
                    Glide.with(mBinding.editProfileView)
                            .load(profilePhotoUri)
                            .into(mBinding.editProfileView);
                    //isPhotoTaken = true;
                    //TODO アップロードボタンクリック時
                } else {
                    Log.w(TAG, "File URI is null");
                }
            } else {
                Toast.makeText(getActivity(), R.string.upload_fail, Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void uploadFromUri(Uri profilePhotoUri) {
        showProgressBar(getString(R.string.progress_uploading));
        mBinding.editProfileView.setDrawingCacheEnabled(true);
        mBinding.editProfileView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable)mBinding.editProfileView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] data = baos.toByteArray();

        profilePhotoRef = mStorageRef.child("profilePhotos").child(user.getPhotoUrl().getLastPathSegment());//TODO　乱数をいれる必要がある。
        profilePhotoPath = profilePhotoRef.getPath();
        UploadTask uploadTask = profilePhotoRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getActivity(), R.string.upload_fail, Toast.LENGTH_SHORT).show();
                hideProgressBar();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getActivity(), R.string.upload_success, Toast.LENGTH_SHORT).show();
                hideProgressBar();
            }
        });
    }

    private void showProgressBar(String caption) {
        mBinding.caption.setText(caption);
        mBinding.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mBinding.caption.setText("");
        mBinding.progressBar.setVisibility(View.INVISIBLE);
    }

}
