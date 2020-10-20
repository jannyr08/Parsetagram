package com.example.instagramclone;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication  extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        // Register your parse models
        ParseObject.registerSubclass(Post.class);


        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("Y8BzaldcTVMgBvaBHhrWNsMWDR2SlkE18B9GJI4Y")
                .clientKey("yE1R0qiWxR6Mi2dlpQsZYvDrkC08DjV6hOHoMYnk")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
