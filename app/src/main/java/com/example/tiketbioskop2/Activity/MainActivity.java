package com.example.tiketbioskop2.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tiketbioskop2.Adapter.AdapterMovie;
import com.example.tiketbioskop2.Database.User;
import com.example.tiketbioskop2.Model.Movie;
import com.example.tiketbioskop2.Model.Movie_Model;
import com.example.tiketbioskop2.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity implements AdapterMovie.ItemClickListener {
    public static final String EXTRA_USER = "extra_user";
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    private AdapterMovie adapterMovie;
    private ProgressBar progressBar;
    private Movie_Model movie_model;
    RecyclerView recyclerView;
    private ArrayList<Movie> list = new ArrayList<>();
    CarouselView carouselView;
    int[] slide ={R.drawable.slide1,R.drawable.slide2,R.drawable.slide3,R.drawable.slide4};
    private TextView txtUser, textSaldo;
    private Button btnTopup;
    private ImageView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar title = getSupportActionBar();
        title.setTitle("Menu Utama");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Data");

        txtUser = findViewById(R.id.USERNAME);
        textSaldo = findViewById(R.id.SALDO);
        logout = findViewById(R.id.logout);
        final User user = getIntent().getParcelableExtra(EXTRA_USER);
        txtUser.setText(user.getUsername());
        textSaldo.setText(user.getSaldo());
        btnTopup = findViewById(R.id.btnIsiSaldo);
        btnTopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, IsiUlangSaldo.class)
                .putExtra(IsiUlangSaldo.EXTRA_USER, user));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Login.class));
            }
        });

        carouselView = findViewById(R.id.carouselView);
        carouselView.setPageCount(slide.length);
        carouselView.setImageListener(imageListener);

        adapterMovie = new AdapterMovie(list);
        adapterMovie.setOnItemClickListener(this);
        recyclerView = findViewById(R.id.rv_movies);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapterMovie);

        progressBar = findViewById(R.id.progressMovie);
        movie_model = ViewModelProviders.of(this).get(Movie_Model.class);
        movie_model.getMovies().observe(this, getMovie);
        movie_model.setMovies("EXTRA_MOVIE");

        showLoading(true);

        if (!jikaConnect()){
            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Check Connection!")
                    .setConfirmText("Logout")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            finish();
                        }
                    })
                    .show();
        }

    }
    private Observer<ArrayList<Movie>> getMovie = new Observer<ArrayList<Movie>>() {
        @Override
        public void onChanged(ArrayList<Movie> Movies) {
            if (Movies != null) {
                adapterMovie.setData(Movies);
            }
            showLoading(false);
        }
    };

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(slide[position]);
            showLoading(false);
        }
    };

    public boolean jikaConnect(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public void onItemClicked(Movie movie) {
        User user = getIntent().getParcelableExtra(EXTRA_USER);
        user.getUsername();
        user.getEmail();
        user.getPassword();
        user.getSaldo();
        user.getNama();
        Intent intent = new Intent(MainActivity.this, DetailMovie.class);
        intent.putExtra(DetailMovie.EXTRA_MOVIE, movie);
        intent.putExtra(CheckOut.EXTRA_USER, user);
        startActivity(intent);
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
