package com.example.admin_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddTeachers extends AppCompatActivity {
       private ImageView addTeacherImage;
       private EditText addTeacherName, addTeacherEmail, addTeacherPost;
       private Spinner addTeacherCategory;
       private Button addTeacherBtn;
       private Bitmap bitmap=null;
    private final int REQ = 1;
    private String category;
    private String name,email,post,downloadUrl="";
    private ProgressDialog pd;

    private StorageReference storageReference;
    private DatabaseReference reference,dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teachers);

        addTeacherImage=findViewById(R.id.addTeacherImage);
        addTeacherName=findViewById(R.id.addTeacherName);
        addTeacherEmail=findViewById(R.id.addTeacherEmail);
        addTeacherPost=findViewById(R.id.addTeacherPost);
        addTeacherCategory=findViewById(R.id.addTeacherCategory);
        addTeacherBtn=findViewById(R.id.addTeacherBtn);
        pd=new ProgressDialog(this);
        reference= FirebaseDatabase.getInstance().getReference().child("Teacher");
        storageReference= FirebaseStorage.getInstance().getReference();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"Select Category", "Advance Java", "Android App Development", "React"});
        addTeacherCategory.setAdapter(adapter);

        // Set the listener for the spinner
       addTeacherCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = addTeacherCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        addTeacherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
addTeacherBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        checkValidation();
    }
});
    }

    private void checkValidation() {
        name=addTeacherName.getText().toString();
        email=addTeacherEmail.getText().toString();
        post=addTeacherPost.getText().toString();
        if(name.isEmpty()){
            addTeacherName.setError("Empty");
            addTeacherName.requestFocus();
        }else   if(email.isEmpty()) {
            addTeacherEmail.setError("Empty");
            addTeacherEmail.requestFocus();
        }else   if(post.isEmpty()) {
            addTeacherPost.setError("Empty");
            addTeacherPost.requestFocus();
        }else if(category.equals("Select Category")){
            Toast.makeText(this,"Please select Teacher catagory",Toast.LENGTH_SHORT).show();
        }
        else if(bitmap==null){
            insertData();

        }else{
           uploadImage();
        }
    }

    private void uploadImage() {
        pd.setMessage("Uploading....");
        pd.show();
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalimg=baos.toByteArray();
        final StorageReference filepath;
        filepath=storageReference.child("Teachers").child(finalimg+"jpg");
        final UploadTask uploadTask=filepath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(AddTeachers.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl=String.valueOf(uri);
                                    insertData();
                                }
                            });
                        }
                    });
                }
                else {
                    pd.dismiss();
                    Toast.makeText(AddTeachers.this,"Somrthing went wrong",Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private void insertData() {
        dbRef=reference.child(category);
        final String uniquekey= dbRef.push().getKey();





        TeacherData teacherdata= new TeacherData(name,email,post,downloadUrl,uniquekey);
        dbRef.child(uniquekey).setValue(teacherdata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();
                Toast.makeText(AddTeachers.this, "Teacher Added",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(AddTeachers.this,"Something went wrong", Toast.LENGTH_SHORT);
            }
        });
    }


    private void openGallery() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // Start the activity to choose an image
        startActivityForResult(pickImageIntent, REQ);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the request code and result code are correct
        if (requestCode == REQ && resultCode == RESULT_OK) {

            // Get the URI of the selected image
            Uri uri = data.getData();

            // Try to get the bitmap of the image

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Set the image view to display the selected image
            addTeacherImage.setImageBitmap(bitmap);
        }
    }
}