package com.example.tiketbioskop2.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tiketbioskop2.Activity.Daftar;
import com.example.tiketbioskop2.Activity.DetailMovie;
import com.example.tiketbioskop2.Activity.Login;
import com.example.tiketbioskop2.Activity.MainActivity;
import com.example.tiketbioskop2.Database.User;
import com.example.tiketbioskop2.Model.Movie;
import com.example.tiketbioskop2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AdapterMovie extends RecyclerView.Adapter<AdapterMovie.ListViewHolder>{


    private ArrayList<Movie> listData;
    public AdapterMovie(ArrayList<Movie> list){
        this.listData = list;
    }
    private AdapterMovie.ItemClickListener onItemClickListener;
    public void setData(ArrayList<Movie> items){
        listData.clear();
        listData.addAll(items);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(AdapterMovie.ItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View mview = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_row_data, parent,false);
        return new ListViewHolder(mview);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder listViewHolder, final int position) {
        Movie movies = listData.get(position);
        String url_image = "https://image.tmdb.org/t/p/w185" + movies.getPhoto();

        listViewHolder.tvName.setText(movies.getName());
        listViewHolder.vote_average.setText(movies.getVote_average());

        Glide.with(listViewHolder.itemView.getContext())
                .load(url_image)
                .placeholder(R.color.colorAccent)
                .dontAnimate()
                .into(listViewHolder.imgPhoto);
        listViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClicked(listData.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView tvName, vote_average;
        ListViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_photo);
            tvName = itemView.findViewById(R.id.txt_name);
            vote_average = itemView.findViewById(R.id.vote_average);
//            itemView.setOnClickListener(this);
        }
        void bind(Movie movies) {

        }
//
//        @Override
//        public void onClick(View view) {
//            int position = getAdapterPosition();
//            Movie movies = listData.get(position);
////
//            movies.setName(movies.getName());
//            movies.setDescription(movies.getDescription());
//
//            Intent moveWithObjectIntent = new Intent(itemView.getContext(), DetailMovie.class);
//            moveWithObjectIntent.putExtra(DetailMovie.EXTRA_MOVIE, movies);
//            itemView.getContext().startActivity(moveWithObjectIntent);
//        }
    }
    public interface ItemClickListener{
        void onItemClicked(Movie movie);
    }
}
