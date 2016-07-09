package com.example.olga.photoeditor.network;

import com.example.olga.photoeditor.models.vkfriends.FriendListRequest;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Date: 23.06.16
 * Time: 09:05
 *
 * @author Olga
 */
public interface VkFriendsApi {
    @GET(VkUrls.Friends.GET_FRIENDS)
    Observable<FriendListRequest> getFriends(@Query("user_id") int userId,
                                             @Query("order") String order,
                                             @Query("fields") String fields,
                                             @Query("v") String version);
}
