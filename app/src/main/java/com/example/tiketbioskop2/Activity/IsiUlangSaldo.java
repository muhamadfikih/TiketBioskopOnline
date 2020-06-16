package com.example.tiketbioskop2.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiketbioskop2.Database.User;
import com.example.tiketbioskop2.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class IsiUlangSaldo extends AppCompatActivity {
    public static final String EXTRA_USER = "extra_user";
    private TextView txtSaldouser, txtSaldopwd,  noSaldo, TSALDO, TotalSaldo;
    Button btnIsiulang;
    private String username, pwd, email, nama,s;
    private RadioGroup RGsaldo;
    private RadioButton RBsaldo;
    private int TSaldo, saldo;
    RadioButton updtradioBtnGENDER;
    DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isi_ulang_saldo);
        txtSaldouser = findViewById(R.id.Saldousername);
        txtSaldopwd = findViewById(R.id.Saldopwd);
        btnIsiulang = findViewById(R.id.btnIsi);
        noSaldo = findViewById(R.id.noSaldo);
        RGsaldo = findViewById(R.id.Isisaldo);
        TSALDO = findViewById(R.id.pilihSaldo);
        TotalSaldo = findViewById(R.id.TotalnoSaldo);

        database = FirebaseDatabase.getInstance().getReference("Data");


        final User user = getIntent().getParcelableExtra(EXTRA_USER);
        noSaldo.setText(user.getSaldo().replace(".",""));
        String t = noSaldo.getText().toString().trim();

        final int saldo = Integer.parseInt(t);

        txtSaldouser.setText(user.getUsername());

        RGsaldo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int pilihSaldo = RGsaldo.getCheckedRadioButtonId();
                RBsaldo = findViewById(pilihSaldo);
                TSALDO.setText(RBsaldo.getText().toString().replace(".",""));
                TSaldo = Integer.parseInt(TSALDO.getText().toString()) + saldo;
                TotalSaldo.setText("" + TSaldo);
            }
        });
        btnIsiulang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = txtSaldouser.getText().toString();
                nama = user.getNama();
                email = user.getEmail();
                String pwd = user.getPassword();
                s = TotalSaldo.getText().toString();
                if (txtSaldopwd.getText().toString().equals("")){
                    txtSaldopwd.setError("Harap isi terlebih dahulu");
                    txtSaldopwd.requestFocus();
                }
                else if (!txtSaldopwd.getText().toString().equals(pwd)){
                    txtSaldopwd.setError("Wrong Password");
                    txtSaldopwd.requestFocus();
                }
                else{

                    updateData(new User(username,nama,email,pwd,s),username);
                }
            }
        });
    }

    private void updateData(final User user, final String saldo){
        database.child(saldo).setValue(user).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(IsiUlangSaldo.this, "Berhasil Di tambah", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(IsiUlangSaldo.this, MainActivity.class)
                .putExtra(MainActivity.EXTRA_USER, user));
            }
        });
    }
}
