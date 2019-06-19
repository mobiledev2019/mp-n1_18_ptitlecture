package ptit.com.ptitmanager.object;

public class LichDay {
    private String ID;
    private String hocKyID;
    private String lopHocID;
    private String giangVienID;
    private String kip;
    private String ngayDay;
    private String checkIn;
    private String kieuTietHoc;

    public LichDay(String ID, String hocKyID, String lopHocID, String giangVienID, String kip, String ngayDay, String checkIn, String kieuTietHoc) {
        this.ID = ID;
        this.hocKyID = hocKyID;
        this.lopHocID = lopHocID;
        this.giangVienID = giangVienID;
        this.kip = kip;
        this.ngayDay = ngayDay;
        this.checkIn = checkIn;
        this.kieuTietHoc = kieuTietHoc;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getHocKyID() {
        return hocKyID;
    }

    public void setHocKyID(String hocKyID) {
        this.hocKyID = hocKyID;
    }

    public String getLopHocID() {
        return lopHocID;
    }

    public void setLopHocID(String lopHocID) {
        this.lopHocID = lopHocID;
    }

    public String getGiangVienID() {
        return giangVienID;
    }

    public void setGiangVienID(String giangVienID) {
        this.giangVienID = giangVienID;
    }

    public String getKip() {
        return kip;
    }

    public void setKip(String kip) {
        this.kip = kip;
    }

    public String getNgayDay() {
        return ngayDay;
    }

    public void setNgayDay(String ngayDay) {
        this.ngayDay = ngayDay;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getKieuTietHoc() {
        return kieuTietHoc;
    }

    public void setKieuTietHoc(String kieuTietHoc) {
        this.kieuTietHoc = kieuTietHoc;
    }
}
