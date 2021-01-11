package com.websarva.wings.android.droidsoftsecond;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class s002_UploadService extends IntentService {

    private static final String TAG = "s002_UploadService";

    public static final String ACTION_UPLOAD = "action_upload";//大文字のStringはIntent間のキーとして使われている様子
    public static final String UPLOAD_COMPLETED = "upload_completed";
    public static final String UPLOAD_ERROR = "upload_error";

    public static final String EXTRA_FILE_URI = "extra_file_uri";
    public static final String EXTRA_DOWNLOAD_URL = "extra_download_url";

    private StorageReference mStorageRef;

    @Override
    public void onCreate() {
        super.onCreate();

        // [START get_storage_ref]
        mStorageRef = FirebaseStorage.getInstance().getReference();
        // [END get_storage_ref]
    }

    public s002_UploadService(String name, StorageReference mStorageRef) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (ACTION_UPLOAD.equals(intent.getAction())) {//f002でセットしたアップロードアクションとの照合
            Uri fileUri = intent.getParcelableExtra(EXTRA_FILE_URI);//ファイルURIの取り出し。
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//KITKAT以上のバージョンか確認している。なぜ？
                getContentResolver().takePersistableUriPermission(fileUri, intent.FLAG_GRANT_READ_URI_PERMISSION);//ContentResolverとは？
            }
            uploadFromUri(fileUri);
        }

    }

    private void uploadFromUri(final Uri fileUri){//同名のメソッドがあるが、privateであるため問題なし。
        Log.d(TAG, "uploadFromUri:src:" + fileUri.toString());

        final StorageReference photoRef = mStorageRef.child("photos").child(fileUri.getLastPathSegment());
        UploadTask uploadTask = photoRef.putFile(fileUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });
    }
}


