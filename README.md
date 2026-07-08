# Tarung Bogor Ticketing

Tarung Bogor Ticketing adalah aplikasi desktop GUI berbasis Java Swing untuk melakukan booking tiket event boxing Tarung Bogor. Aplikasi ini dibuat untuk kebutuhan UAS mata kuliah Pemrograman Berorientasi Objek/PBO dengan struktur class yang rapi, logic stok yang jelas, dan tanpa database eksternal.

Data tiket, sesi, dan transaksi disimpan menggunakan `ArrayList`. Data awal tiket otomatis dibuat saat program dijalankan melalui `Main.java`.

## Catatan Upgrade Versi Modern

Versi terbaru menggunakan `CardLayout` di `MainFrame`, sehingga seluruh flow aplikasi tetap berada dalam satu window tetapi dipisah menjadi beberapa halaman:

- Login Page
- User Booking Page
- Order Review Page
- Payment Page
- Modern Receipt Page
- Admin Page dengan tab Dashboard, Riwayat Transaksi, dan Kelola Stok

Transaksi dan pengurangan stok sekarang hanya terjadi setelah user menekan tombol `Confirm Payment` pada Payment Page. Tahap Booking dan Review hanya melakukan validasi data serta pengecekan stok tanpa mengubah stok.

## Fitur Aplikasi

- Booking tiket berdasarkan data pembeli.
- Login awal dengan opsi Continue as Guest dan Login as Admin.
- Review pesanan sebelum pembayaran.
- Simulasi pembayaran dengan pilihan QRIS, Bank Transfer, E-Wallet, dan Cash on Venue.
- Receipt modern setelah pembayaran berhasil.
- Admin page terpisah dengan navigasi tab.
- Pilihan kategori tiket: VIP, All Day, dan Per-Sesi.
- Dropdown sesi otomatis aktif hanya untuk tiket Per-Sesi.
- Validasi input nama, nomor HP, email Gmail, kategori, sesi, dan jumlah tiket.
- Pengecekan stok sebelum transaksi dibuat.
- Pengurangan stok otomatis hanya setelah payment berhasil dikonfirmasi.
- Transaksi gagal tidak masuk ke riwayat.
- Tampilan stok tiket menggunakan card/panel.
- Riwayat transaksi menggunakan `JTable`.
- Panel admin sederhana untuk menambah stok tiket.
- Total transaksi dan total pendapatan diperbarui otomatis.

## Kategori Tiket

| Kategori | Harga | Stok Awal | Aturan Sesi |
| --- | ---: | ---: | --- |
| VIP | Rp150.000 | 50 | Tidak memilih sesi |
| All Day | Rp100.000 | 100 | Berlaku untuk semua sesi |
| Per-Sesi Sesi 1 | Rp50.000 | 40 | Wajib memilih Sesi 1 |
| Per-Sesi Sesi 2 | Rp50.000 | 40 | Wajib memilih Sesi 2 |
| Per-Sesi Sesi 3 | Rp50.000 | 40 | Wajib memilih Sesi 3 |
| Per-Sesi Sesi 4 | Rp50.000 | 40 | Wajib memilih Sesi 4 |

## Perbedaan VIP, All Day, dan Per-Sesi

VIP menggunakan stok global dari object `Tiket` VIP. Saat user membeli VIP, stok VIP langsung berkurang sesuai jumlah pembelian. Pada tabel transaksi, kolom sesi diisi `-`.

All Day juga menggunakan stok global dari object `Tiket` All Day. Tiket ini dianggap berlaku untuk seluruh sesi, sehingga pada tabel transaksi kolom sesi diisi `Semua Sesi`.

Per-Sesi menggunakan stok yang dipisah per sesi. Stok transaksi tidak diambil dari atribut `stok` pada `Tiket`, tetapi dari object `SesiEvent`. Jika user membeli Per-Sesi Sesi 2, hanya stok Sesi 2 yang berkurang.

## Konsep PBO yang Digunakan

1. Encapsulation
   Semua atribut utama dibuat `private` dan diakses melalui getter/setter.

2. Inheritance
   Class `Customer` dan `Admin` mewarisi abstract class `Pengguna`.

3. Polymorphism
   Method `tampilkanInfo()` dideklarasikan di `Pengguna`, lalu dioverride di `Customer` dan `Admin`.

4. Abstraction
   `Pengguna` dibuat sebagai abstract class agar class turunannya wajib mengimplementasikan `tampilkanInfo()`.

5. Class dan Object
   Program membuat object dari `Tiket`, `SesiEvent`, `Customer`, `Admin`, dan `Transaksi`.

