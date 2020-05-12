package com.instantmusic.appmovil.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.instantmusic.appmovil.R;
import com.instantmusic.appmovil.album.Album;
import com.instantmusic.appmovil.friends.FriendsSearch;
import com.instantmusic.appmovil.playlist.Playlist;
import com.instantmusic.appmovil.playlist.PlaylistActivity;
import com.instantmusic.appmovil.podcast.PodcastSearch;
import com.instantmusic.appmovil.server.connect.JSONConnection;
import com.instantmusic.appmovil.server.remoteServer;
import com.instantmusic.appmovil.server.serverInterface;
import com.instantmusic.appmovil.song.Song;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class Settings extends AppCompatActivity implements JSONConnection.Listener {

    private String username;
    private String mail;
    private TextView user;
    private TextView userNew;
    private TextView email;
    private TextView emailNew;
    private TextView usernameSettings;
    private serverInterface server;
    private boolean open1 = false;
    private boolean open2 = false;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app_settings);
        server = new remoteServer();
        server.getUserData(new JSONConnection.Listener() {
            @Override
            public void onValidResponse(int responseCode, JSONObject data) {
                try {
                    if (responseCode == 200) {
                        mail = data.getString("email");
                        username = data.getString("username");
                        email = findViewById(R.id.changeR);
                        user = findViewById(R.id.changeU);
                        userNew = findViewById(R.id.newUser);
                        emailNew = findViewById(R.id.newMail);
                        usernameSettings=findViewById(R.id.usernameSettings);
                        usernameSettings.setText(username);
                        email.setText(mail);
                        user.setText(username);
                        userNew.setText(username);
                        emailNew.setText(mail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(Throwable throwable) {
                setTitle("Unknown user");
            }
        });

        Button Button1 = findViewById(R.id.menuButton1);
        Button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Home();
            }
        });
        Button Button2 = findViewById(R.id.menuButton2);
        Button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search();
            }
        });
        Button Button3 = findViewById(R.id.menuButton3);
        Button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Podcasts();
            }
        });
        Button Button4 = findViewById(R.id.menuButton4);
        Button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Friends();
            }
        });

        Button changeU = findViewById(R.id.change1);
        Button changeR = findViewById(R.id.change2);
        changeU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText changeEdit=findViewById(R.id.newUser);
                if (open1) {
                    open1=false;
                    LinearLayout change = findViewById(R.id.changeUserMenu);
                    change.setVisibility(View.GONE);
                    Button changeButton = findViewById(R.id.change1);
                    changeButton.setText("CHANGE");

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if ( imm != null ) {
                        imm.showSoftInput(changeEdit, InputMethodManager.SHOW_IMPLICIT);
                    }
                    changeEdit.requestFocus();
                    changeEdit.setSelection(changeEdit.getText().length());
                    username=userNew.getText().toString();
                    user.setText(username);
                    usernameSettings.setText(username);
                    server.changeDataUser(username, "", mail, Settings.this);
                } else {
                    open1=true;
                    LinearLayout change = findViewById(R.id.changeUserMenu);
                    change.setVisibility(View.VISIBLE);
                    Button changeButton = findViewById(R.id.change1);
                    changeButton.setText("CONFIRM");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if ( imm != null ) {
                        imm.showSoftInput(changeEdit, InputMethodManager.SHOW_IMPLICIT);
                    }
                    changeEdit.requestFocus();
                    changeEdit.setSelection(changeEdit.getText().length());

                }
            }
        });
        changeR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText changeEdit=findViewById(R.id.newUser);
                if (open2) {
                    open2=false;
                    LinearLayout change = findViewById(R.id.changeMailMenu);
                    change.setVisibility(View.GONE);
                    Button changeButton = findViewById(R.id.change2);
                    changeButton.setText("CHANGE");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if ( imm != null ) {
                        imm.showSoftInput(changeEdit, InputMethodManager.SHOW_IMPLICIT);
                    }
                    changeEdit.requestFocus();
                    changeEdit.setSelection(changeEdit.getText().length());
                    mail=emailNew.getText().toString();
                    email.setText(mail);
                    server.changeDataUser(username, "", mail, Settings.this);
                } else {
                    open2=true;
                    LinearLayout change = findViewById(R.id.changeMailMenu);
                    change.setVisibility(View.VISIBLE);
                    Button changeButton = findViewById(R.id.change2);
                    changeButton.setText("CONFIRM");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if ( imm != null ) {
                        imm.showSoftInput(changeEdit, InputMethodManager.SHOW_IMPLICIT);
                    }
                    changeEdit.requestFocus();
                    changeEdit.setSelection(changeEdit.getText().length());
                }
            }
        });
        Button changePassword=findViewById(R.id.changeM3);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeP();
            }
        });
        Button logout=findViewById(R.id.logOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

    }
    private void logOut(){
        server.logOut(new JSONConnection.Listener() {
            @Override
            public void onValidResponse(int responseCode, JSONObject data) throws JSONException {
                if (responseCode==200){
                    Intent i = new Intent(Settings.this, MusicApp.class);
                    startActivityForResult(i, 1);
                }
            }

            @Override
            public void onErrorResponse(Throwable throwable) {

            }
        });
    }
    private void changeP() {
        Intent i = new Intent(this, SettingsPassword.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        i.putExtra("user", username);
        i.putExtra("mail", mail);
        startActivityForResult(i, 1);
    }
    private void Home() {
        Intent i = new Intent(this, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        startActivityForResult(i, 1);
    }

    private void Search() {
        Intent i = new Intent(this, Search.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        startActivityForResult(i, 1);

    }

    private void Friends() {
        Intent i = new Intent(this, FriendsSearch.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        startActivityForResult(i, 1);

    }

    private void Podcasts() {
        Intent i = new Intent(this, PodcastSearch.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(i, 1);

    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public void onValidResponse(int responseCode, JSONObject data) throws JSONException {

    }

    @Override
    public void onErrorResponse(Throwable throwable) {

    }
}
