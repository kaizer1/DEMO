package com.UHF.scanlable;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;


public final class AsynchronousGet {

    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.get("text/x-markdown; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient();
    private final String myString;
      AsynchronousGet() {
          myString = "https://beloson.ru/api/tags/push";
      }

    public void run() throws Exception {
        RequestBody requestBody = new RequestBody() {
            @Nullable
            @Override
            public MediaType contentType() {
               return MediaType.parse("application/json; charset=utf-8");
            }

            @Override
            public void writeTo(@NonNull BufferedSink bufferedSink) throws IOException {
                //bufferedSink.writeUtf8("Numbers\n");
                //bufferedSink.writeUtf8("-------\n");
                //bufferedSink.writeUtf8(" It's my life !!! \n");
                bufferedSink.writeUtf8(myString);
           /*     for (int i = 2; i <= 997; i++) {
                    bufferedSink.writeUtf8(String.format(" * %s = %s\n", i, factor(i)));
                }*/
            }



            public  final MediaType JSON
    = MediaType.parse("application/json; charset=utf-8");
        };

         // 158.160.162.155/
        Request request = new Request.Builder()
                //.url("http://losbalderdash.com/?baton=sdfsdf&id_dev=dsKOP4ffjjkh32dskdj&lanti=60.0023023&longi=59.930449&height=46&time_date=2024-19-09_21:19")  // ehi  //.url("http://loskutnikovgames.com/ehi") // was ehi                  .url("http://loswandernigs.com/ehi") // was ehi
                .url(myString)
                //.get()
                .post(requestBody)
                .build();

           client.newCall(request).enqueue(new Callback() {
               @Override
               public void onFailure(@NonNull Call call, @NonNull IOException e) {
                   Log.d("sdf", " eror this " + e.getLocalizedMessage());
               }

               @Override
               public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                    Log.d("dsf", " ok we are return " + response.toString());
               }
           });

     /*   try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);*/

          //   System.out.println(response.body().string());
        }


}
