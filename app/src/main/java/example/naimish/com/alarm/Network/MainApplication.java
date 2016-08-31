package example.naimish.com.alarm.Network;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by kanishk on 6/4/16.
 */
public class MainApplication extends Application {

    private static MainApplication instance;

    public void onCreate()
    {
        super.onCreate();
        printHashKey();
        instance=this;
    }

    public void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "example.naimish.com.alarm",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException ignored) {
            ignored.printStackTrace();
        }
    }

    public static MainApplication getInstance(){
        return instance;
    }

    public static Context getAppContext()
    {
        return instance.getApplicationContext();
    }
}
