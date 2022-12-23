package com.example.uaspraktikumpemrogramanandroid.model;

public class Berita {

    private String judulBerita;
    private String authors;
    private String isi;

    public Berita() {
        // Default constructor required for calls to DataSnapshot.getValue(Berita.class)
    }

    public String getJudulBerita() {
        return judulBerita;
    }

    public void setJudulBerita(String judulBerita) {
        this.judulBerita = judulBerita;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

}
