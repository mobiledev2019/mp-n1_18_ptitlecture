package ptit.com.ptitmanager.object;

public class HocKy {
    private String ID;
    private String tenHocKy;
    private String ngayBatDau;
    private String ngayKetThuc;

    public HocKy(String ID, String tenHocKy, String ngayBatDau, String ngayKetThuc) {
        this.ID = ID;
        this.tenHocKy = tenHocKy;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTenHocKy() {
        return tenHocKy;
    }

    public void setTenHocKy(String tenHocKy) {
        this.tenHocKy = tenHocKy;
    }

    public String getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(String ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public String getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(String ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }
}
