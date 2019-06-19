package ptit.com.ptitmanager.object;

public class Subject {
    private String lopHocID;
    private String subjectName;
    private String sttNhom;
    private String classRoom;
    private String studyTime;
    private String typeSubject;
    private String lichDayID;
    private String status;
    private String ngayDay;

    public Subject() {
    }

    public Subject(String lopHocID, String subjectName, String sttNhom, String classRoom, String studyTime, String typeSubject, String lichDayID, String status, String ngayDay) {
        this.lopHocID = lopHocID;
        this.subjectName = subjectName;
        this.sttNhom = sttNhom;
        this.classRoom = classRoom;
        this.studyTime = studyTime;
        this.typeSubject = typeSubject;
        this.lichDayID = lichDayID;
        this.status = status;
        this.ngayDay = ngayDay;
    }

    public String getNgayDay() {
        return ngayDay;
    }

    public void setNgayDay(String ngayDay) {
        this.ngayDay = ngayDay;
    }

    public String getLopHocID() {
        return lopHocID;
    }

    public void setLopHocID(String lopHocID) {
        this.lopHocID = lopHocID;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSttNhom() {
        return sttNhom;
    }

    public void setSttNhom(String sttNhom) {
        this.sttNhom = sttNhom;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public String getStudyTime() {
        return studyTime;
    }

    public void setStudyTime(String studyTime) {
        this.studyTime = studyTime;
    }

    public String getTypeSubject() {
        return typeSubject;
    }

    public void setTypeSubject(String typeSubject) {
        this.typeSubject = typeSubject;
    }

    public String getLichDayID() {
        return lichDayID;
    }

    public void setLichDayID(String lichDayID) {
        this.lichDayID = lichDayID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
