package com.coding.pixel.chatrooms;

import com.coding.pixel.chatrooms.Notification.MyResponse;
import com.coding.pixel.chatrooms.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAiOZZCp4:APA91bGjvquRyFKV6dosaLNQOx1s5rswLW-MM8RwPCuML0ciXPjGd7ABCa9Cg4JBKXn-81Jjb91W3jwLNYDt0-BqPB7TKAsGtR4VjwVrbhyMeii5UnyLUjDiN5xFB2AbfEJnuMlmR_FJ"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
