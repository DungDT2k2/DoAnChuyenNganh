package com.example.appbanhang.retrofit;



import com.example.appbanhang.model.NotiResponse;
import com.example.appbanhang.model.NotiSendData;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiPushNotification {
    @Headers(
            {
                    "Content-Type: application/json",
                    "Authorization: key=AAAAVanwWhQ:APA91bHHT3py8-CVK94HBwExFXdZzL8AzxzpZrCiU89Mhd-XO7LBBkRomBOgiQvLPyRjTlnMxrnKSoJV7JZBdXvr3duC04GUxAUek9HSPmn7kqnrKOaj3aYb1k3fmECTa9nxBu_425R3"

            }
    )
    @POST("fcm/send")
    Observable<NotiResponse> sendNotification (@Body NotiSendData data);

}
