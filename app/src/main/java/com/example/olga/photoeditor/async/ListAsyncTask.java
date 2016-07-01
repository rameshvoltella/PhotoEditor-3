package com.example.olga.photoeditor.async;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Date: 30.06.16
 * Time: 19:44
 *
 * @author Olga
 */
public abstract class ListAsyncTask<Data> extends AsyncTask<Void, Integer, Data> {
    protected final Context mContext;
    private Listener<Data> mListener;
    private Data mList;

    public ListAsyncTask(Context context, Listener<Data> listener) {
        mContext = context.getApplicationContext();
        mListener = listener;
    }

    public void setListener(Listener<Data> listener) {
        mListener = listener;

        if (mListener != null) {
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
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mListener != null) mListener.startProgress();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (mListener != null) mListener.updateProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Data data) {
        super.onPostExecute(data);
        mList = data;
        if (mListener != null) {
            mListener.stopProgress();
            mListener.setResult(data);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if (mListener != null) mListener.stopProgress();
    }

    public interface Listener<T> {
        void startProgress();

        void stopProgress();

        void failedProgress();

        void updateProgress(int progress);

        void setResult(T data);
    }

    public void setList(Data data) {
        mList = data;
    }
}

