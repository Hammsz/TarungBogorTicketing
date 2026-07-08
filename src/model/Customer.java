package model;

public class Customer extends Pengguna {
    private String noHp;

    public Customer(String id, String nama, String email, String noHp) {
        super(id, nama, email);
        this.noHp = noHp;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    @Override
    public String tampilkanInfo() {
        return "Customer: " + getNama() + " | Email: " + getEmail() + " | No HP: " + noHp;
    }
}
