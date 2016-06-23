package com.example.olga.photoeditor.network;

import android.content.Context;

import com.example.photoeditor.R;

/**
 * Date: 23.06.16
 * Time: 09:05
 *
 * @author Olga
 */
public final class VkUrls {
    static final String API = "https://api.vk.com/method";

    public  static class Friends {
        public static  final  String GET_FRIENDS = API + "/friends.get";
    }

    public static String  getApiBaseUrl(Context context){
        return context.getString(R.string.base_api_url);
    }
}
