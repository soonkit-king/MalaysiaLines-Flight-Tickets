package model;

public class Passenger {

    private int bookingId;
    private String firstName;
    private String lastName;
    private String nationality;
    private String countryOfIssue;
    private String passportNumber;
    private String gender;
    private String dateOfBirth;
    private String passportExpiry;

    public Passenger() {
        // Default constructor
    }

    public Passenger(int bookingId, String firstName, String lastName, String nationality, String countryOfIssue, String passportNumber, String gender, String dateOfBirth, String passportExpiry) {
        this.bookingId = bookingId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationality = nationality;
        this.countryOfIssue = countryOfIssue;
        this.passportNumber = passportNumber;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.passportExpiry = passportExpiry;
    }

    // Getters and setters for all fields
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public String getCountryOfIssue() { return countryOfIssue; }
    public void setCountryOfIssue(String countryOfIssue) { this.countryOfIssue = countryOfIssue; }

    public String getPassportNumber() { return passportNumber; }
    public void setPassportNumber(String passportNumber) { this.passportNumber = passportNumber; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getPassportExpiry() { return passportExpiry; }
    public void setPassportExpiry(String passportExpiry) { this.passportExpiry = passportExpiry; }
}