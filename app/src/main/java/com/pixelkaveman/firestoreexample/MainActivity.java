package com.pixelkaveman.firestoreexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    // cretae a varibale which will be aused as a reference to our firestore database
    FirebaseFirestore db = FirebaseFirestore.getInstance().getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        buttonSave = findViewById(R.id.save_btn);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create collection and save the data on firestore
                saveNote();
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
        db.collection("Notebook").document("My first note").set(note)
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
}
