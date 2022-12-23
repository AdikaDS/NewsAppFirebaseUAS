package com.example.uaspraktikumpemrogramanandroid;

public class Berita {

    private String key;
    private String judulBerita;
    private String authors;
    private String isi;

    public Berita() {
        // Default constructor required for calls to DataSnapshot.getValue(Berita.class)
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
