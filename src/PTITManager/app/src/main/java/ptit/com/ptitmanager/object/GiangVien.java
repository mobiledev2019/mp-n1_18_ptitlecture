package ptit.com.ptitmanager.object;

public class GiangVien {
    private String ID;
    private String tenGiangVien;
    private String email;
    private String password;

    public GiangVien(String ID, String tenGiangVien, String email, String password) {
        this.ID = ID;
        this.tenGiangVien = tenGiangVien;
        this.email = email;
        this.password = password;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTenGiangVien() {
        return tenGiangVien;
    }

    public void setTenGiangVien(String tenGiangVien) {
        this.tenGiangVien = tenGiangVien;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
