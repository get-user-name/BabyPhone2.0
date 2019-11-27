package com.mraon.babyphone2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private WebView webView;
    private WebSettings webViewSetting;
    private String webUrlLocal = "http://3.230.157.235";
    private String testURL = "https://m.naver.com";

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = getIntent();

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


                        GetCodeTask getCodeTask =
                                new GetCodeTask(
                                        token,
                                        intent.getExtras().getString("ip1"),
                                        intent.getExtras().getString("ip2"),
                                        intent.getExtras().getString("ip3"),
                                        intent.getExtras().getString("ip4"));
                        getCodeTask.execute();

                    }
                });

        //-------------------------------------------------------------------

        webView = findViewById(R.id.myWebview);

        webViewSetting = webView.getSettings();
        webViewSetting.setJavaScriptEnabled(true);
        webViewSetting.setLoadWithOverviewMode(true);
        webView.setWebViewClient(new myWebClient());

        webView.loadUrl(webUrlLocal);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode== KeyEvent.KEYCODE_BACK){
            if(webView.canGoBack()){
                webView.goBack();
                return false;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    class GetCodeTask extends AsyncTask<Void,Void,String>
    {

        private String token;
        private String ip1;
        private String ip2;
        private String ip3;
        private String ip4;

        public GetCodeTask(String _token, String _ip1, String _ip2, String _ip3, String _ip4){
            this.token = _token;
            this.ip1 = _ip1;
            this.ip2 = _ip2;
            this.ip3 = _ip3;
            this.ip4 = _ip4;
        }

        protected String doInBackground(Void... params) {

            String code = null;

            try{
                URL url = new URL(String.format("http://3.230.157.235/registIP?token=%s&ip1=%s&ip2=%s&ip3=%s&ip4=%s",token,ip1,ip2,ip3,ip4));
                Log.e("URL", url.toString());

                HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                InputStream inputStream = connection.getInputStream();

                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line;
                while ((line = reader.readLine())!=null){
                    builder.append(line);
                }

                code = builder.toString();


            } catch (Exception e) {
                Log.e("REST_API", "GET method failed: " + e.getMessage());
                e.printStackTrace();
            }

            return code;
        }


        @Override
        protected void onPostExecute(String code) {
            // dismiss progress dialog and update ui
            TextView textView = findViewById(R.id.code);
            textView.setText(code);
        }
    }


}
