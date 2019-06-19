package ptit.com.ptitmanager.object;

public class DiemDanh {
    private String ID;
    private String lichDayID;
    private String danhSachSVID;
    private String trangThai;

    public DiemDanh(String ID, String lichDayID, String danhSachSVID, String trangThai) {
        this.ID = ID;
        this.lichDayID = lichDayID;
        this.danhSachSVID = danhSachSVID;
        this.trangThai = trangThai;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getLichDayID() {
        return lichDayID;
    }

    public void setLichDayID(String lichDayID) {
        this.lichDayID = lichDayID;
    }

    public String getDanhSachSVID() {
        return danhSachSVID;
    }

    public void setDanhSachSVID(String danhSachSVID) {
        this.danhSachSVID = danhSachSVID;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
