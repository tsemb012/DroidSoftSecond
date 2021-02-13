package com.websarva.wings.android.droidsoftsecond.ui.group_add;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.websarva.wings.android.droidsoftsecond.GlideApp;
import com.websarva.wings.android.droidsoftsecond.R;
import com.websarva.wings.android.droidsoftsecond.databinding.F002FragmentGroupAddBinding;
import com.websarva.wings.android.droidsoftsecond.model.Group;
import com.websarva.wings.android.droidsoftsecond.ui.group_dialog.d001_GroupTypeDialog;
import com.websarva.wings.android.droidsoftsecond.ui.group_dialog.d002_ActivityAreaDialog;
import com.websarva.wings.android.droidsoftsecond.ui.group_dialog.d003_FacilityEnvironmentDialog;
import com.websarva.wings.android.droidsoftsecond.ui.group_dialog.d004_LeaningFrequencyDialog;
import com.websarva.wings.android.droidsoftsecond.ui.group_dialog.d005_AgeRangeDialog;
import com.websarva.wings.android.droidsoftsecond.ui.group_dialog.d006_NumberPersonsDialog;
import com.websarva.wings.android.droidsoftsecond.viewmodel.MainViewModel;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class f002_AddGroupFragment extends Fragment implements View.OnClickListener {


    private F002FragmentGroupAddBinding mBinding;
    private FirebaseFirestore mFirestore;
    private DocumentReference mGroupRef;
    private MainViewModel model;
    private Group group;
    private FragmentManager manager;
    private DialogFragment dialog;
    private String activityArea;
    private String prefecture;
    private String city;
    private int minAge;
    private int maxAge = 0;
    private int minNumberPerson;
    private int maxNumberPerson;

    //-----Upload関連
    private static final int RC_TAKE_PICTURE = 101;
    private static final String KEY_FILE_URI = "key_file_uri";
    private BroadcastReceiver mBroadcastReceiver;
    private Uri mDownloadUrl = null;
    private Uri mFileUri = null;//Fileの位置情報
    private StorageReference mStorageRef;
    private StorageReference photoRef;



    public f002_AddGroupFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getActivity().getSupportFragmentManager();
        minNumberPerson = getArguments().getInt("number_person_min");
        maxNumberPerson = getArguments().getInt("number_person_max");

        //-----FireStoreレファレンス取得
        mFirestore = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //-----ViewModelのエントリポイント
        model = new ViewModelProvider(requireActivity()).get(MainViewModel.class);



        //-----不明のため後回し。
        //onNewIntent(getIntent());

        //-----ブロードキャスト
//        mBroadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
//            }
//        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // -----レイアウト関連
        mBinding = F002FragmentGroupAddBinding.inflate(inflater, container, false);
        mBinding.btnGroupImage.setOnClickListener(this);
        mBinding.btnToGroupDetailBarGroupType.setOnClickListener(this);
        mBinding.btnToGroupDetailBarActivityArea.setOnClickListener(this);
        mBinding.btnToGroupDetailBarFacilityEnvironment.setOnClickListener(this);
        mBinding.btnToGroupDetailBarLearningFrequency.setOnClickListener(this);
        mBinding.btnToGroupDetailBarAgeRange.setOnClickListener(this);
        mBinding.btnToGroupDetailBarNumberPersons.setOnClickListener(this);
        mBinding.btnToGroupDetailBarGenderRestriction.setOnClickListener(this);
        mBinding.btnToGroupPOJO.setOnClickListener(this);

        return mBinding.getRoot();
    }


    @SuppressLint("UseRequireInsteadOfGet")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_group_image:
                launchUploader();
                break;
            case R.id.btn_to_groupDetailBar_group_type:
                dialog = new d001_GroupTypeDialog();//タイプ選択のダイアログインスタンス化
                dialog.show(manager, "group_type");
                break;
            case R.id.btn_to_groupDetailBar_activity_area:
                dialog = new d002_ActivityAreaDialog();//オンラインおよび市町村選択ダイアログのインスタンス化
                dialog.show(manager, "activity_area");
                break;
            case R.id.btn_to_groupDetailBar_facility_environment:
                dialog = new d003_FacilityEnvironmentDialog();//施設ダイアログのインスタンス化
                dialog.show(manager, "facility_environment");
                break;
            case R.id.btn_to_groupDetailBar_learning_frequency:
                dialog = new d004_LeaningFrequencyDialog();//施設ダイアログのインスタンス化
                dialog.show(manager, "learning_frequency");
                break;
            case R.id.btn_to_groupDetailBar_age_range:
                dialog = new d005_AgeRangeDialog();//施設ダイアログのインスタンス化
                dialog.show(manager, "age_range");
                break;
            case R.id.btn_to_groupDetailBar_number_persons:
                dialog = new d006_NumberPersonsDialog();//施設ダイアログのインスタンス化
                dialog.show(manager, "number_persons");
                break;
            case R.id.btn_to_groupDetailBar_gender_restriction:
                SwitchMaterial sm = mBinding.genderRestrictionSwitch;//施設ダイアログのインスタンス化
                sm.setOnCheckedChangeListener(
                        new CompoundButton.OnCheckedChangeListener() {
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                Toast.makeText(
                                        getContext(),
                                        isChecked ? "性別設定をOnにしました。" : "性別設定をOffにしました。",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        });
                break;
            case R.id.btn_to_groupPOJO:
                activityArea = mBinding.activityAreaInput.getText().toString();
                prefecture = activityArea.substring(activityArea.lastIndexOf("、") + 1);
                city = activityArea.substring(0, activityArea.lastIndexOf("、"));
                minAge = model.getMinAge();
                maxAge = model.getMaxAge();
                minNumberPerson = model.getMinNumberPerson();
                maxNumberPerson = model.getMaxNumberPerson();

                Log.i("check16", String.valueOf(minAge));
                group = new Group(
                        FirebaseAuth.getInstance().getCurrentUser(),
                        photoRef.getPath(),
                        mBinding.editGroupName.getText().toString(),
                        mBinding.editGroupIntro.getText().toString(),
                        mBinding.groupTypeInput.getText().toString(),
                        prefecture,
                        city,
                        mBinding.facilityEnvironmentInput.getText().toString(),
                        mBinding.learningFrequencyInput.getText().toString(),
                        minAge,
                        maxAge,
                        minNumberPerson,
                        maxNumberPerson,
                        mBinding.genderRestrictionSwitch.isChecked()
                );

                Log.i("check11",mBinding.groupTypeInput.getText().toString());
                addGroup(mFirestore.collection("groups"),group);
                        /*.addOnSuccessListener((Executor) this, new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Check", "GroupAdded");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Check", "GroupAddFiled");
                            }
                        });*/
                //AddOnSuccessListenerはインターフェースなど様々なところと関連しているように思われる。

                //トースト作成
                Toast.makeText(getActivity(),R.string.group_add,Toast.LENGTH_SHORT).show();
                //画面遷移
                NavDirections action = f002_AddGroupFragmentDirections.actionF002AddGroupFragment2ToBnf001Search();
                Navigation.findNavController(view).navigate(action);

                break;
        }
    }

    //addOnSuccessと関係しているような気がする。
    private Task<Void> addGroup(final CollectionReference groupRef, final Group group) {
         return mFirestore.runTransaction(new Transaction.Function<Void>(){
             @Nullable
             @Override
             public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                 groupRef.add(group);
                 Log.i("check12",mBinding.groupTypeInput.getText().toString());
                 return null;
             }
         });
    }





    private void launchUploader(){
        Log.d(TAG, "launchUploader");
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
                mFileUri = data.getData();//IntentからURIの取り出し。→
                if (mFileUri != null) {
                    uploadFromUri(mFileUri);//URI
                } else {
                    Log.w(TAG, "File URI is null");
                }
            } else {
                Toast.makeText(getActivity(), "Taking picture failed.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void uploadFromUri(Uri fileUri) {
        Log.d(TAG, "uploadFromUri:src:" + fileUri.toString());
        showProgressBar(getString(R.string.progress_uploading));
        mFileUri = fileUri;//ファイルパス保存
        photoRef = mStorageRef.child("photos").child(fileUri.getLastPathSegment());
        photoRef.putFile(fileUri).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getActivity(), R.string.upload_fail, Toast.LENGTH_SHORT).show();
                hideProgressBar();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(getActivity(), R.string.upload_success, Toast.LENGTH_SHORT).show();
                downloadFromPath();
                hideProgressBar();
            }

        });
    }

    private void downloadFromPath() {
        GlideApp.with(this).load(photoRef).into(mBinding.btnGroupImage);
        Log.i("check22", String.valueOf(photoRef));
        Log.i("check23", String.valueOf(photoRef.getDownloadUrl()));

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

