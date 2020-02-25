package com.instantmusic.appmovil;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Regist extends AppCompatActivity {
    private EditText mail;
    private EditText pass;
    private TextView passAux;
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
        mail = findViewById(R.id.emailSign);
        pass = findViewById(R.id.passwordSign);
        String email = mail.getText().toString();
        String password = pass.getText().toString();
        if ( password.length() < 8 ) {
            passAux = findViewById(R.id.passwordTip);
            String texto = "Invalid password. Use at least 8 characters";
            passAux.setText(texto);
            passAux.setTextColor(0xFA3021);
        }

    }
}
