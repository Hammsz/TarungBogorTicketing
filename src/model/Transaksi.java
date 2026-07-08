package model;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Transaksi {
    private String idTransaksi;
    private Customer customer;
    private Tiket tiket;
    private String namaSesi;
    private int jumlah;
    private int totalHarga;
    private String paymentMethod;
    private LocalDateTime tanggalTransaksi;

    public Transaksi(String idTransaksi, Customer customer, Tiket tiket, String namaSesi, int jumlah) {
        this(idTransaksi, customer, tiket, namaSesi, jumlah, "Cash on Venue");
    }

    public Transaksi(String idTransaksi, Customer customer, Tiket tiket, String namaSesi, int jumlah, String paymentMethod) {
        this.idTransaksi = idTransaksi;
        this.customer = customer;
        this.tiket = tiket;
        this.namaSesi = namaSesi;
        this.jumlah = jumlah;
        this.paymentMethod = paymentMethod;
        this.tanggalTransaksi = LocalDateTime.now();
        hitungTotal();
    }

    public void hitungTotal() {
        this.totalHarga = tiket.hitungSubtotal(jumlah);
    }

    public String cetakStruk() {
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        return "TARUNG BOGOR TICKETING\n"
                + "PAYMENT SUCCESSFUL, THANK YOU. SEE YOU AT THE RING!!\n\n"
                + "ID Transaksi : " + idTransaksi + "\n"
                + "Nama Pembeli : " + customer.getNama() + "\n"
                + "No HP        : " + customer.getNoHp() + "\n"
                + "Email        : " + customer.getEmail() + "\n"
                + "Kategori     : " + tiket.getKategori() + "\n"
                + "Sesi         : " + namaSesi + "\n"
                + "Jumlah       : " + jumlah + "\n"
                + "Metode Bayar : " + paymentMethod + "\n"
                + "Total Harga  : " + formatRupiah.format(totalHarga) + "\n"
                + "Tanggal      : " + tanggalTransaksi.format(formatter);
    }

    public String getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(String idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Tiket getTiket() {
        return tiket;
    }

    public void setTiket(Tiket tiket) {
        this.tiket = tiket;
    }

    public String getNamaSesi() {
        return namaSesi;
    }

    public void setNamaSesi(String namaSesi) {
        this.namaSesi = namaSesi;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
        hitungTotal();
    }

    public int getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(int totalHarga) {
        this.totalHarga = totalHarga;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getTanggalTransaksi() {
        return tanggalTransaksi;
    }

    public void setTanggalTransaksi(LocalDateTime tanggalTransaksi) {
        this.tanggalTransaksi = tanggalTransaksi;
    }
}
