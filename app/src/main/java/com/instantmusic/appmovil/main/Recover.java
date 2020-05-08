package com.instantmusic.appmovil.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.instantmusic.appmovil.R;
import com.instantmusic.appmovil.server.connect.JSONConnection;
import com.instantmusic.appmovil.server.remoteServer;
import com.instantmusic.appmovil.server.serverInterface;
import org.json.JSONException;
import org.json.JSONObject;

public class Recover extends AppCompatActivity {

    EditText emailRecover;
    public serverInterface server;


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        server=new remoteServer();

        setContentView(R.layout.activity_instant_music_app_recover);

        Button confirmButton2 = findViewById(R.id.backButton1);
        confirmButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

        emailRecover = findViewById(R.id.emailRecover);

        Button enviar; enviar = findViewById(R.id.create2);
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmRecover();
            }
        });
    }

    private void confirmRecover() {
        String email = emailRecover.getText().toString();
        server.recoverPassword(email, new JSONConnection.Listener() {
            @Override
            public void onValidResponse(int responseCode, JSONObject data) {
                if ( responseCode == 200 ) {
                    Toast.makeText(Recover.this, R.string.email_sent, Toast.LENGTH_SHORT).show();
                }
                else if ( responseCode == 400 ) {
                    Toast.makeText(Recover.this, R.string.email_wrong, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onErrorResponse(Throwable throwable) {
                Toast.makeText(Recover.this, throwable.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void back() {
        Intent i = new Intent();
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        setResult(RESULT_OK, i);
        finish();
    }
}
