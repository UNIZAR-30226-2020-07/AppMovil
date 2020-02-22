package com.instantmusic.appmovil;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import androidx.appcompat.app.AppCompatActivity;


/**
 * Actividad principal. Muestra el listado de notas.
 */
public class MusicApp extends AppCompatActivity {

    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;

    private static final int REGISTER = Menu.FIRST;
    private static final int RECOVER =Menu.FIRST+1;
    private static final int LOGIN = Menu.FIRST + 1;
    //private static final int EDIT_ID = Menu.FIRST + 2;
    //private static final int SEND_SMS_ID = Menu.FIRST + 3;
    //private static final int SEND_EMAIL_ID = Menu.FIRST + 4;

    private ListView mList;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_music_app);

       // mDbHelper = new NotesDbAdapter(this);
       // mDbHelper.open();
        //mList = (ListView) findViewById(R.id.list);
       // registerForContextMenu(mList);

    }

    /**
     * Populates the list with the notes
     */
   /* private void fillData() {
        // Get all of the notes from the database and create the item list
        Cursor mNotesCursor = mDbHelper.fetchAllNotes();
        startManagingCursor(mNotesCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{NotesDbAdapter.KEY_TITLE};

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.text1};

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.notes_row, mNotesCursor, from, to);
        mList.setAdapter(notes);
    }*/


    @Override
    /*public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, INSERT_ID, Menu.NONE, R.string.menu_insert);
        return result;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case REGISTER:
                registuser();
                return true;
            case RECOVER:
                registuser();
                return true;
            case LOGIN:
                registuser();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, REGISTER, Menu.NONE, R.string.register);
        menu.add(Menu.NONE, RECOVER, Menu.NONE, R.string.passwordR);
        menu.add(Menu.NONE, LOGIN, Menu.NONE, R.string.accept);

    }

  /*  @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                mDbHelper.deleteNote(info.id);
                fillData();
                return true;
            case EDIT_ID:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                editNote(info.position, info.id);
                return true;
            case SEND_EMAIL_ID:
            case SEND_SMS_ID:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                Cursor cursor = mDbHelper.fetchNote(info.id);
                (item.getItemId() == SEND_EMAIL_ID ? sendEMAIL : sendSMS).send(
                        cursor.getString(cursor.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY))
                );
                return true;
        }
        return super.onContextItemSelected(item);
    }*/

    /**
     * Starts the activity to create a new note
     */
    private void registuser() {
        Intent i = new Intent(this, Regist.class);
    }
    }
    private void createNote() {
        Intent i = new Intent(this, NoteEdit.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    /**
     * Starts the activity to edit an existing note
     * @param position unused
     * @param id identifier of the note that will be edited
     */
    protected void editNote(int position, long id) {
        Intent i = new Intent(this, NoteEdit.class);
        i.putExtra(NotesDbAdapter.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_EDIT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }
}