## Penjelasan Class

### Pengguna

Abstract parent class untuk pengguna sistem.

Atribut:
- `id`
- `nama`
- `email`

Method penting:
- constructor
- getter dan setter
- abstract `tampilkanInfo()`

### Customer

Class turunan dari `Pengguna` untuk menyimpan data pembeli tiket.

Atribut tambahan:
- `noHp`

Method penting:
- getter dan setter `noHp`
- override `tampilkanInfo()`

### Admin

Class turunan dari `Pengguna` untuk merepresentasikan admin aplikasi.

Atribut tambahan:
- `username`

Method penting:
- getter dan setter `username`
- override `tampilkanInfo()`

### Tiket

Class untuk menyimpan data kategori tiket.

Atribut:
- `idTiket`
- `kategori`
- `harga`
- `stok`
- `membutuhkanSesi`
- `ArrayList<SesiEvent> daftarSesi`

Untuk VIP dan All Day, stok diambil langsung dari atribut `stok`. Untuk Per-Sesi, stok transaksi diambil dari `SesiEvent`.

### SesiEvent

Class untuk menyimpan data sesi khusus tiket Per-Sesi.

Atribut:
- `idSesi`
- `namaSesi`
- `stok`

Method penting:
- `cekStok(int jumlah)`
- `kurangiStok(int jumlah)`
- `tambahStok(int jumlah)`

### Transaksi

Class untuk menyimpan transaksi booking yang berhasil.

Atribut:
- `idTransaksi`
- `Customer customer`
- `Tiket tiket`
- `String namaSesi`
- `jumlah`
- `totalHarga`
- `paymentMethod`
- `tanggalTransaksi`

Method penting:
- `hitungTotal()`
- `cetakStruk()`
- getter dan setter

### BookingService

Class service yang mengatur logic utama aplikasi.

Atribut:
- `ArrayList<Tiket> daftarTiket`
- `ArrayList<Transaksi> daftarTransaksi`

Method penting:
- `prosesBooking(...)`
- `tambahStokTiket(...)`
- `tambahStokSesi(...)`
- `hitungTotalPendapatan()`
- `generateIdTransaksi()`

### MainFrame

Class GUI utama berbasis Java Swing. Class ini menggunakan `CardLayout` untuk mengatur halaman login, booking, review, payment, receipt, dan admin. Admin page menggunakan tab untuk dashboard, riwayat transaksi, dan kelola stok.

### Main

Class utama untuk menjalankan program. Di sini object `BookingService` dibuat, data awal tiket dimasukkan, lalu GUI `MainFrame` dibuka.

## Hubungan Class Tiket dengan SesiEvent

Class `Tiket` memiliki atribut `ArrayList<SesiEvent> daftarSesi`. Relasi ini digunakan khusus untuk kategori Per-Sesi.

VIP dan All Day:
- `membutuhkanSesi = false`
- stok transaksi memakai `Tiket.stok`
- tidak perlu object `SesiEvent`

Per-Sesi:
- `membutuhkanSesi = true`
- `Tiket.stok` tidak dipakai untuk transaksi
- stok transaksi memakai `SesiEvent.stok`
- setiap sesi berdiri sendiri

## Flow Program

1. Program dijalankan dari `Main.java`.
2. `BookingService` dibuat.
3. Data awal tiket VIP, All Day, dan Per-Sesi dibuat.
4. Empat object `SesiEvent` dimasukkan ke tiket Per-Sesi.
5. Semua tiket dimasukkan ke `BookingService`.
6. `MainFrame` dibuka pada Login Page.
7. User dapat masuk sebagai Guest untuk booking atau login sebagai Admin.
8. Guest melewati Booking Page, Order Review Page, Payment Page, lalu Receipt Page.
9. Admin dapat melihat dashboard, riwayat transaksi, dan menambah stok.

## Flow Payment Modern

1. User mengisi data booking.
2. User klik `Next`.
3. Sistem mengecek validasi input dan stok.
4. User masuk ke Order Review Page.
5. User klik `Continue to Payment`.
6. Sistem mengecek stok ulang.
7. User memilih metode pembayaran.
8. User klik `Confirm Payment`.
9. Sistem mengecek stok ulang untuk mencegah stok berubah di tengah proses.
10. Jika stok cukup, `BookingService.prosesBooking(...)` membuat transaksi dan mengurangi stok.
11. Jika stok tidak cukup, payment gagal, stok tidak berubah, dan transaksi tidak masuk riwayat.

## Flow Booking VIP dan All Day

