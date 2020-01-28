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
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = "MainActivity";
    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextPriority;
    private TextView textViewData;
    private Button buttonSave;
    private Button buttonLoad;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Notebook");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextPriority = findViewById(R.id.edit_text_priority);
        textViewData = findViewById(R.id.text_view_data);
        buttonSave = findViewById(R.id.save_btn);
        buttonLoad = findViewById(R.id.load_btn);

        buttonSave.setOnClickListener(this);
        buttonLoad.setOnClickListener(this);
    }

    public void saveNotes() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();

        if (editTextPriority.getText().length() == 0) {
            editTextPriority.setText("0");
        }

        int priority = Integer.parseInt(editTextPriority.getText().toString());

        Note note = new Note(title, description, priority);

        notebookRef.add(note)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(MainActivity.this, "Note saved", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error !", Toast.LENGTH_LONG).show();
                        //Log.d(TAG, e.toString());
                    }
                });
    }

    public void loadNotes() {

      notebookRef.orderBy("priority")
              .orderBy("title")
              .startAt(3 , "Title2") // will start form priority =3
              .get()
              .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                  @Override
                  public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                      String data  = "";

                      for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){

                          Note note = documentSnapshot.toObject(Note.class);
                          note.setDocumentId(documentSnapshot.getId());

                          String title = note.getTitle();
                          String description = note.getDescription();
                          int priority = note.getPriority();


                          data+= "Id : " + note.getDocumentId() + "\nTitle: " + title + "\nDescription: " + description
                                  + "\nPriority: " + priority + "\n\n";
                      }

                      textViewData.setText(data);
                  }
              })
      .addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
              Log.d(TAG , e.toString());
          }
      });


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.save_btn:
                saveNotes();
                break;

            case R.id.load_btn:
                loadNotes();
                break;


            default:
                //Log.d(TAG, v.getId() + "Button not found");


        }
    }
}
