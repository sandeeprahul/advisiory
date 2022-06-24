package com.example.advisoryservice.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.example.advisoryservice.R;
import com.example.advisoryservice.util.Constants;

import java.io.File;
import java.util.Objects;



public abstract class BaseActivity extends AppCompatActivity {

    public static final String TAG = BaseActivity.class.getSimpleName();
    CircularProgressDrawable circularProgressDrawable;

    public void setActionBarTitle(String message) {
        // Set up your ActionBar
        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.action_bar_simple, null);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(actionBarLayout);
            actionBar.setTitle("");
            final int actionBarColor = getResources().getColor(R.color.white);
            actionBar.setBackgroundDrawable(new ColorDrawable(actionBarColor));
        }
        ImageView ivBack = findViewById(R.id.ivBack);
        ImageView ivLogout = findViewById(R.id.ivLogout);
        ImageView ivLogo = findViewById(R.id.ivLogo);
        ivLogo.setVisibility(View.VISIBLE);
        ivLogout.setVisibility(View.GONE);
        ivBack.setOnClickListener(view -> onBackPressed());

        SharedPreferences sp = getSharedPreferences();
        Log.d(TAG, "Logo Url " + sp.getString(Constants.LOGO_URL, ""));
        if (!sp.getString(Constants.LOGO_URL, "").isEmpty())
            Glide.with(this).load(sp.getString(Constants.LOGO_URL, "")).into(ivLogo);

        TextView title = findViewById(R.id.ab_activity_title);
        title.setText(message);
        title.setVisibility(View.VISIBLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#3c3c3c"));
        }
    }

    public void setActionBarTitle(String message, boolean logout) {
        // Set up your ActionBar
        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.action_bar_simple, null);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(actionBarLayout);
            actionBar.setTitle("");
            final int actionBarColor = getResources().getColor(R.color.white);
            actionBar.setBackgroundDrawable(new ColorDrawable(actionBarColor));
        }
        ImageView ivBack = findViewById(R.id.ivBack);
        ImageView ivLogout = findViewById(R.id.ivLogout);
        ivBack.setOnClickListener(view -> onBackPressed());

        ivLogout.setVisibility(logout ? View.VISIBLE : View.GONE);

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor spe = getSharedPreferences().edit();
                spe.clear();
                spe.apply();

                TelephonyManager manager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                if (Objects.requireNonNull(manager).getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
                    spe = getSharedPreferences().edit();
                    spe.putBoolean(Constants.TABLET, true);
                } else {
                    spe = getSharedPreferences().edit();
                    spe.putBoolean(Constants.TABLET, false);
                }
                spe.apply();

               /* Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();*/
            }
        });

        TextView title = findViewById(R.id.ab_activity_title);
        title.setText(message);
        title.setVisibility(View.VISIBLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#3c3c3c"));
        }
    }

    public boolean isTablet() {
        try {
            // Compute screen size
            DisplayMetrics dm = getResources().getDisplayMetrics();
            float screenWidth = dm.widthPixels / dm.xdpi;
            float screenHeight = dm.heightPixels / dm.ydpi;
            double size = Math.sqrt(Math.pow(screenWidth, 2) +
                    Math.pow(screenHeight, 2));
            // Tablet devices should have a screen size greater than 6 inches
            return size >= 6;
        } catch (Throwable t) {
            Log.e(TAG, "Failed to compute screen size", t);
            return false;
        }
    }

    public CircularProgressDrawable getCircularProgressDrawable() {
        int[] COLORS = new int[]{
                getResources().getColor(R.color.colorAccent)
        };

        circularProgressDrawable = new CircularProgressDrawable(this);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.setColorSchemeColors(COLORS);
        circularProgressDrawable.start();

        return circularProgressDrawable;
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void expand(final View v) {

        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        if (isTablet())
            v.getLayoutParams().height = 2500;
        else
            v.getLayoutParams().height = 1000;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                Log.d(TAG, "interpolatedTime " + (int) interpolatedTime);
                Log.d(TAG, "v.getLayoutParams().height " + v.getLayoutParams().height);

                v.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public void deleteImage() {

        String filePath = getSharedPreferences().getString(Constants.FILE_URL, "");
       Log.d(TAG, "File Path to be deleted <<::>> " + filePath);

        if (!filePath.isEmpty()) {

            String filename = filePath.substring(filePath.lastIndexOf("/") + 1);
          Log.d(TAG, "File Name to be deleted <<::>> " + filename);

            String file_dj_path = Environment.getExternalStorageDirectory() + "/mBOSPictures/" + filename;
            File fdelete = new File(file_dj_path);
            if (fdelete.exists()) {
                if (fdelete.delete()) {

                    SharedPreferences.Editor spe = getSharedPreferences().edit();
                    spe.remove(Constants.FILE_URL);
                    spe.apply();

                   Log.e("-->", "file Deleted :" + file_dj_path);
                    callBroadCast();
                } else {
                   Log.e("-->", "file not Deleted :" + file_dj_path);
                }
            }
        }
    }

    public void deleteImage(String path, String value) {

        try {
            Log.d(TAG, "File Path <<::>> " + path);
            if (!path.isEmpty()) {

                String filename = path.substring(path.lastIndexOf("/") + 1);
               Log.d(TAG, "File Name to be deleted <<::>> " + filename);

                File fdelete = new File(path);
                if (fdelete.exists()) {
                    if (fdelete.delete()) {

                        SharedPreferences.Editor spe = getSharedPreferences().edit();
                        spe.remove(value);
                        spe.apply();

                     Log.e("-->", "file Deleted :" + path);
                        callBroadCast();
                    } else {
                       Log.e("-->", "file not Deleted :" + path);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, "Exception while deleting thumb nail image <<::>> " + ex);
        }
    }

    private void callBroadCast() {
       Log.e(TAG, "--> >= 14");
        MediaScannerConnection.scanFile(this, new String[]{Environment.getExternalStorageDirectory().toString()},
                null,

                new MediaScannerConnection.OnScanCompletedListener() {
                    /*
                     *   (non-Javadoc)
                     * @see android.media.MediaScannerConnection.OnScanCompletedListener#onScanCompleted(java.lang.String, android.net.Uri)
                     */
                    public void onScanCompleted(String path, Uri uri) {
                       Log.e("ExternalStorage", "Scanned " + path + ":");
                     Log.e("ExternalStorage", "-> uri=" + uri);
                    }
                });
    }

    public SharedPreferences getSharedPreferences() {
        return getSharedPreferences(Constants.SP_PREF, Context.MODE_PRIVATE);
    }
}
