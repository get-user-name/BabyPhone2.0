package com.mraon.babyphone2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class IpRegistActivity extends AppCompatActivity {

    EditText ip1;
    EditText ip2;
    EditText ip3;
    EditText ip4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_regist);

        ip1 = findViewById(R.id.ip1);
        ip2 = findViewById(R.id.ip2);
        ip3 = findViewById(R.id.ip3);
        ip4 = findViewById(R.id.ip4);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(IpRegistActivity.this, MainActivity.class);
                intent.putExtra("ip1", ip1.getText().toString());
                intent.putExtra("ip2", ip2.getText().toString());
                intent.putExtra("ip3", ip3.getText().toString());
                intent.putExtra("ip4", ip4.getText().toString());
                startActivity(intent);

            }
        });

    }
}
