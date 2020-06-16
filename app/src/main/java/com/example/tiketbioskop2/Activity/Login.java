package com.example.tiketbioskop2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tiketbioskop2.Database.User;
import com.example.tiketbioskop2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login extends AppCompatActivity implements View.OnClickListener{
    private Button btnMasuk, btnDaftar;
    private EditText edtUsername, edtPassword;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar title = getSupportActionBar();
        title.setTitle("Login");
        btnMasuk = findViewById(R.id.btnMasuk);
        btnDaftar = findViewById(R.id.Daftar);
        edtUsername = findViewById(R.id.Lusernmae);
        edtPassword = findViewById(R.id.Lpassword);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Data");
        btnMasuk.setOnClickListener(this);
        btnDaftar.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnMasuk:
                String Lpwd = null;
                String Luser = edtUsername.getText().toString();
                try {
                    Lpwd = edtPassword.getText().toString();
                }catch (Exception e){
                    e.printStackTrace();
                }
                masuk(Luser,Lpwd);
                break;
            case R.id.Daftar:
                startActivity(new Intent(Login.this, Daftar.class));
                break;

        }
    }
    private void masuk(final String  user, final String password){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(user).exists()){
                    if (!user.isEmpty()){
                        User users = dataSnapshot.child(user).getValue(User.class);
                        if (users.getPassword().equals(password)){
                            Toast.makeText(Login.this, user, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Login.this, MainActivity.class)
                                    .putExtra(MainActivity.EXTRA_USER, users));
                        }else {
                            new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Wrong Password!")
                                    .show();
                        }
                    }else {
                        edtUsername.setError("Isi terlebih dahulu");
                        edtPassword.setError("Isi terlebih dahulu");
                        Toast.makeText(Login.this, "Lengkapi terlebih dahulu", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    new SweetAlertDialog(Login.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("User not found!")
                            .setContentText("We cannot find your user ...")
                            .setConfirmText("Register")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    startActivity(new Intent(Login.this,Daftar.class));
                                }
                            })
                            .setCancelButton("Try Again", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.create();
        builder.setIcon(R.drawable.ic_error);
        builder.setTitle("KELUAR!!");
        builder.setMessage("Yakin ingin keluar ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNeutralButton("Tidak jadi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
