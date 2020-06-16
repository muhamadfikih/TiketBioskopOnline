package com.example.tiketbioskop2.Database;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String username, nama, email, password, saldo;

    public User(){

    }

    public User(String username,String nama, String email, String password, String saldo){
        this.username = username;
        this.nama = nama;
        this.email = email;
        this.password = password;
        this.saldo = saldo;
    }

    protected User(Parcel in) {
        this.username = in.readString();
        this.nama = in.readString();
        this.email = in.readString();
        this.password = in.readString();
        this.saldo = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public void setUser(String username) {
        this.username = username;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public String toString(){
        return this.username
                + " " + nama
                + " " + email
                + " " + password
                + " " + saldo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(nama);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(saldo);
    }
}
