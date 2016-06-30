package com.example.olga.photoeditor.network;

import java.util.List;
import android.os.AsyncTask;
import com.example.olga.photoeditor.models.vkfriends.FriendListRequest;
import java.util.ArrayList;
import java.util.Collections;
import retrofit2.Call;

/**
 * Date: 30.06.16
 * Time: 19:44
 *
 * @author Olga
 */
public abstract class ListAsyncTask<T> extends AsyncTask<Void, Integer, List<T>> {
    private Listener mListener;
    private List<T> mList = new ArrayList<>();
    private Call<FriendListRequest> mFriendListRequest;

    public ListAsyncTask() {
        /**/
    }

    public void setListener(Listener listener) {
        mListener = listener;

        switch (getStatus()) {
            case PENDING:
                break;

            case RUNNING:
                mListener.startProgress();
                break;

            case FINISHED:
                mListener.stopProgress();
                mListener.setResult(mList);
                break;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mListener.startProgress();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (mListener != null) mListener.updateProgress(values[0]);
    }

    @Override
    protected void onPostExecute(List<T> list) {
        super.onPostExecute(list);
        mListener.stopProgress();
        mListener.setResult(list);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        mListener.stopProgress();
        mListener.setResult(Collections.<T>emptyList());
    }

    public interface Listener {
        void startProgress();

        void stopProgress();

        void failedProgress();

        void updateProgress(int progress);

        void setResult(List<T> list);
    }

    public void setList(List<T> list) {
        mList = list;
    }
}