1. User mengisi nama, nomor HP, email, kategori tiket, dan jumlah tiket.
2. Dropdown sesi otomatis disabled.
3. Sistem mengambil object `Tiket` sesuai kategori.
4. Sistem mengecek stok langsung dari object `Tiket`.
5. Jika stok cukup, stok tiket dikurangi.
6. Object `Transaksi` dibuat.
7. Transaksi masuk ke `ArrayList<Transaksi>`.
8. UI stok, tabel transaksi, total transaksi, dan total pendapatan diperbarui.

## Flow Booking Per-Sesi

1. User mengisi nama, nomor HP, email, kategori Per-Sesi, sesi, dan jumlah tiket.
2. Dropdown sesi otomatis enabled.
3. Sistem memastikan sesi sudah dipilih.
4. Sistem mengambil object `SesiEvent` sesuai pilihan user.
5. Sistem mengecek stok dari `SesiEvent` tersebut.
6. Jika stok cukup, stok sesi yang dipilih dikurangi.
7. Object `Transaksi` dibuat.
8. Transaksi masuk ke riwayat.
9. Stok sesi lain tidak berubah.

## Cara Menjalankan Program

1. Buka folder project di VS Code.
2. Pastikan Java/JDK sudah terinstall.
3. Buka file `src/Main.java`.
4. Klik tombol Run di VS Code, atau jalankan melalui terminal:

```bash
cd src
javac Main.java model/*.java service/*.java view/*.java
java Main
```

## Contoh Skenario Booking

### Booking VIP Berhasil

- Nama: Budi
- No HP: 08123456789
- Email: budi@email.com
- Kategori: VIP
- Jumlah: 2

Hasil:
- Stok VIP dari 50 menjadi 48.
- Kolom sesi di tabel berisi `-`.
- Total pendapatan bertambah Rp300.000.

### Booking All Day Berhasil

- Nama: Sinta
- No HP: 081222333444
- Email: sinta@email.com
- Kategori: All Day
- Jumlah: 3

Hasil:
- Stok All Day dari 100 menjadi 97.
- Kolom sesi di tabel berisi `Semua Sesi`.
- Total pendapatan bertambah Rp300.000.

### Booking Per-Sesi Berhasil

- Nama: Andi
- No HP: 081999888777
- Email: andi@email.com
- Kategori: Per-Sesi
- Sesi: Sesi 2
- Jumlah: 3

Hasil:
- Stok Per-Sesi Sesi 2 dari 40 menjadi 37.
- Stok Sesi 1, Sesi 3, dan Sesi 4 tetap 40.
- Total pendapatan bertambah Rp150.000.

### Booking Gagal Karena Stok Tidak Cukup

Jika user membeli VIP sebanyak 999 tiket, sistem menampilkan pesan error `Stok tiket tidak cukup.`

Hasil:
- Stok VIP tidak berubah.
- Transaksi tidak masuk tabel.
- Total transaksi dan total pendapatan tidak berubah.

## Penjelasan Logic Pengurangan Stok

Logic utama berada di `BookingService.prosesBooking(...)`.

Untuk Per-Sesi:

```java
if (tiket.isMembutuhkanSesi()) {
    if (sesi == null) {
        throw new IllegalArgumentException("Pilih sesi terlebih dahulu.");
    }

    if (sesi.cekStok(jumlah)) {
        sesi.kurangiStok(jumlah);
        buatTransaksi();
    } else {
        throw new IllegalArgumentException("Stok sesi tidak cukup.");
    }
}
```

Untuk VIP dan All Day:

```java
if (tiket.cekStok(jumlah)) {
    tiket.kurangiStok(jumlah);
    buatTransaksi();
} else {
    throw new IllegalArgumentException("Stok tiket tidak cukup.");
}
```

Pada source code asli, `buatTransaksi()` direpresentasikan dengan pembuatan object `Transaksi`, lalu object tersebut dimasukkan ke `daftarTransaksi`.

## Cara Sistem Mencegah Stok Minus

- Jumlah booking harus lebih dari 0.
- Stok dicek menggunakan `cekStok(jumlah)` sebelum dikurangi.
- Method `kurangiStok(jumlah)` tetap melakukan validasi ulang.
- Setter stok menolak nilai minus.
- Jika stok tidak cukup, program melempar `IllegalArgumentException`.
- Transaksi baru dibuat setelah user menekan `Confirm Payment` dan stok berhasil dikurangi.
- Jika error terjadi, transaksi tidak ditambahkan ke `ArrayList<Transaksi>`.

Dengan alur ini, stok tidak akan minus dan transaksi gagal tidak masuk ke riwayat.
