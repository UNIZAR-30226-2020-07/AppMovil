package com.instantmusic.appmovil;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

/**
 * Actividad principal. Muestra el listado de notas.
 */
public class MusicApp extends AppCompatActivity {
    private static int ACTIVITY_CREATE = 0;
    private EditText mail;
    private EditText pass;
    private String email;
    private String name;
    private TextView aux;
    public serverInterface server;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app);
        server = new remoteServer();
        mail = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        email = mail.getText().toString();
        mail.setTextColor(Color.WHITE);
        server.addSong("Pegamos tela","Omar Montes","Reggaeton");
        server.addSong("Despacito","Luis Fonsi","Reggaeton");
        server.addSong("Purpurina","Alberto Gambino","Hip-Hop");
        server.addSong("Fighting Gold","Kazusou Oda","Rock");
        server.addSong("Me Gusta","Shakira","Reggaeton");
        server.addSong("Pikete italiano","Kvndy Swing","Trap");
        Button confirmButton = findViewById(R.id.register);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                registInScreen();
            }
        });
        Button confirmButton2 = findViewById(R.id.forget);
        confirmButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                server.recover(mail.getText().toString());
            }
        });
        Button confirmButton3 = findViewById(R.id.accept);
        confirmButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                server.login(mail.getText().toString(), pass.getText().toString(), new JSONConnection.Listener() {
                    @Override
                    public void onValidResponse(int responseCode, JSONObject data) {
                        if(responseCode == 200) {
                            logInScreen();
                        }else{
                            new AlertDialog.Builder(MusicApp.this)
                                    .setMessage(Utils.listifyErrors(data))
                                    .show();

                        }
                    }

                    @Override
                    public void onErrorResponse(Throwable throwable) {
                        Toast.makeText(getBaseContext(), throwable.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        /*
        pass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if ( server.login(mail.getText().toString(), pass.getText().toString()) == 1 ) { // Caso en el que los datos introducidos sean erroneos
                        aux = findViewById(R.id.Tip);
                        aux.setTextColor(Color.RED);
                        aux.setVisibility(View.VISIBLE);
                    }
                    else {
                        logInScreen();
                    }
                    return true;
                }
                return false;
            }
        });
        mail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if ( server.login(mail.getText().toString(), pass.getText().toString()) == 1 ) { // Caso en el que los datos introducidos sean erroneos
                        aux = findViewById(R.id.Tip);
                        aux.setTextColor(Color.RED);
                        aux.setVisibility(View.VISIBLE);
                    }
                    else {
                        logInScreen();

                    }
                    return true;
                }
                return false;
            }
        });
        // mDbHelper = new NotesDbAdapter(this);
        // mDbHelper.open();
        //mList = (ListView) findViewById(R.id.list);
        // registerForContextMenu(mList);

         */

    }

    private void logInScreen() {
        Intent i = new Intent(this, Login.class);
        i.putExtra("email", email);
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