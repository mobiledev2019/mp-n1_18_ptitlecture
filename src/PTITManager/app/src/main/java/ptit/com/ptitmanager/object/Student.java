package ptit.com.ptitmanager.object;

public class Student {
    private int image;
    private String maSV;
    private String fullName;
    private String DiemCC;
    private String DiemKT;
    private String DiemBT;
    private String DSSVID;
    private int status = 1;

    public Student() {
    }

    public Student(int image, String maSV, String fullName, String diemCC, String diemKT, String diemBT, String DSSVID, int status) {
        this.image = image;
        this.maSV = maSV;
        this.fullName = fullName;
        DiemCC = diemCC;
        DiemKT = diemKT;
        DiemBT = diemBT;
        this.DSSVID = DSSVID;
        this.status = status;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDiemCC() {
        return DiemCC;
    }

    public void setDiemCC(String diemCC) {
        DiemCC = diemCC;
    }

    public String getDiemKT() {
        return DiemKT;
    }

    public void setDiemKT(String diemKT) {
        DiemKT = diemKT;
    }

    public String getDiemBT() {
        return DiemBT;
    }

    public void setDiemBT(String diemBT) {
        DiemBT = diemBT;
    }

    public String getDSSVID() {
        return DSSVID;
    }

    public void setDSSVID(String DSSVID) {
        this.DSSVID = DSSVID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
