package model;

import java.util.ArrayList;

public class Tiket {
    private String idTiket;
    private String kategori;
    private int harga;
    private int stok;
    private boolean membutuhkanSesi;
    private ArrayList<SesiEvent> daftarSesi;

    public Tiket(String idTiket, String kategori, int harga, int stok, boolean membutuhkanSesi) {
        this.idTiket = idTiket;
        this.kategori = kategori;
        this.harga = harga;
        this.membutuhkanSesi = membutuhkanSesi;
        this.daftarSesi = new ArrayList<>();
        setStok(stok);
    }

    public String getIdTiket() {
        return idTiket;
    }

    public String getKategori() {
        return kategori;
    }

    public int getHarga() {
        return harga;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        if (stok < 0) {
            throw new IllegalArgumentException("Stok tiket tidak boleh minus.");
        }
        this.stok = stok;
    }

    public boolean isMembutuhkanSesi() {
        return membutuhkanSesi;
    }

    public ArrayList<SesiEvent> getDaftarSesi() {
        return daftarSesi;
    }

    public void tambahSesi(SesiEvent sesi) {
        if (sesi != null) {
            daftarSesi.add(sesi);
        }
    }

    public SesiEvent getSesiByNama(String namaSesi) {
        for (SesiEvent sesi : daftarSesi) {
            if (sesi.getNamaSesi().equalsIgnoreCase(namaSesi)) {
                return sesi;
            }
        }
        return null;
    }

    public boolean cekStok(int jumlah) {
        return jumlah > 0 && stok >= jumlah;
    }

    public void kurangiStok(int jumlah) {
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah tiket harus lebih dari 0.");
        }
        if (!cekStok(jumlah)) {
            throw new IllegalArgumentException("Stok tiket tidak cukup.");
        }
        stok -= jumlah;
    }

    public void tambahStok(int jumlah) {
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah stok tambahan harus angka positif.");
        }
        stok += jumlah;
    }

    public int hitungSubtotal(int jumlah) {
        return harga * jumlah;
    }
}
