package com.example.olga.photoeditor.async;

import android.content.Context;

import com.example.olga.photoeditor.db.FriendDataSource;
import com.example.olga.photoeditor.models.vkfriends.Friend;
import com.example.olga.photoeditor.models.vkfriends.FriendListRequest;
import com.example.olga.photoeditor.network.RetrofitService;
import com.example.olga.photoeditor.network.VkFriendsApi;

import java.io.IOException;
import java.io.Serializable;
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
public class FriendsListAsyncTask extends ListAsyncTask<List<Friend>> implements Serializable {
    private final Context mContext;
    private Listener<List<Friend>> mFriendsListener;
    private List<Friend> mFriends = new ArrayList<>();
    private Call<FriendListRequest> mFriendListRequest;

    public FriendsListAsyncTask(Context context, Listener<List<Friend>> friendsListener, int count) {
        super(context, friendsListener);
        mContext = context.getApplicationContext();
        mFriendsListener = friendsListener;
        mFriendListRequest = RetrofitService.getInstance(mContext)
                .createApiService(VkFriendsApi.class)
                .getFriends(4283833, "random", count, "photo_100,online", "5.52");
    }

    public FriendsListAsyncTask(Context context, Listener<List<Friend>> friendsListener) {
        super(context, friendsListener);
        mContext = context.getApplicationContext();
        mFriendsListener = friendsListener;
        FriendDataSource dataSource = new FriendDataSource(mContext);
        mFriends = dataSource.getAllFriends();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Friend> doInBackground(Void... voids) {
        publishProgress(0);
        if (mFriendListRequest != null) {
            try {
                Response<FriendListRequest> friendsResponse = mFriendListRequest.execute();

                if (friendsResponse.isSuccessful())
                    mFriends = friendsResponse.body().getResponse().getFriends();

                else {
                    mFriendsListener.failedProgress();
                    return Collections.emptyList();
                }

            } catch (IOException e) {
                e.printStackTrace();
                return Collections.emptyList();
            }
        }

        int currentProgress = 0;
        for (int i = 0; i < mFriends.size(); i++) {
            try {
                TimeUnit.MILLISECONDS.sleep(200);
                publishProgress(currentProgress);
                currentProgress += 100 / mFriends.size();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return Collections.emptyList();
            }
            publishProgress(currentProgress);
        }
        return mFriends;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(List<Friend> friends) {
        super.onPostExecute(friends);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    public void setFriends(List<Friend> friends) {
        mFriends = friends;
    }
}
