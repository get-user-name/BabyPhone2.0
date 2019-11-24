package com.mraon.babyphone2;

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class myWebClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

        Uri uri = Uri.parse(view.getUrl());

        /*
        if(uri.getPath().contains("/test/page")){

            Intent intent = new Intent(MainActivity.this, TestActivity.class); // 새창을 여는 액티비티나, 팝업일때 이용하면 용이합니다.
            intent.putExtra("url",view.getUrl());
            startActivity(intent);

            return true;
        }

         */

        return super.shouldOverrideUrlLoading(view, request);

    }

}
