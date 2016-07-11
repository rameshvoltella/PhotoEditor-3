package com.example.olga.photoeditor.mvp.presenter;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.olga.photoeditor.db.FriendDataSource;
import com.example.olga.photoeditor.models.vkfriends.Friend;
import com.example.olga.photoeditor.models.vkfriends.FriendListRequest;
import com.example.olga.photoeditor.mvp.view.FriendListView;
import com.example.olga.photoeditor.network.RetrofitService;
import com.example.olga.photoeditor.network.VkFriendsApi;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Date: 05.07.16
 * Time: 19:03
 *
 * @author Olga
 */

@InjectViewState
public class FriendListPresenter extends MvpPresenter<FriendListView> {

    private FriendDataSource mFriendDataSource;
    private Context mContext;
    Subscription mSubscription;

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().showProgress();
    }

    public void userLoadFriends(Context context) {
        mContext = context;
        mFriendDataSource = new FriendDataSource(mContext);
        mFriendDataSource.deleteAllFriends();
        mSubscription = getFriendsList()
                .timeout(1, TimeUnit.SECONDS)
                .retry(2)
                .subscribeOn(Schedulers.io())
                .map(friendListRequest -> friendListRequest.getResponse().getFriends())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Friend>>() {
                    @Override
                    public void onCompleted() {
                        getViewState().hideProgress();
                        getViewState().showFriendList();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().hideProgress();
                        getViewState().showError();
                    }

                    @Override
                    public void onNext(List<Friend> friends) {
                        for (int i = 0; i < friends.size(); i++) {
                            mFriendDataSource.saveFriend(friends.get(i));
                        }
                        if ((friends.isEmpty())) {
                            getViewState().hideError();
                            getViewState().showEmpty();
                        } else {
                            getViewState().hideEmpty();
                            getViewState().hideError();
                            getViewState().setData(friends);
                        }
                    }
                });

    }

    public void userRemoveFriend(Friend friend) {
        mFriendDataSource.deleteFriend(friend);
    }

    public void userCheckFriend() {
        //click on item RecyclerView??
    }

    public void userClickFindFriend(String name) {
        getViewState().setData(mFriendDataSource.findFriends(name));
    }

    public void userClickOk() {
        //send vkApi
    }

    public void userClickCancel() {
        if (!mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        //Back
    }

    public void userLeaveScreen() {
        if (!mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    private Observable<FriendListRequest> getFriendsList() {

        return RetrofitService.getInstance(mContext)
                .createApiService(VkFriendsApi.class)
                .getFriends(4283833, "random", "photo_100,online", "5.52");
    }


}
