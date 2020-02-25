package com.instantmusic.appmovil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Regist extends AppCompatActivity {
    private EditText mail;
    private EditText pass;
    private TextView passAux;
    private TextView emailAux;
    private serverInterface server = new localServer();

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_regist);

        Button confirmButton = findViewById(R.id.create);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                confirmSignUp();
            }
        });
        /*mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();*/

    }

    private void confirmSignUp() {
        boolean seguir = true;
        mail = findViewById(R.id.emailSign);
        pass = findViewById(R.id.passwordSign);
        String texto;
        String email = mail.getText().toString();
        String password = pass.getText().toString();
        emailAux = findViewById(R.id.emailTip);
        passAux = findViewById(R.id.passwordTip);
        if ( password.length() < 8 ) { // Caso en el que la longitud de la contrasenya no sea correcto
            texto = "Invalid password. Use at least 8 characters";
            passAux.setText(texto);
            passAux.setTextColor(Color.RED);
            seguir = false;
        }
        else { // La contrasenya es valida y por tanto se quita el mensaje de error de contrasenya
            texto = "";
            passAux.setText(texto);
        }
        if ( email.isEmpty() ) { // Caso en el que el email sea vacio
            texto = "Email is empty";
            emailAux.setText(texto);
            emailAux.setTextColor(Color.RED);
            emailAux.setVisibility(View.VISIBLE);
            seguir = false;
        }
        else if ( server.checkUser(email) != null ) { // Caso en el que el email este en uso ya
            texto = "That email is already registered";
            emailAux.setText(texto);
            emailAux.setTextColor(Color.RED);
            emailAux.setVisibility(View.VISIBLE);
            seguir = false;
        }
        else { // El email es valido y por tanto se quita el mensaje de error de email
            texto = "";
            emailAux.setText(texto);
        }

        if ( seguir ) { // Caso en el que no hay ningun error
            server.registUser(email,password);
            Intent i = new Intent(this, Login.class);
            startActivityForResult(i, 0);
        }
    }
}
