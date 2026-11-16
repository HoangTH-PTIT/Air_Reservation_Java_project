package Model;

public class Passenger {
    private String id;
    private String fullName;
    private String email;
    private String phone;
    private String passportNo;

    public Passenger(String id, String fullName, String email, String phone, String passportNo) {
        if(id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Please enter your ID!");
        }
        this.id = id;
        if(fullName == null || fullName.isEmpty()) {
            throw new IllegalArgumentException("Please enter your full name!");
        }
        this.fullName = fullName;
        if(email == null) {
            throw new IllegalArgumentException("Invalid email: must contain '@'.");
        }
        int at = email.indexOf('@');
        if(at <= 0 || at != email.lastIndexOf('@') || at == email.length() -1) {
            throw new IllegalArgumentException("Invalid email: must have an username and a domain around '@'.");
        }
        this.email = email;
        if(phone == null || !phone.matches("0\\d+")) {
            throw new IllegalArgumentException("Invalid phone: must start with 0 and contain digits only.");
        }
        this.phone = phone;
        if(passportNo == null || passportNo.isEmpty()) {
            throw new IllegalArgumentException("Please enter your passport number!");
        }
        this.passportNo = passportNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }
}
