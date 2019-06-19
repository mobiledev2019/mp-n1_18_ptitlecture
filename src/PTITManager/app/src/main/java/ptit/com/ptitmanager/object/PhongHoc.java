package ptit.com.ptitmanager.object;

public class PhongHoc {
    private String ID;
    private String tenPhongHoc;

    public PhongHoc(String ID, String tenPhongHoc) {
        this.ID = ID;
        this.tenPhongHoc = tenPhongHoc;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTenPhongHoc() {
        return tenPhongHoc;
    }

    public void setTenPhongHoc(String tenPhongHoc) {
        this.tenPhongHoc = tenPhongHoc;
    }
}
