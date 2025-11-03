package railwayTicketing;
import java.sql.Date;

public class Ticket {
    private int ticketId;
    private int passengerId;
    private int trainId;
    private Date dateOfJourney;
    private String bookingStatus;
    private String seat;
    private String passengerName;
    private int age;
    private String gender;
    private String contactNo;

    // Constructor
    public Ticket(int ticketId, int passengerId, int trainId, Date dateOfJourney, 
                  String bookingStatus, String seat, String passengerName, 
                  int age, String gender, String contactNo) {
        this.ticketId = ticketId;
        this.passengerId = passengerId;
        this.trainId = trainId;
        this.dateOfJourney = dateOfJourney;
        this.bookingStatus = bookingStatus;
        this.seat = seat;
        this.passengerName = passengerName;
        this.age = age;
        this.gender = gender;
        this.contactNo = contactNo;
    }

    // Getters
    public int getTicketId() {
        return ticketId;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public int getTrainId() {
        return trainId;
    }

    public Date getDateOfJourney() {
        return dateOfJourney;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public String getSeat() {
        return seat;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getContactNo() {
        return contactNo;
    }

    // Setters
    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public void setTrainId(int trainId) {
        this.trainId = trainId;
    }

    public void setDateOfJourney(Date dateOfJourney) {
        this.dateOfJourney = dateOfJourney;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }
}
