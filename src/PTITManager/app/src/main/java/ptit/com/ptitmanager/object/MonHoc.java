package ptit.com.ptitmanager.object;

public class MonHoc {
    private String ID;
    private String tenMonHoc;

    public MonHoc(String ID, String tenMonHoc) {
        this.ID = ID;
        this.tenMonHoc = tenMonHoc;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTenMonHoc() {
        return tenMonHoc;
    }

    public void setTenMonHoc(String tenMonHoc) {
        this.tenMonHoc = tenMonHoc;
    }
}
