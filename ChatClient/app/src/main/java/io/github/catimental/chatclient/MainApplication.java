package io.github.catimental.chatclient;

import android.app.Activity;
import android.app.Application;

public class MainApplication extends Application {
    public static MainApplication instance;

    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    private Activity mCurrentActivity = null;
    public Activity getCurrentActivity(){
        return mCurrentActivity;
    }
    public void setCurrentActivity(Activity mCurrentActivity){
        this.mCurrentActivity = mCurrentActivity;
    }

    public static MainApplication getInstance() {
        return instance;
    }
}
