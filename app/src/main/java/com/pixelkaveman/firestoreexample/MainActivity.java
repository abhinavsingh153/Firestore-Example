package com.pixelkaveman.firestoreexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static final String KEY_TITLE= "title";
    public static final String KEY_DESCRIPTION = "description";

    private EditText editTextTitle;
    private EditText editTextDescription;
    private Button buttonSave;
    private Button buttonLoad;
    private TextView textViewData;

    // cretae a varibale which will be aused as a reference to our firestore database
    FirebaseFirestore db = FirebaseFirestore.getInstance().getInstance();
    DocumentReference noteRef = db.collection("Notebook").document("My first note");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        buttonSave = findViewById(R.id.save_btn);
        buttonLoad = findViewById(R.id.load_btn);
        textViewData = findViewById(R.id.text_view_data);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create collection and save the data on firestore
                saveNote();
            }
        });

        //load button click
        buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // load dtaata from firestore collections or documents

                loadNote();
            }
        });
    }

    private void saveNote(){

        String title  = editTextTitle.getText().toString();
        String description  = editTextDescription.getText().toString();

        Map<String , Object> note = new HashMap<>();
        note.put(KEY_TITLE , title);
        note.put(KEY_DESCRIPTION , description);

        //implicitly crete the collection and
        //inside the clollection store a document
        noteRef.set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(MainActivity.this, "Note saved" , Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(MainActivity.this, "Error!" , Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void loadNote(){


        noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        //Chaeck if the document snapshot exists
                        if(documentSnapshot.exists()){
                            // poultae the textViewData with the title snd description data
                            String title = documentSnapshot.getString(KEY_TITLE);
                            String description = documentSnapshot.getString(KEY_DESCRIPTION);

                            textViewData.setText("Title: " + title + "\n" + "Description: " + description);
;                        }
                        else {
                            Toast.makeText(MainActivity.this , "Documnet does'nt exist.",Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this , "Error!",Toast.LENGTH_LONG).show();
                        Log.d(TAG , e.toString());
                    }
                });
    }
}
