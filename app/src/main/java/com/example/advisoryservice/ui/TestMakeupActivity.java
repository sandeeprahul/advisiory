package com.example.advisoryservice.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.advisoryservice.R;

public class TestMakeupActivity extends AppCompatActivity {

    ImageButton back_imgbtn;
    WebView webview;

    String url = "https://apiservices1.healthandglowonline.co.in/iframe/form.aspx";
    String html = "<iframe id=\"facesdk\" frameBorder=\"0\" allow=\"camera\" src=\"https://apiservices1.healthandglowonline.co.in/iframe/form.aspx?cms-client-id=76&watermark=false&features=image-slider-photo&carousel=true&filter=true&license=dbigwXniB-Ug1fLiGkKljMaC06P73eobRwQFbjNiZJPT-_IimbFEPYJbimYTgJa9D5MrDLAPnrL7o7UvtNwPfVh0aspslila4nQ8pEMiipApnkcqxvQ0xmRL1Lu5ermHmMejP78GJ_WTt5ASf5wVtrrNpAEaIvPUms-yjajA41WYaM1-9mtDTJjYLmNIphWjDckXaFyMvllRsgYB8taA98qjr40_r4YPlykMpYwCdNL0z3EB4odl2x1F--M8YiaZukh0Gb43IKzwLfoxqOB_lx23dFFXeRYuLLEEWBnlImwPibp6U_E-68ij873FdyobHHrpFMhSwb2YjEYgV1-j164dHEaxtHYdPg7QGW5w8Ti-0JkGbBMS6CrdTToUTXevjHkbSi6EGRHXD_mqn2Wa0FD1Ql8qMRRs4YOyA05Uk3oJJDQ_ahDXOzDmQgoXfiXbeue2TYK1yzkfSBxvCIsYrICrsg5wIdgs3bIuUlWKuwyE_gUgPwj-qiiiX1Vo7ITQnzuOs85QqqZvV_KqmIrr-XJ5tjxK_DLj5THATn0wywnp4lZELT3k78I6-_3rg8JD3di-7GSS0RlImxZliLhhqEz6fsfofCSOP7mqJwXsdRD76sPV33wAC3GtKhxfD1ls8zOFmFeTO0Ph19z0oB3rtVkSFSQgYQhDnxsOonW1DjeJhzd9QVMrrH9XXphFXcjQ-oXu9StUIKNswx3HuWsUsmeYkFlneM6dhAnnVXzN\"></iframe>";
    private MyWebViewClient myWebViewClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testmakeup);

        back_imgbtn = (ImageButton)findViewById(R.id.back_imgbtn);
        webview = (WebView) findViewById(R.id.webview);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;

        webview.setWebViewClient(myWebViewClient);

        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
//        webSettings.getMediaPlaybackRequiresUserGesture();
        webSettings.setMediaPlaybackRequiresUserGesture(false);
                String data_html = "<!DOCTYPE html><html> <head> <meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"target-densitydpi=high-dpi\" /> <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"> <link rel=\"stylesheet\" media=\"screen and (-webkit-device-pixel-ratio:1.5)\" href=\"hdpi.css\" /></head> <body style=\"background:black;margin:0 0 0 0; padding:0 0 0 0;\"> <iframe id=\"facesdk\" frameBorder=\"0\" style=\"width: 300;height: 400;\" allow=\"camera\" src=\"https://apiservices1.healthandglowonline.co.in/iframe/form.aspx\"</iframe> </body> </html> ";

        webview.loadUrl(url);


//        String data_html = "<!DOCTYPE html><html> <head> <meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"target-densitydpi=high-dpi\" /> <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"> <link rel=\"stylesheet\" media=\"screen and (-webkit-device-pixel-ratio:1.5)\" href=\"hdpi.css\" /></head> <body style=\"background:black;margin:0 0 0 0; padding:0 0 0 0;\"> <iframe id=\"facesdk\" frameBorder=\"0\" style=\"width: 300;height: 400;\" allow=\"camera\" src=\"https://apiservices1.healthandglowonline.co.in/iframe/form.aspx\"</iframe> </body> </html> ";
//
//        webview.setWebViewClient(new WebViewClient());
//        webview.setWebChromeClient(new WebChromeClient() {
//
//            @Override
//            public void onPermissionRequest(final PermissionRequest request) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////                    request.grant(request.getResources());
//                    runOnUiThread(() -> {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                            String[] PERMISSIONS = {
//                                    PermissionRequest.RESOURCE_AUDIO_CAPTURE,
//                                    PermissionRequest.RESOURCE_VIDEO_CAPTURE
//                            };
//                            request.grant(PERMISSIONS);
//                        }
//                    });
//                }
//            }
//
//        });
//
//        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        webview.getSettings().setAllowFileAccessFromFileURLs(true);
//        webview.getSettings().setAllowUniversalAccessFromFileURLs(true);
//        webview.getSettings().setJavaScriptEnabled(true);
//        webview.getSettings().setDomStorageEnabled(true);
//        webview.getSettings().setBuiltInZoomControls(true);
//        webview.getSettings().setAllowFileAccess(true);
//        webview.getSettings().setSupportZoom(true);
////        webview.loadData(html,"text/html", "utf-8");
//        webview.loadUrl("https://apiservices1.healthandglowonline.co.in/iframe/form.aspx");
//        back_imgbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });




    }
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if ("www.example.com".equals(request.getUrl().getHost())) {
                    // This is my website, so do not override; let my WebView load the page
                    return false;
                }
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, request.getUrl());
            startActivity(intent);
            return true;
        }
    }
}
