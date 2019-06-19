package ptit.com.ptitmanager.object;

public class NhomMonHoc {
    private String ID;
    private String monHocID;
    private String sttNhom;
    private String linkNhom;

    public NhomMonHoc(String ID, String monHocID, String sttNhom, String linkNhom) {
        this.ID = ID;
        this.monHocID = monHocID;
        this.sttNhom = sttNhom;
        this.linkNhom = linkNhom;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getMonHocID() {
        return monHocID;
    }

    public void setMonHocID(String monHocID) {
        this.monHocID = monHocID;
    }

    public String getSttNhom() {
        return sttNhom;
    }

    public void setSttNhom(String sttNhom) {
        this.sttNhom = sttNhom;
    }

    public String getLinkNhom() {
        return linkNhom;
    }

    public void setLinkNhom(String linkNhom) {
        this.linkNhom = linkNhom;
    }
}
