package com.instantmusic.appmovil.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.instantmusic.appmovil.server.connect.JSONConnection;
import com.instantmusic.appmovil.R;
import com.instantmusic.appmovil.server.connect.Utils;
import com.instantmusic.appmovil.server.remoteServer;
import com.instantmusic.appmovil.server.serverInterface;

import org.json.JSONObject;

/**
 * Actividad principal. Muestra el listado de notas.
 */
public class MusicApp extends AppCompatActivity {
    private static int ACTIVITY_CREATE = 0;
    private EditText mail;
    private EditText pass;
    private TextView mailTip;
    private TextView  passTip;
    public String email;
    public serverInterface server;
    public serverInterface local;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app);
        server = new remoteServer();
        mail = findViewById(R.id.email);
        email = mail.getText().toString();
        mail.setTextColor(Color.WHITE);
        Button confirmButton = findViewById(R.id.register);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                registInScreen();
            }
        });

        Button confirmButton2 = findViewById(R.id.forget);
        Button confirmButton3 = findViewById(R.id.accept);
        confirmButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                boolean seguir = true;
                mail = findViewById(R.id.email);
                pass = findViewById(R.id.password);
                mailTip = findViewById(R.id.emailLogin);
                passTip = findViewById(R.id.Tip);
                String userEmail = mail.getText().toString();
                String password = pass.getText().toString();
                String texto;

                if ( userEmail.isEmpty() ) {
                    texto = "Email/User is empty";
                    mailTip.setText(texto);
                    mailTip.setTextColor(Color.RED);
                    mailTip.setVisibility(View.VISIBLE);
                    seguir = false;
                }
                else {
                    texto = "";
                    mailTip.setText(texto);
                }

                if ( password.isEmpty() ) {
                    texto = "Password is empty";
                    passTip.setText(texto);
                    passTip.setTextColor(Color.RED);
                    passTip.setVisibility(View.VISIBLE);
                    seguir = false;
                }
                else {
                    texto = "";
                    passTip.setText(texto);
                }

                if ( seguir ) {
                    server.login(mail.getText().toString(), pass.getText().toString(), new JSONConnection.Listener() {
                        @Override
                        public void onValidResponse(int responseCode, JSONObject data) {
                            if(responseCode == 200) {
                                logInScreen();
                            }
                            else{
                                String error = Utils.listifyErrors(data);
                                if ( error.equals("Unable to log in with provided credentials.")) {
                                    String texto = "Your email and password combination does not match an InstantMusic account. Please try again";
                                    passTip=findViewById(R.id.Tip);
                                    passTip.setText(texto);
                                    passTip.setTextColor(Color.RED);
                                    passTip.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                        @Override
                        public void onErrorResponse(Throwable throwable) {
                            Toast.makeText(getBaseContext(), throwable.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void logInScreen() {
        Intent i = new Intent(this, Login.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    private void registInScreen() {
        Intent i = new Intent(this, Regist.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

    }
}