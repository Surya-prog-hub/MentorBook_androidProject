package com.example.admin_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class activity_upload_notice extends AppCompatActivity {
    private CardView addimage;
    private ImageView noticeImageview;
    private EditText noticeTitle;
    private Button uploadNoticeBtn;
    private final int REQ=1;
    private Bitmap bitmap;
    private DatabaseReference reference,dbRef;
    private StorageReference storageReference;
    String downloadUrl="";
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_notice);
        reference= FirebaseDatabase.getInstance().getReference();
        storageReference= FirebaseStorage.getInstance().getReference();
        pd=new ProgressDialog(this);
        addimage = findViewById(R.id.addimage);
        noticeTitle=findViewById(R.id.noticeTitle);
        uploadNoticeBtn = findViewById(R.id.uploadNoticeBtn);
        noticeImageview=findViewById(R.id.noticeImageView);
        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        uploadNoticeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(noticeTitle.getText().toString().isEmpty()){
                    noticeTitle.setError("Empty");
                    noticeTitle.requestFocus();
                }
                else if(bitmap==null){
                    uploadData();
                }
                else{
                    uploadImage();
                }
            }
        });
    }

    private void uploadData() {
        dbRef=reference.child("Notice");
        final String uniquekey= dbRef.push().getKey();

        String title = noticeTitle.getText().toString();

        Calendar calForDate= Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("dd-MM-yy");

        String date=currentDate.format(calForDate.getTime());
        Calendar calForTime= Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        String time = currentTime.format(calForTime.getTime());

        NoticeData noticedata= new NoticeData(title,downloadUrl,date,time,uniquekey);
        dbRef.child(uniquekey).setValue(noticedata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();
                Toast.makeText(activity_upload_notice.this, "Notice Uploaded",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(activity_upload_notice.this,"Something went wrong", Toast.LENGTH_SHORT);
            }
        });
    }

    private void uploadImage() {
        pd.setMessage("Uploading....");
        pd.show();
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalimg=baos.toByteArray();
        final StorageReference filepath;
        filepath=storageReference.child("Notice").child(finalimg+"jpg");
        final UploadTask uploadTask=filepath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(activity_upload_notice.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                    uploadData();
                                }
                            });
                        }
                    });
                }
                else {
                    pd.dismiss();
                    Toast.makeText(activity_upload_notice.this,"Somrthing went wrong",Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private void openGallery() {
        Intent pickimage=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
           startActivityForResult(pickimage,REQ);
    }
    @Override
    protected void onActivityResult(int requestCode , int resultCode , @Nullable Intent data){
   super.onActivityResult(requestCode,resultCode,data);
   if(requestCode== REQ && resultCode== RESULT_OK){
       Uri uri= data.getData();
       try {
           bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
       noticeImageview.setImageBitmap(bitmap);
   }
    }
}