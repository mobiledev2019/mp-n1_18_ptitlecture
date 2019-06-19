package ptit.com.ptitmanager.object;

public class LopHoc {
    private String id;
    private String nhomMonHocID;
    private String phongHocID;
    private String typeLopHoc;

    public LopHoc() {
    }

    public LopHoc(String id, String nhomMonHocID, String phongHocID, String typeLopHoc) {
        this.id = id;
        this.nhomMonHocID = nhomMonHocID;
        this.phongHocID = phongHocID;
        this.typeLopHoc = typeLopHoc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNhomMonHocID() {
        return nhomMonHocID;
    }

    public void setNhomMonHocID(String nhomMonHocID) {
        this.nhomMonHocID = nhomMonHocID;
    }

    public String getPhongHocID() {
        return phongHocID;
    }

    public void setPhongHocID(String phongHocID) {
        this.phongHocID = phongHocID;
    }

    public String getTypeLopHoc() {
        return typeLopHoc;
    }

    public void setTypeLopHoc(String typeLopHoc) {
        this.typeLopHoc = typeLopHoc;
    }

    @Override
    public String toString() {
        return "LopHoc{" +
                "id='" + id + '\'' +
                ", nhomMonHocID='" + nhomMonHocID + '\'' +
                ", phongHocID='" + phongHocID + '\'' +
                ", typeLopHoc='" + typeLopHoc + '\'' +
                '}';
    }
}
