package com.example.tiketbioskop2.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiketbioskop2.Database.User;
import com.example.tiketbioskop2.Model.Movie;
import com.example.tiketbioskop2.R;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Pesan extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_USER = "extra_user";
    ViewGroup layout;
    public static final String EXTRA_MOVIE = "extra_movie";
    String seats =
              "_____________/"
            + "AA_AAAAAAAAA/"
            + "AA_AAAAAAAAA/"
            + "AA_AAAAAAAAA/"
            + "AA_AAAAAAAAA/"
            + "AA_AAAA_AAAA/"
            + "AA_AAAA_AAAA/"
            + "AA_AAAA_AAAA/"
            + "AA_AAAA_AAAA/"
            + "_____________/"
            + "AA_AAAAAAAAA/"
            + "AA_AAAAAAAAA/"
            + "AA_AAAAAAAAA/"
            + "AA_AAAAAAAAA/"
            + "____________/";

    List<TextView> seatViewList = new ArrayList<>();
    int seatSize = 100;
    int seatGaping = 10;
//    int STATUS_BOOKED = 1;
//    int STATUS_RESERVED = 3;
    int STATUS_AVAILABLE;
    private Button Checkout;
    String selectedIds = "";
    private TextView tiket;
    int jmlhTiket = 0 ;
    private int nomor_bangku = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan);
        ActionBar title = getSupportActionBar();
        title.setTitle("Booking Seats");
        final User user = getIntent().getParcelableExtra(EXTRA_USER);
        tiket = findViewById(R.id.tiket);
        layout = findViewById(R.id.layoutSeat);
        Checkout = findViewById(R.id.btnCheck);
        Checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(tiket.getText().toString()) <= 0){
                    new SweetAlertDialog(Pesan.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Pick Your Seats")
                            .setConfirmText("Cancel")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }else{
                    Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
                    int txtTiket = Integer.parseInt(tiket.getText().toString().trim());
                    startActivity(new Intent(Pesan.this, CheckOut.class)
                            .putExtra(CheckOut.EXTRA_tiket, txtTiket)
                            .putExtra(DetailMovie.EXTRA_MOVIE, movie)
                    .putExtra(CheckOut.EXTRA_USER, user));
                }
            }
        });
        seats = "/" + seats;

        LinearLayout layoutSeat = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutSeat.setOrientation(LinearLayout.VERTICAL);
        layoutSeat.setLayoutParams(params);
//        layoutSeat.setPadding(8 * seatGaping, 8 * seatGaping, 8 * seatGaping, 8 * seatGaping);
        layout.addView(layoutSeat);

        LinearLayout layout = null;

        int count = 0;
        for (int index = 0; index < seats.length(); index++){
            if (seats.charAt(index) == '/') {
                layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layoutSeat.addView(layout);
            }
            else if (seats.charAt(index) == 'A') {
                count++;
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setPadding(0, 0, 0, 2 * seatGaping);
                view.setId(count);
                view.setGravity(Gravity.CENTER);
                view.setBackgroundResource(R.drawable.ic_seats_book);
                view.setText(count + "");
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                view.setTextColor(Color.BLACK);
                view.setTag(STATUS_AVAILABLE);
                layout.addView(view);
                seatViewList.add(view);
                view.setOnClickListener(this);
            }else if (seats.charAt(index) == '_') {
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setBackgroundColor(Color.TRANSPARENT);
                view.setText("");
                layout.addView(view);
            }
        }
    }

    @Override
    public void onClick(View view) {
        nomor_bangku = view.getId();
        if ((int) view.getTag() == STATUS_AVAILABLE) {
            if (selectedIds.contains(nomor_bangku + ",")) {
                selectedIds = selectedIds.replace(+nomor_bangku + ",", "");
                view.setBackgroundResource(R.drawable.ic_seats_book);
                jmlhTiket = jmlhTiket - 1;
                tampil(jmlhTiket);
                Toast.makeText(this, "Seat " + nomor_bangku + " is Cancel Booked", Toast.LENGTH_SHORT).show();
            } else {
                selectedIds = selectedIds + nomor_bangku + ",";
                view.setBackgroundResource(R.drawable.ic_seats_selected);
                jmlhTiket = jmlhTiket + 1;
                tampil(jmlhTiket);
                Toast.makeText(this, "Seat " + nomor_bangku + " is Booked", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void tampil(int nomor){
        tiket.setText("" + nomor);
    }
}
