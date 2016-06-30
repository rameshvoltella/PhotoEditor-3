package com.example.olga.photoeditor.models.vkfriends;

import com.google.gson.annotations.SerializedName;

/**
 * Date: 22.06.16
 * Time: 19:58
 *
 * @author Olga
 */
public class FriendListRequest {

    @SerializedName("response")
    private Response mResponse;

    public Response getResponse() {
        return mResponse;
    }

    public void setResponse(Response response) {
        mResponse = response;
    }
}
