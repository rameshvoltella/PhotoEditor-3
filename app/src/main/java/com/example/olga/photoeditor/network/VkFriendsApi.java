package com.example.olga.photoeditor.network;

import com.example.olga.photoeditor.models.vkfriends.FriendListRequest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Date: 23.06.16
 * Time: 09:05
 *
 * @author Olga
 */
public interface VkFriendsApi {
    @GET(VkUrls.Friends.GET_FRIENDS)
    Call<FriendListRequest> getFriends(@Query("user_id") int UserId,
                                       @Query("fields") String fields,
                                       @Query("v") String version);
}
