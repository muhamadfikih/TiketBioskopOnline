package com.example.tiketbioskop2.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tiketbioskop2.Database.User;
import com.example.tiketbioskop2.Model.Movie;
import com.example.tiketbioskop2.R;

public class DetailMovie extends AppCompatActivity {
    private ProgressBar progressBar;
    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String EXTRA_USER = "extra_user";
    public static
    TextView txt_name,txt_genre,txt_runtime,txt_detail,inisaldo;
    ImageView img_foto;
    Button btnPesan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        ActionBar title = getSupportActionBar();
        title.setTitle("Detail Film");
        inisaldo = findViewById(R.id.inisaldo);
        final String hsaldo = getIntent().getStringExtra("saldo");
        final User user = getIntent().getParcelableExtra(EXTRA_USER);
        img_foto = findViewById(R.id.foto);
        txt_name = findViewById(R.id.tname);
        txt_genre = findViewById(R.id.tbahasa);
        txt_runtime = findViewById(R.id.truntime);
        txt_detail = findViewById(R.id.detail);
        btnPesan = findViewById(R.id.btnPesan);
        btnPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
                startActivity(new Intent(DetailMovie.this, Pesan.class)
                        .putExtra(DetailMovie.EXTRA_MOVIE, movie)
                .putExtra(Pesan.EXTRA_USER, user));
            }
        });
//
//
        progressBar = findViewById(R.id.progressDetail);

        progressBar.setVisibility(View.VISIBLE);
        final Handler handler = new Handler();


        new Thread(new Runnable() {
            public void run() {
                try{
                    Thread.sleep(5000);
                }
                catch (Exception e) { }

                handler.post(new Runnable() {
                    public void run() {
                        Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE);

                        String url_image = "https://image.tmdb.org/t/p/w185" + movie.getPhoto();

                        txt_name.setText(movie.getName());
                        txt_detail.setText(movie.getDescription());
                        txt_genre.setText(movie.getVote_average());
                        txt_runtime.setText(movie.getPopularity()) ;
                        Glide.with(DetailMovie.this)
                                .load(url_image)
                                .placeholder(R.color.colorAccent)
                                .dontAnimate()
                                .into(img_foto);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }).start();
    }
}
