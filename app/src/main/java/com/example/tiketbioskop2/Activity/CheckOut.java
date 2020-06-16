package com.example.tiketbioskop2.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tiketbioskop2.Database.User;
import com.example.tiketbioskop2.Model.Movie;
import com.example.tiketbioskop2.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CheckOut extends AppCompatActivity {

    public static final int NOTIFICATION_ID = 1;
    public static String CHANNEL_ID = "id";
    public static CharSequence CHANNEL_NAME = "nama";


    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String EXTRA_tiket = "extra_tiket";
    public static final String EXTRA_USER = "extra_user";
    private ImageView img_chk;
    DatabaseReference database;
    private TextView judul, harga, tiket, total, saldo;
    private int Total= 0, Harga = 35000;
    Button bayar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        ActionBar title = getSupportActionBar();
        title.setTitle("Payment");
        database = FirebaseDatabase.getInstance().getReference("Data");
        img_chk = findViewById(R.id.img_photo_chk);
        judul = findViewById(R.id.judul);
        harga = findViewById(R.id.Chk_harga);
        tiket = findViewById(R.id.Chk_tiket);
        total = findViewById(R.id.Chk_total);
        bayar = findViewById(R.id.btnByr);
        saldo = findViewById(R.id.Chk_saldo);

        final User user = getIntent().getParcelableExtra(EXTRA_USER);
        saldo.setText(user.getSaldo());

        int txtTiket = getIntent().getIntExtra(EXTRA_tiket,0);
        tiket.setText("" + txtTiket);
        harga.setText("" + Harga);
        Total = Integer.parseInt(tiket.getText().toString().trim()) * Integer.parseInt(harga.getText().toString().trim());

        total.setText("" + Total);

        final Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        String url_image = "https://image.tmdb.org/t/p/w185" + movie.getPhoto();
        Glide.with(CheckOut.this)
                .load(url_image)
                .placeholder(R.color.colorAccent)
                .dontAnimate()
                .into(img_chk);
        judul.setText(movie.getName());


        bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(saldo.getText().toString()) < Integer.parseInt(total.getText().toString())){
                    new SweetAlertDialog(CheckOut.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Your Balance is not Enough!")
                            .setConfirmText("Top Up Balance")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    startActivity(new Intent(CheckOut.this, IsiUlangSaldo.class)
                                            .putExtra(IsiUlangSaldo.EXTRA_USER, user));

                                }
                            })
                            .show();
                }else if (Integer.parseInt(saldo.getText().toString()) > Integer.parseInt(total.getText().toString())){
                    new SweetAlertDialog(CheckOut.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure want to buy a ticket ?")
                            .setConfirmText("Buy")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    int h = Integer.parseInt(saldo.getText().toString()) - Integer.parseInt(total.getText().toString());
                                    updateData(new User(user.getUsername(),user.getNama(),user.getEmail(),user.getPassword(),String.valueOf(h)),user.getUsername());


                                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(CheckOut.this, CHANNEL_ID)
                                            .setSmallIcon(R.drawable.ic_check)
                                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_check))
                                            .setContentTitle("BERHASIL!!")
                                            .setContentText("Kamu berhasil membeli tiket untuk film " + movie.getName())
                                            .setSubText(getResources().getString(R.string.app_name))
                                            .setAutoCancel(true);

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                                        mBuilder.setChannelId(CHANNEL_ID);
                                        if (mNotificationManager != null) {
                                            mNotificationManager.createNotificationChannel(channel);
                                        }
                                    }

                                    Notification notification = mBuilder.build();

                                    if (mNotificationManager != null) {
                                        mNotificationManager.notify(NOTIFICATION_ID, notification);
                                    }

                                }
                            })
                            .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();

                                }
                            })
                            .show();
                }else{
                    new SweetAlertDialog(CheckOut.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure want to buy a ticket ?")
                            .setConfirmText("Buy")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    int h = Integer.parseInt(saldo.getText().toString()) - Integer.parseInt(total.getText().toString());
                                    updateData(new User(user.getUsername(),user.getNama(),user.getEmail(),user.getPassword(),String.valueOf(h)),user.getUsername());


                                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(CheckOut.this, CHANNEL_ID)
                                            .setSmallIcon(R.drawable.ic_check)
                                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_check))
                                            .setContentTitle("BERHASIL!!")
                                            .setContentText("Kamu berhasil membeli tiket untuk film " + movie.getName())
                                            .setSubText(getResources().getString(R.string.app_name))
                                            .setAutoCancel(true);

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                                        mBuilder.setChannelId(CHANNEL_ID);
                                        if (mNotificationManager != null) {
                                            mNotificationManager.createNotificationChannel(channel);
                                        }
                                    }

                                    Notification notification = mBuilder.build();

                                    if (mNotificationManager != null) {
                                        mNotificationManager.notify(NOTIFICATION_ID, notification);
                                    }

                                }
                            })
                            .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();

                                }
                            })
                            .show();
                }
            }
        });
    }
    private void updateData(final User user, final String saldo){
        database.child(saldo).setValue(user).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(CheckOut.this, "Berhasil Di tambah", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CheckOut.this, MainActivity.class)
                        .putExtra(MainActivity.EXTRA_USER, user));
            }
        });
    }
}
