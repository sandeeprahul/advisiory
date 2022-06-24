package com.example.advisoryservice.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.advisoryservice.R;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

import static android.os.Build.VERSION_CODES.R;

public class TestingModule extends AppCompatActivity implements
        EasyPermissions.PermissionCallbacks{

    WebView myWebView;

    private String TAG = "TestingModule";
    private PermissionRequest mPermissionRequest;

    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final String[] PERM_CAMERA =
            {Manifest.permission.CAMERA};
    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(TestingModule.this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA},
                    50); }
        setContentView(com.example.advisoryservice.R.layout.activity_testmakeup);
        myWebView = (WebView) findViewById(com.example.advisoryservice.R.id.webview);

//        myWebView  = new WebView(this);

        myWebView.setWebContentsDebuggingEnabled(true);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        myWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        myWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        myWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        myWebView.getSettings().setDomStorageEnabled(true);
        myWebView.getSettings().setBuiltInZoomControls(true);
        myWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        myWebView.getSettings().setAllowFileAccess(true);
        myWebView.getSettings().setSupportZoom(true);
        myWebView.getSettings().setLoadWithOverviewMode(true);
//        myWebView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        // Enable pinch to zoom without the zoom buttons
        // AppRTC requires third party cookies to work
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(myWebView, true);
        }
        // Allow use of Local Storage
        myWebView.getSettings().setDomStorageEnabled(true);
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // open in Webview
                // open rest of URLS in default browser
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }
        });
        //myWebView.setWebViewClient(new WebViewClient());
        myWebView.setWebChromeClient(new WebChromeClient() {
            // Grant permissions for cam
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                Log.e("onPermissionReq", "onPermissionRequest");
                mPermissionRequest = request;
//                request.grant(request.getResources());
                final String[] requestedResources = request.getResources();
                for (String r : requestedResources) {
                    if (r.equals(PermissionRequest.RESOURCE_VIDEO_CAPTURE)) {
                        // In this sample, we only accept video capture request.
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TestingModule.this)
                                .setTitle("Allow Permission to camera")
                                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        mPermissionRequest.grant(new String[]{PermissionRequest.RESOURCE_VIDEO_CAPTURE});
                                        Log.d(TAG,"Granted");
                                    }
                                })
                                .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        mPermissionRequest.deny();
                                        Log.d(TAG,"Denied");
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                        break;
                    }
                }
            }

            @Override
            public void onPermissionRequestCanceled(PermissionRequest request) {
                super.onPermissionRequestCanceled(request);
                Toast.makeText(TestingModule.this,"Permission Denied",Toast.LENGTH_SHORT).show();
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                //todo
            }
        });

        String html = "<iframe id=\"facesdk\" frameBorder=\"0\" allow=\"camera\" src=\"https://apiservices1.healthandglowonline.co.in/iframe/form.aspx?cms-client-id=76&watermark=false&features=image-slider-photo&carousel=true&filter=true&license=dbigwXniB-Ug1fLiGkKljMaC06P73eobRwQFbjNiZJPT-_IimbFEPYJbimYTgJa9D5MrDLAPnrL7o7UvtNwPfVh0aspslila4nQ8pEMiipApnkcqxvQ0xmRL1Lu5ermHmMejP78GJ_WTt5ASf5wVtrrNpAEaIvPUms-yjajA41WYaM1-9mtDTJjYLmNIphWjDckXaFyMvllRsgYB8taA98qjr40_r4YPlykMpYwCdNL0z3EB4odl2x1F--M8YiaZukh0Gb43IKzwLfoxqOB_lx23dFFXeRYuLLEEWBnlImwPibp6U_E-68ij873FdyobHHrpFMhSwb2YjEYgV1-j164dHEaxtHYdPg7QGW5w8Ti-0JkGbBMS6CrdTToUTXevjHkbSi6EGRHXD_mqn2Wa0FD1Ql8qMRRs4YOyA05Uk3oJJDQ_ahDXOzDmQgoXfiXbeue2TYK1yzkfSBxvCIsYrICrsg5wIdgs3bIuUlWKuwyE_gUgPwj-qiiiX1Vo7ITQnzuOs85QqqZvV_KqmIrr-XJ5tjxK_DLj5THATn0wywnp4lZELT3k78I6-_3rg8JD3di-7GSS0RlImxZliLhhqEz6fsfofCSOP7mqJwXsdRD76sPV33wAC3GtKhxfD1ls8zOFmFeTO0Ph19z0oB3rtVkSFSQgYQhDnxsOonW1DjeJhzd9QVMrrH9XXphFXcjQ-oXu9StUIKNswx3HuWsUsmeYkFlneM6dhAnnVXzN\"></iframe>";
        String html_ = "<!DOCTYPE html><html> <head> <meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"target-densitydpi=high-dpi\" /> <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"> <link rel=\"stylesheet\" media=\"screen and (-webkit-device-pixel-ratio:1.5)\" href=\"hdpi.css\" /></head> <body style=\"background:black;margin:0 0 0 0; padding:0 0 0 0;\"> <iframe id=\"facesdk\" frameBorder=\"0\" style=\"width: 300;height: 400;\" allow=\"camera\" src=\"https://apiservices1.healthandglowonline.co.in/iframe/form.aspx\"</iframe> </body> </html> ";


        if(hasCameraPermission()){
            myWebView.loadUrl("https://apiservices1.healthandglowonline.co.in/iframe/form.aspx");
//            myWebView.loadUrl("https://meet.google.com/eya-wcxt-kyy");
//            setContentView(myWebView);
        }else{
            //This app needs access to your camera so you can take pictures
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs access to your camera so you can take pictures.",
                    REQUEST_CAMERA_PERMISSION,
                    PERM_CAMERA);
        }

    }




    private boolean hasCameraPermission() {
        return EasyPermissions.hasPermissions(TestingModule.this, PERM_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(TestingModule.this,"onPermissionsGranted",Toast.LENGTH_SHORT).show();
        Log.e("onPermissionsGranted", "onPermissionsGranted");

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(TestingModule.this,"onPermissionsDenied",Toast.LENGTH_SHORT).show();
        Log.e("onPermissionsDenied", "onPermissionsDenied");

    }



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        Toast.makeText(TestingModule.this,"onPointerCaptureChanged",Toast.LENGTH_SHORT).show();
        Log.e("onPointerCaptureChanged", "onPointerCaptureChanged");

    }
}
