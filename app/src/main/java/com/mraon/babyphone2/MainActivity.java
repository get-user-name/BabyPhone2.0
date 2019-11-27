package com.mraon.babyphone2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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


                        GetCodeTask getCodeTask = new GetCodeTask(token);
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

        public GetCodeTask(String _token){
            this.token = _token;
        }

        protected String doInBackground(Void... params) {

            String code = null;

            try{
                URL url = new URL("http://3.230.157.235/storeToken?token="+token);
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
