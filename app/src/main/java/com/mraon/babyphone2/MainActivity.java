package com.mraon.babyphone2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private WebView webView;
    private WebSettings webViewSetting;
    private String webUrlLocal = "http://3.230.157.235";
    private String testURL = "www.google.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });



        //-------------------------------------------------------------------

        webView = findViewById(R.id.myWebview);

        webViewSetting = webView.getSettings();
        webViewSetting.setJavaScriptEnabled(true);
        webViewSetting.setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient(){});

        webView.loadUrl(testURL);

    }
}
