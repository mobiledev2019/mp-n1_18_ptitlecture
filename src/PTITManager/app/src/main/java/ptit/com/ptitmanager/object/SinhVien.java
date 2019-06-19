package ptit.com.ptitmanager.object;

public class SinhVien {
    private String ID;
    private String tenSV;
    private String maSV;
    private String lop;

    public SinhVien(String ID, String tenSV, String maSV, String lop) {
        this.ID = ID;
        this.tenSV = tenSV;
        this.maSV = maSV;
        this.lop = lop;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTenSV() {
        return tenSV;
    }

    public void setTenSV(String tenSV) {
        this.tenSV = tenSV;
    }

    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV;
    }

    public String getLop() {
        return lop;
    }

    public void setLop(String lop) {
        this.lop = lop;
    }

    @Override
    public String toString() {
        return "SinhVien{" +
                "ID='" + ID + '\'' +
                ", tenSV='" + tenSV + '\'' +
                ", maSV='" + maSV + '\'' +
                ", lop='" + lop + '\'' +
                '}';
    }
}
