package com.example.tiketbioskop2.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.tiketbioskop2.Database.User;
import com.example.tiketbioskop2.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Daftar extends AppCompatActivity implements View.OnClickListener {
    EditText username, nama, email, pwd, pwdUlang;
    Button DAFTAR,LOGIN;
    RadioGroup pilihSaldo;
    RadioButton jmlhsaldo;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
        ActionBar title = getSupportActionBar();
        title.setTitle("Register");
        username = findViewById(R.id.username);
        nama = findViewById(R.id.namalengkap);
        email = findViewById(R.id.email);
        pwd = findViewById(R.id.pwd);
        pwdUlang = findViewById(R.id.pwdulang);
        DAFTAR = findViewById(R.id.btnDaftar);
        LOGIN = findViewById(R.id.btnLogin);
        pilihSaldo = findViewById(R.id.saldo);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Data");
        DAFTAR.setOnClickListener(this);
        LOGIN.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnDaftar:
                String USERNAME = username.getText().toString();
                String NAMA = nama.getText().toString();
                String EMAIL = email.getText().toString();
                String PASSWORD = pwd.getText().toString();
                String PWD_ULANG = pwdUlang.getText().toString();
                int tentukanSaldo = pilihSaldo.getCheckedRadioButtonId();
                jmlhsaldo = findViewById(tentukanSaldo);
                String SALDO = jmlhsaldo.getText().toString().replace(".","");
                if (USERNAME.equals("")){
                    username.setError("Tidak boleh kosong");
                    username.requestFocus();
                }else if(NAMA.equals("")){
                    nama.setError("Tidak boleh kosong");
                    nama.requestFocus();
                }else if(EMAIL.equals("")){
                    email.setError("Tidak boleh kosong");
                    email.requestFocus();
                }else if (PASSWORD.equals("")){
                    pwd.setError("Tidak boleh kosong");
                    pwd.requestFocus();
                }else if (!PWD_ULANG.equals(PASSWORD)){
                    pwdUlang.setError("Password tidak sama");
                    pwdUlang.requestFocus();
                }else{
                    final User user = new User(USERNAME, NAMA, EMAIL, PASSWORD, SALDO);
                    databaseReference.child(USERNAME).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            username.setText("");
                            nama.setText("");
                            email.setText("");
                            pwd.setText("");
                            pwdUlang.setText("");
                            pilihSaldo.clearCheck();

                            Toast.makeText(Daftar.this, "Data berhasil di tambahkan", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
            case R.id.btnLogin:
                startActivity(new Intent(Daftar.this, Login.class));
                break;
        }
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
