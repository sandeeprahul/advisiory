package com.example.advisoryservice.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Window;
import android.view.WindowManager;

import com.example.advisoryservice.R;
import com.example.advisoryservice.base.BaseActivity;
import com.example.advisoryservice.ui.advisoryserviceselection.AdvisoryServiceSelection;
import com.example.advisoryservice.ui.login.LoginActivity;
import com.example.advisoryservice.ui.main.MainActivity;
import com.example.advisoryservice.util.Constants;
import com.example.advisoryservice.util.Log;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SplashScreenActivity extends BaseActivity {

    public static final String TAG = SplashScreenActivity.class.getSimpleName();

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spe;

    boolean isLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        TelephonyManager manager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (Objects.requireNonNull(manager).getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            // Toast.makeText(this, "Detected... You're using a Tablet", Toast.LENGTH_SHORT).show();
            spe = getSharedPreferences().edit();
            spe.putBoolean(Constants.TABLET, true);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            //Toast.makeText(this, "Detected... You're using a Mobile Phone", Toast.LENGTH_SHORT).show();
            spe = getSharedPreferences().edit();
            spe.putBoolean(Constants.TABLET, false);
        }
        spe.apply();
        /*
        if (!isTablet())
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);*/

        setContentView(R.layout.activity_splash_screen);
        sharedPreferences = getSharedPreferences(Constants.SP_PREF, Context.MODE_PRIVATE);

        isLoggedIn = getSharedPreferences().getBoolean(Constants.LOGGED_IN, false);
      //  Log.d(TAG, "Is Already logged IN <<::>> " + isLoggedIn);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#3c3c3c"));
        }

        Observable.timer(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    Log.d(TAG, "Starting main activity");

                    if (!isLoggedIn) {

//                        Intent intent = new Intent(this, TestingModule.class);
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
//                        Intent intent = new Intent(this, TestingModule.class);
                        Intent intent = new Intent(this, AdvisoryServiceSelection.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }
}
