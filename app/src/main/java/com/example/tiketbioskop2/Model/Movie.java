package com.example.tiketbioskop2.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class Movie implements Parcelable {
    private String name,description,photo,vote_count,popularity,vote_average;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public String getVote_count() {
        return vote_count;
    }

    public void setVote_count(String vote_count) {
        this.vote_count = vote_count;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }


    public Movie(JSONObject object) {
        try {
            String name = object.getString("title");
            String overview = object.getString("overview");
            String poster_path = object.getString("poster_path");
            String vote_count = object.getString("vote_count");
            String popularity = object.getString("popularity");
            String vote_average = object.getString("vote_average");

            this.name = name;
            this.description = overview;
            this.photo = poster_path;
            this.vote_count = vote_count;
            this.popularity = popularity;
            this.vote_average = vote_average;

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(this.photo);
        dest.writeString(this.vote_count);
        dest.writeString(this.popularity);
        dest.writeString(this.vote_average);
    }

    public Movie(Parcel in) {
        this.name = in.readString();
        this.description = in.readString();
        this.photo = in.readString();
        this.vote_count = in.readString();
        this.popularity = in.readString();
        this.vote_average = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
