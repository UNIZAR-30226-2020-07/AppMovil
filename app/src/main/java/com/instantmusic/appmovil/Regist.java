package com.instantmusic.appmovil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Regist extends AppCompatActivity {
    private EditText mail;
    private EditText username;
    private EditText pass;
    private EditText passConfirm;
    private TextView passAux;
    private TextView passConfirmAux;
    private TextView emailAux;
    private TextView userAux;
    private String email;
    public serverInterface server;
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_regist);
        Button confirmButton = findViewById(R.id.create);
        server=new localServer(this);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                confirmSignUp();
            }
        });
        Button confirmButton2 = findViewById(R.id.backButton1);
        confirmButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back1();
            }
        });
    }
    @Override
    public void onBackPressed() {}

    private void back1() {
        Intent i = new Intent();
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        setResult(RESULT_OK, i);
        finish();
    }private void back2() {
        setContentView(R.layout.activity_instant_music_app_regist);
        Button confirmButton = findViewById(R.id.create);
        server=new localServer(this);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                confirmSignUp();
            }
        });
        Button confirmButton2 = findViewById(R.id.backButton1);
        confirmButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back1();
            }
        });
        EditText mail=findViewById(R.id.emailSign);
        mail.setText(email);
    }
    private void confirmSignUp() {
        boolean seguir = true;
        mail = findViewById(R.id.emailSign);
        pass = findViewById(R.id.passwordSign);
        passConfirm = findViewById(R.id.passwordSign4);
        String texto;
        email = mail.getText().toString();
        String password = pass.getText().toString();
        String passwordConfirm = passConfirm.getText().toString();
        System.out.println(passwordConfirm);
        System.out.println(password);
        emailAux = findViewById(R.id.emailTip);
        passAux = findViewById(R.id.passwordTip);
        passConfirmAux = findViewById(R.id.passwordTip3);
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
        if ( !passwordConfirm.equals(password) ) { // Las contrasenyas no coinciden, se muestra mensaje de error
            passConfirmAux.setVisibility(View.VISIBLE);
            seguir = false;
        }
        else { // Coinciden por tanto se quita el mensaje
            passConfirmAux.setVisibility(View.INVISIBLE);
        }
        if ( email.isEmpty() ) { // Caso en el que el email sea vacio
            texto = "Email is empty";
            emailAux.setText(texto);
            emailAux.setTextColor(Color.RED);
            emailAux.setVisibility(View.VISIBLE);
            seguir = false;
        }
        else if ( server.checkUser(email) ==0 ) { // Caso en el que el email este en uso ya
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
            setContentView(R.layout.activity_instant_music_app_regist2);
            Button confirmButton2 = findViewById(R.id.createF);
            confirmButton2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    confirmSignUp2();
                }
            });
            Button confirmButton3 = findViewById(R.id.backButton2);
            confirmButton3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    back2();
                }
            });
        }
    }private void confirmSignUp2() {
        boolean seguir = true;
        username = findViewById(R.id.usernameSign);
        String texto;
        String user = username.getText().toString();
        userAux = findViewById(R.id.usernameTip);
        if ( username.length() < 4 ) { // Caso en el que la longitud de la contrasenya no sea correcto
            texto = "Invalid username. Use at least 4 characters";
            userAux.setText(texto);
            userAux.setTextColor(Color.RED);
            seguir = false;
        }
        else { // La contrasenya es valida y por tanto se quita el mensaje de error de contrasenya
            texto = "";
            userAux.setText(texto);
        }
        if ( user.isEmpty() ) { // Caso en el que el email sea vacio
            texto = "Username is empty";
            userAux.setText(texto);
            userAux.setTextColor(Color.RED);
            userAux.setVisibility(View.VISIBLE);
            seguir = false;
        }
        if ( seguir ) { // Caso en el que no hay ningun error
            server.registUser(mail.getText().toString(),pass.getText().toString(),user);
            Intent i = new Intent();
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            setResult(RESULT_OK, i);
            finish();
        }
    }
}
