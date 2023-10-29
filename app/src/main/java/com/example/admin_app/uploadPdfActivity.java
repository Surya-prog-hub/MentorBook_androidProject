package com.example.admin_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

public class uploadPdfActivity extends AppCompatActivity {
    private CardView addPdf;

    private EditText pdfTitle;
    private Button uploadPdfBtn;
    private final int REQ = 1;
    private Uri pdfData;
    private DatabaseReference reference;
    private StorageReference storageReference;
    String downloadUrl = "";
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf);

        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        pd = new ProgressDialog(this);

        addPdf = findViewById(R.id.addPdf);
        pdfTitle = findViewById(R.id.pdfTitle);
        uploadPdfBtn = findViewById(R.id.uploadPdfBtn);

        addPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        uploadPdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPdf();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf File"), REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ && resultCode == RESULT_OK) {
            pdfData = data.getData();
            Toast.makeText(this, "" + pdfData, Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadPdf() {
        pd.setMessage("Uploading...");
        pd.show();

        StorageReference pdfRef = storageReference.child("pdfs/" + pdfData.getLastPathSegment());
        pdfRef.putFile(pdfData)
                .addOnSuccessListener(taskSnapshot -> {
                    pd.dismiss();
                    Toast.makeText(uploadPdfActivity.this, "PDF uploaded successfully", Toast.LENGTH_SHORT).show();

                    downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();

                    // Add the PDF to the database
                    reference.child("pdfs").push().child("title").setValue(pdfTitle.getText().toString());
                    reference.child("pdfs").push().child("url").setValue(downloadUrl);
                })
                .addOnFailureListener(e -> {
                    pd.dismiss();
                    Toast.makeText(uploadPdfActivity.this, "Failed to upload PDF", Toast.LENGTH_SHORT).show();
                });
    }
}
