package com.iw.thieftrackerv3;

import android.app.Application;
import com.iw.thieftrackerv3.Config.TypeFaceUtil;

public class ThiefTracker extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        TypeFaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Lato-Regular.ttf");
    }
}
