package ro.videanuadrian.mobile.smarthome;

public class TemperatureDisplayDTO {

    private String sensor;
    private String date;
    private Double temp;

    public TemperatureDisplayDTO(String sensor, String date, Double temp) {
        this.sensor = sensor;
        this.date = date;
        this.temp = temp;
    }
    public TemperatureDisplayDTO() {
    }

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }


}
