package ptit.com.ptitmanager.object;

public class DanhSachSinhVien {
    private String ID;
    private String lopHocID;
    private String sinhVienID;
    private String diemCC;
    private String diemBT;
    private String diemKT;
    private String diemTH;
    private String diemHK;
    private String diemTB;

    public DanhSachSinhVien(String ID, String lopHocID, String sinhVienID, String diemCC, String diemBT, String diemKT, String diemTH, String diemHK, String diemTB) {
        this.ID = ID;
        this.lopHocID = lopHocID;
        this.sinhVienID = sinhVienID;
        this.diemCC = diemCC;
        this.diemBT = diemBT;
        this.diemKT = diemKT;
        this.diemTH = diemTH;
        this.diemHK = diemHK;
        this.diemTB = diemTB;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getLopHocID() {
        return lopHocID;
    }

    public void setLopHocID(String lopHocID) {
        this.lopHocID = lopHocID;
    }

    public String getSinhVienID() {
        return sinhVienID;
    }

    public void setSinhVienID(String sinhVienID) {
        this.sinhVienID = sinhVienID;
    }

    public String getDiemCC() {
        return diemCC;
    }

    public void setDiemCC(String diemCC) {
        this.diemCC = diemCC;
    }

    public String getDiemBT() {
        return diemBT;
    }

    public void setDiemBT(String diemBT) {
        this.diemBT = diemBT;
    }

    public String getDiemKT() {
        return diemKT;
    }

    public void setDiemKT(String diemKT) {
        this.diemKT = diemKT;
    }

    public String getDiemTH() {
        return diemTH;
    }

    public void setDiemTH(String diemTH) {
        this.diemTH = diemTH;
    }

    public String getDiemHK() {
        return diemHK;
    }

    public void setDiemHK(String diemHK) {
        this.diemHK = diemHK;
    }

    public String getDiemTB() {
        return diemTB;
    }

    public void setDiemTB(String diemTB) {
        this.diemTB = diemTB;
    }

    @Override
    public String toString() {
        return "DanhSachSinhVien{" +
                "ID='" + ID + '\'' +
                ", lopHocID='" + lopHocID + '\'' +
                ", sinhVienID='" + sinhVienID + '\'' +
                ", diemCC='" + diemCC + '\'' +
                ", diemBT='" + diemBT + '\'' +
                ", diemKT='" + diemKT + '\'' +
                ", diemTH='" + diemTH + '\'' +
                ", diemHK='" + diemHK + '\'' +
                ", diemTB='" + diemTB + '\'' +
                '}';
    }
}
