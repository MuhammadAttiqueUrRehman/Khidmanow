package layout.utils;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

public class AppController extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocalManager.setLocale(base));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocalManager.setLocale(this);
    }
}
