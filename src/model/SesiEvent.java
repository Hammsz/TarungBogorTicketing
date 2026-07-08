package model;

public class SesiEvent {
    private String idSesi;
    private String namaSesi;
    private int stok;

    public SesiEvent(String idSesi, String namaSesi, int stok) {
        this.idSesi = idSesi;
        this.namaSesi = namaSesi;
        setStok(stok);
    }

    public String getIdSesi() {
        return idSesi;
    }

    public String getNamaSesi() {
        return namaSesi;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        if (stok < 0) {
            throw new IllegalArgumentException("Stok sesi tidak boleh minus.");
        }
        this.stok = stok;
    }

    public boolean cekStok(int jumlah) {
        return jumlah > 0 && stok >= jumlah;
    }

    public void kurangiStok(int jumlah) {
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah tiket harus lebih dari 0.");
        }
        if (!cekStok(jumlah)) {
            throw new IllegalArgumentException("Stok sesi tidak cukup.");
        }
        stok -= jumlah;
    }

    public void tambahStok(int jumlah) {
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah stok tambahan harus angka positif.");
        }
        stok += jumlah;
    }
}
