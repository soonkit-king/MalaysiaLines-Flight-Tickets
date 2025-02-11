package model;

public class Passenger {
    private String firstName;
    private String lastName;
    private String gender;
    private String dateOfBirth;
    private String nationality;
    private String countryOfIssue;
    private String passportNumber;
    private String passportExpiry;

    public Passenger(String firstName, String lastName, String gender, String dateOfBirth, String nationality, String countryOfIssue, String passportNumber, String passportExpiry) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.countryOfIssue = countryOfIssue;
        this.passportNumber = passportNumber;
        this.passportExpiry = passportExpiry;
    }

    public Passenger(int bookingId, String alice, String smith, String female, String date, String malaysia, String malaysia1, String a1234567, String date1) {
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getGender() { return gender; }
    public String getDateOfBirth() { return dateOfBirth; }
    public String getNationality() { return nationality; }
    public String getCountryOfIssue() { return countryOfIssue; }
    public String getPassportNumber() { return passportNumber; }
    public String getPassportExpiry() { return passportExpiry; }

    public byte[] getBookingId() {
        return new byte[0];
    }
}
