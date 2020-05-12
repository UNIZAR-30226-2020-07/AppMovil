package com.instantmusic.appmovil.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.instantmusic.appmovil.R;
import com.instantmusic.appmovil.friends.FriendsSearch;
import com.instantmusic.appmovil.podcast.PodcastSearch;
import com.instantmusic.appmovil.server.connect.JSONConnection;
import com.instantmusic.appmovil.server.remoteServer;
import com.instantmusic.appmovil.server.serverInterface;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingsPassword extends AppCompatActivity {
    private String mail;
    private String user;
    private serverInterface server;
    private String password;
    private String passwordConfirm;
    private EditText pass;
    private EditText passConfirm;
    private TextView passAux;
    private TextView passConfirmAux;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_settings2);
        server = new remoteServer();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = extras.getString("user");
            mail = extras.getString("mail");
        }
        Button confirmButton = findViewById(R.id.create);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                confirmSignUp();
            }
        });
        Button confirmButton2 = findViewById(R.id.backButton1);
        confirmButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void confirmSignUp() {
        boolean seguir = true;
        pass = findViewById(R.id.passwordSign);
        passConfirm = findViewById(R.id.passwordSign4);
        String texto;

        password = pass.getText().toString();
        passwordConfirm = passConfirm.getText().toString();

        passAux = findViewById(R.id.passwordTip);
        passConfirmAux = findViewById(R.id.passwordTip3);
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (password.length() < 8) { // Caso en el que la longitud de la contrasenya no sea correcto
            texto = "Invalid password. Use at least 8 characters";
            passAux.setText(texto);
            passAux.setTextColor(Color.RED);
            seguir = false;
        } else { // La contrasenya es valida y por tanto se quita el mensaje de error de contrasenya
            texto = "";
            passAux.setText(texto);
        }
        if (!passwordConfirm.equals(password)) { // Las contrasenyas no coinciden, se muestra mensaje de error
            passConfirmAux.setVisibility(View.VISIBLE);
            seguir = false;
        } else { // Coinciden por tanto se quita el mensaje
            passConfirmAux.setVisibility(View.INVISIBLE);
        }

        if (seguir) {
            server.changeDataUser(user, password, mail, new JSONConnection.Listener() {
                @Override
                public void onValidResponse(int responseCode, JSONObject data) throws JSONException {
                    if(responseCode==200){
                        onBackPressed();
                    }
                }

                @Override
                public void onErrorResponse(Throwable throwable) {

                }
            });
        }
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        setResult(RESULT_OK, i);
        finish();
    }

}
