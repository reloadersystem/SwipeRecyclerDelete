package com.example.silvia.swiperecycler.Helper;

import com.example.silvia.swiperecycler.Model.IMenuRequest;
import com.example.silvia.swiperecycler.Remote.RetrofitClient;

public class Common {

    public static IMenuRequest getMenuRequest()
    {
        return RetrofitClient.getClient("https://api.androidhive.info/").create(IMenuRequest.class);

    }


}
