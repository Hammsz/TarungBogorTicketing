package model;

public abstract class Pengguna {
    private String id;
    private String nama;
    private String email;

    public Pengguna(String id, String nama, String email) {
        this.id = id;
        this.nama = nama;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public abstract String tampilkanInfo();
}
