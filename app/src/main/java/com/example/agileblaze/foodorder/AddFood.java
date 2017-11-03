package com.example.agileblaze.foodorder;

import android.content.Intent;
import android.net.Uri;
import android.os.storage.StorageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddFood extends AppCompatActivity {
    private ImageButton foodImage;
    private static  final int GALLREQ=1;
    private EditText name,desc,price;
    private Uri uri=null;
    private StorageReference storageReference=null;
    private DatabaseReference mRef;
    private FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        name=(EditText)findViewById(R.id.foodName);
        desc=(EditText)findViewById(R.id.foodDescription);
        price=(EditText)findViewById(R.id.foodPrice);
        storageReference= FirebaseStorage.getInstance().getReference();
        mRef=FirebaseDatabase.getInstance().getReference("Item");
    }

    public  void  addImage(View view)
    {
        Intent galaryIntent=new Intent(Intent.ACTION_GET_CONTENT);
        galaryIntent.setType("Image/*");
        startActivityForResult(galaryIntent,GALLREQ);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLREQ && resultCode == RESULT_OK) {
            uri = data.getData();
            foodImage = (ImageButton) findViewById(R.id.addImage);
            foodImage.setImageURI(uri);
        }
    }

    public  void addItemButton(View view)
    {

        final String name_text=name.getText().toString().trim();
        final String desc_text=desc.getText().toString().trim();
        final String price_text=price.getText().toString().trim();

        if(!TextUtils.isEmpty(name_text) && !TextUtils.isEmpty(desc_text) && !TextUtils.isEmpty(price_text))
        {
           StorageReference filepath=storageReference.child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    final  Uri downloadurl=taskSnapshot.getDownloadUrl();
                    Toast.makeText(AddFood.this,"Image Uploaded",Toast.LENGTH_LONG).show();
                    final  DatabaseReference newPost=mRef.push();
                    newPost.child("Name").setValue(name_text);
                    newPost.child("Desc").setValue(desc_text);
                    newPost.child("price").setValue(price_text);
                    newPost.child("Image").setValue(downloadurl.toString());


                }
            });
        }
    }

    }
