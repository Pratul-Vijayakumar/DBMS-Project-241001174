package railwayTicketing;

import java.math.BigDecimal;
import java.sql.Time;

public class Train {
    private int trainId;
    private String trainName;
    private String source;
    private String destination;
    private Time departureTime;
    private Time arrivalTime;
    private BigDecimal fare;

    // Constructor
    public Train(int trainId, String trainName, String source, String destination, 
                 Time departureTime, Time arrivalTime, BigDecimal fare) {
        this.trainId = trainId;
        this.trainName = trainName;
        this.source = source;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.fare = fare;
    }

    // Getters
    public int getTrainId() {
        return trainId;
    }

    public String getTrainName() {
        return trainName;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public Time getDepartureTime() {
        return departureTime;
    }

    public Time getArrivalTime() {
        return arrivalTime;
    }

    public BigDecimal getFare() {
        return fare;
    }

    // Setters
    public void setTrainId(int trainId) {
        this.trainId = trainId;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDepartureTime(Time departureTime) {
        this.departureTime = departureTime;
    }

    public void setArrivalTime(Time arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setFare(BigDecimal fare) {
        this.fare = fare;
    }
}
