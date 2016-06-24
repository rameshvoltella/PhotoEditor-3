package com.example.olga.photoeditor;

import android.content.Context;
import android.os.AsyncTask;

import com.example.olga.photoeditor.models.vkfriends.Friend;
import com.example.olga.photoeditor.models.vkfriends.FriendListRequest;
import com.example.olga.photoeditor.network.RetrofitService;
import com.example.olga.photoeditor.network.VkFriendsApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Date: 22.06.16
 * Time: 22:02
 *
 * @author Olga
 */
public class FriendsListAsyncTask extends AsyncTask<Void, Integer, List<Friend>> {
    private FriendsListener mFriendsListener;
    private List<Friend> mFriends = new ArrayList<>();
    private Call<FriendListRequest> mFriendListRequest;

    public FriendsListAsyncTask(Context context, FriendsListener friendsListener) {
        mFriendsListener = friendsListener;
        mFriendListRequest = RetrofitService.getInstance(context)
                .createApiService(VkFriendsApi.class)
                .getFriends(4283833, "photo_100,online", "5.52");
    }

    public void setFriendsListener(FriendsListener friendsListener) {
        mFriendsListener = friendsListener;

        switch (getStatus()) {
            case PENDING:
                break;

            case RUNNING:
                mFriendsListener.startProgress();
                break;

            case FINISHED:
                mFriendsListener.stopProgress();
                mFriendsListener.setResult(mFriends);
                break;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mFriendsListener.startProgress();
    }

    @Override
    protected List<Friend> doInBackground(Void... voids) {

        publishProgress(0);

        try {
            Response<FriendListRequest> friendsResponse = mFriendListRequest.execute();
            if (friendsResponse.isSuccessful()) {
                mFriends = friendsResponse.body().getResponse().getFriends();
                int currentProgress = 0;

                for (int i = 0; i < mFriends.size(); i++) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(30);
                        publishProgress(currentProgress);
                        currentProgress += 100 / mFriends.size();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    publishProgress(currentProgress);
                }

                return friendsResponse.body().getResponse().getFriends();

            } else {
                mFriendsListener.failedProgress();
                return Collections.emptyList();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        mFriendsListener.updateProgress(values[0]);
    }

    @Override
    protected void onPostExecute(List<Friend> friends) {
        super.onPostExecute(friends);
        mFriends = friends;
        mFriendsListener.stopProgress();
        mFriendsListener.setResult(friends);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        mFriendsListener.stopProgress();
        mFriendsListener.setResult(Collections.<Friend>emptyList());
    }

    public interface FriendsListener {
        void startProgress();

        void stopProgress();

        void failedProgress();

        void updateProgress(int progress);

        void setResult(List<Friend> friends);
    }
}
