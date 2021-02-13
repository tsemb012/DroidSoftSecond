package com.websarva.wings.android.droidsoftsecond.ui.adapter;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public abstract class FirestoreAdapter<VH extends RecyclerView.ViewHolder> //リサイクラービューのビューホルダーを継承しているVHを型パラメーターとして指定
        extends RecyclerView.Adapter<VH>
        implements EventListener<QuerySnapshot> {

    private static final String TAG = "FirestoreAdapter";
    private Query mQuery;
    private ListenerRegistration mRegistration;//リスナーを解除するためだけに使用する専用クラス。
    private ArrayList<DocumentSnapshot> mSnapshotsArray = new ArrayList<>();

    //①受け取ったクエリにしたがって、ドキュメント一覧を取得
    //②クエリに対してリスナーを設置し、変更があれば受け取れるように設定する。
    //③変更時OnEventを使い、実際の変更を受け取る。

    public FirestoreAdapter(Query query){//派生クラスからsuperを使って呼び出される。
        mQuery = query;
    }
    //アクティビティで設定したqueryをコンストラクターから受け取り使用。
    //ベースはここのコントラクターで得たクエリを中心として動く
        /*mQuery = mFirestore.collection("groups")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limit(LIMIT);*/

    //-----FireStoreリスナー開始
    public void startListening(){
        if(mQuery!=null && mRegistration == null){//条件：コンストラクト済　AND　リスナー未設定
            mRegistration = mQuery.addSnapshotListener(this);
        }
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

        //-----例外発生ログ
        if(error != null){
         Log.w(TAG,"onEvent:error",error);
         return;
        }

        //------変更発生時処理
        Log.d(TAG, "onEvent:numChanges:" + value.getDocumentChanges().size());//前回のスナップショットからどれだけデータ数が変化しているか出力。
        Log.i("Check:DocumentChange", value.getDocumentChanges().toString());//getDocumentChanges()は前回時から変化したドキュメントリストを取得する

        for (DocumentChange change: value.getDocumentChanges()){//getDocumentChangesは初回は全てのDocumentReferenceを出力し、その後変化したものだけを出力する。

            switch (change.getType()){
                case ADDED:
                    onDocumentAdded(change);
                    break;
                case MODIFIED:
                    onDocumentModified(change);
                    break;
                case REMOVED:
                    onDocumentRemoved(change);
                    break;
            }
        }
        onDataChanged();
    }

        protected void onDocumentAdded(DocumentChange change){
            mSnapshotsArray.add(change.getNewIndex(),change.getDocument());//TODO f003　内部構造を実際に動かして理解する。
            notifyItemInserted(change.getNewIndex());//TODO　f004 Observerとは何を指しているのだろうか。
            //change.getNewIndexは新しく追加//リサイクルビューワーのIndexとFireStoreのIndexがリンクしていれば、構造把握が簡単
        }

        protected void onDocumentModified(DocumentChange change){

            if (change.getOldIndex() == change.getNewIndex()){
                mSnapshotsArray.set(change.getOldIndex(),change.getDocument());
                notifyItemChanged(change.getOldIndex());
            }
            else {
                mSnapshotsArray.remove(change.getOldIndex());
                mSnapshotsArray.add(change.getNewIndex(), change.getDocument());
                notifyItemRemoved(change.getOldIndex());
            }
        }

        protected void onDocumentRemoved(DocumentChange change){
            mSnapshotsArray.remove(change.getOldIndex());
            notifyItemChanged(change.getOldIndex());
        }



    //-----FireStoreリスナー停止
    public void stopListening(){
        if(mRegistration!=null){
            mRegistration.remove();//リスナー停止
            mRegistration = null;
        }
        mSnapshotsArray.clear();//リストの内容削除
        notifyDataSetChanged();//データセットが変更されたことを、登録されているすべてのobserverに通知する。//TODO f002 分析
    }


    protected void onDataChanged() {}

    protected void onError(FirebaseFirestoreException e) {
        Log.w(TAG, "onError", e);
    };


    protected DocumentSnapshot getSnapshot (int index){
        return mSnapshotsArray.get(index);
    }

    public void setQuery(Query query){//おそらく新規でクエリが設定される時に動くのでは無いだろうか。

        stopListening();

        mSnapshotsArray.clear();//リストの内容削除
        notifyDataSetChanged();//登録されているObserverに変更があったことを知らせる処理

        mQuery = query;
        startListening();

    }

    @Override
    public int getItemCount() {
        return mSnapshotsArray.size();
    }
}
