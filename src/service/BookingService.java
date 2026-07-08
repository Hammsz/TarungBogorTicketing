package service;

import java.util.ArrayList;

import model.Customer;
import model.SesiEvent;
import model.Tiket;
import model.Transaksi;

public class BookingService {
    private ArrayList<Tiket> daftarTiket;
    private ArrayList<Transaksi> daftarTransaksi;

    public BookingService() {
        daftarTiket = new ArrayList<>();
        daftarTransaksi = new ArrayList<>();
    }

    public void tambahTiket(Tiket tiket) {
        if (tiket != null) {
            daftarTiket.add(tiket);
        }
    }

    public ArrayList<Tiket> getDaftarTiket() {
        return daftarTiket;
    }

    public ArrayList<Transaksi> getDaftarTransaksi() {
        return daftarTransaksi;
    }

    public Tiket getTiketByKategori(String kategori) {
        for (Tiket tiket : daftarTiket) {
            if (tiket.getKategori().equalsIgnoreCase(kategori)) {
                return tiket;
            }
        }
        return null;
    }

    public SesiEvent getSesiByNama(Tiket tiket, String namaSesi) {
        if (tiket == null || namaSesi == null) {
            return null;
        }
        return tiket.getSesiByNama(namaSesi);
    }

    public Transaksi prosesBooking(Customer customer, Tiket tiket, SesiEvent sesi, int jumlah) {
        return prosesBooking(customer, tiket, sesi, jumlah, "Cash on Venue");
    }

    public Transaksi prosesBooking(Customer customer, Tiket tiket, SesiEvent sesi, int jumlah, String paymentMethod) {
        if (customer == null) {
            throw new IllegalArgumentException("Data customer tidak valid.");
        }
        if (tiket == null) {
            throw new IllegalArgumentException("Pilih kategori tiket terlebih dahulu.");
        }
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah tiket harus lebih dari 0.");
        }

        String namaSesiTransaksi;

        // Untuk tiket Per-Sesi, stok wajib diambil dari object SesiEvent yang dipilih.
        if (tiket.isMembutuhkanSesi()) {
            if (sesi == null) {
                throw new IllegalArgumentException("Pilih sesi terlebih dahulu.");
            }

            if (sesi.cekStok(jumlah)) {
                sesi.kurangiStok(jumlah);
                namaSesiTransaksi = sesi.getNamaSesi();
            } else {
                throw new IllegalArgumentException("Stok sesi tidak cukup.");
            }
        } else {
            if (tiket.cekStok(jumlah)) {
                tiket.kurangiStok(jumlah);
                namaSesiTransaksi = tiket.getKategori().equalsIgnoreCase("All Day") ? "Semua Sesi" : "-";
            } else {
                throw new IllegalArgumentException("Stok tiket tidak cukup.");
            }
        }

        Transaksi transaksi = new Transaksi(generateIdTransaksi(), customer, tiket, namaSesiTransaksi, jumlah, paymentMethod);
        daftarTransaksi.add(transaksi);
        return transaksi;
    }

    public boolean cekKetersediaanStok(Tiket tiket, SesiEvent sesi, int jumlah) {
        if (tiket == null || jumlah <= 0) {
            return false;
        }
        if (tiket.isMembutuhkanSesi()) {
            return sesi != null && sesi.cekStok(jumlah);
        }
        return tiket.cekStok(jumlah);
    }

    public int getStokTersedia(Tiket tiket, SesiEvent sesi) {
        if (tiket == null) {
            return 0;
        }
        if (tiket.isMembutuhkanSesi()) {
            return sesi == null ? 0 : sesi.getStok();
        }
        return tiket.getStok();
    }

    public void tambahStokTiket(Tiket tiket, int jumlah) {
        if (tiket == null) {
            throw new IllegalArgumentException("Pilih kategori tiket terlebih dahulu.");
        }
        if (tiket.isMembutuhkanSesi()) {
            throw new IllegalArgumentException("Tiket Per-Sesi harus memilih sesi untuk tambah stok.");
        }
        tiket.tambahStok(jumlah);
    }

    public void tambahStokSesi(Tiket tiket, SesiEvent sesi, int jumlah) {
        if (tiket == null || !tiket.isMembutuhkanSesi()) {
            throw new IllegalArgumentException("Pilih kategori Per-Sesi terlebih dahulu.");
        }
        if (sesi == null) {
            throw new IllegalArgumentException("Pilih sesi terlebih dahulu.");
        }
        sesi.tambahStok(jumlah);
    }

    public int hitungTotalPendapatan() {
        int total = 0;
        for (Transaksi transaksi : daftarTransaksi) {
            total += transaksi.getTotalHarga();
        }
        return total;
    }

    public String generateIdTransaksi() {
        return String.format("TRX%03d", daftarTransaksi.size() + 1);
    }
}
