package ptit.com.ptitmanager.object;

public class StudySchedule {
    private String day;
    private String tvTotalPeriod;
    private String currentDay;

    public StudySchedule(String day, String tvTotalPeriod, String currentDay) {
        this.day = day;
        this.tvTotalPeriod = tvTotalPeriod;
        this.currentDay = currentDay;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTvTotalPeriod() {
        return tvTotalPeriod;
    }

    public void setTvTotalPeriod(String tvTotalPeriod) {
        this.tvTotalPeriod = tvTotalPeriod;
    }

    public String getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(String currentDay) {
        this.currentDay = currentDay;
    }
}
