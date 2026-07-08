package model;

public class Admin extends Pengguna {
    private String username;

    public Admin(String id, String nama, String email, String username) {
        super(id, nama, email);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String tampilkanInfo() {
        return "Admin: " + getNama() + " | Username: " + username + " | Email: " + getEmail();
    }
}
