package lk.ijse.groupproject.emms.dto.tm;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EmailTM {
    private final IntegerProperty id;
    private final StringProperty email;
    private final StringProperty clientName;
    private final StringProperty gender;
    private final IntegerProperty age;
    private final StringProperty job;

    // Default constructor
    public EmailTM() {
        this.id = new SimpleIntegerProperty();
        this.email = new SimpleStringProperty();
        this.clientName = new SimpleStringProperty();
        this.gender = new SimpleStringProperty();
        this.age = new SimpleIntegerProperty();
        this.job = new SimpleStringProperty();
    }

    // Constructor with all parameters
    public EmailTM(int id, String email, String clientName, String gender, int age, String job) {
        this.id = new SimpleIntegerProperty(id);
        this.email = new SimpleStringProperty(email);
        this.clientName = new SimpleStringProperty(clientName);
        this.gender = new SimpleStringProperty(gender);
        this.age = new SimpleIntegerProperty(age);
        this.job = new SimpleStringProperty(job);
    }

    // Constructor without id (for new records)
    public EmailTM(String email, String clientName, String gender, int age, String job) {
        this.id = new SimpleIntegerProperty();
        this.email = new SimpleStringProperty(email);
        this.clientName = new SimpleStringProperty(clientName);
        this.gender = new SimpleStringProperty(gender);
        this.age = new SimpleIntegerProperty(age);
        this.job = new SimpleStringProperty(job);
    }

    // ID Property methods
    public IntegerProperty idProperty() {
        return id;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    // Email Property methods
    public StringProperty emailProperty() {
        return email;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    // Client Name Property methods
    public StringProperty clientNameProperty() {
        return clientName;
    }

    public String getClientName() {
        return clientName.get();
    }

    public void setClientName(String clientName) {
        this.clientName.set(clientName);
    }

    // Gender Property methods
    public StringProperty genderProperty() {
        return gender;
    }

    public String getGender() {
        return gender.get();
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    // Age Property methods
    public IntegerProperty ageProperty() {
        return age;
    }

    public int getAge() {
        return age.get();
    }

    public void setAge(int age) {
        this.age.set(age);
    }

    // Job Property methods
    public StringProperty jobProperty() {
        return job;
    }

    public String getJob() {
        return job.get();
    }

    public void setJob(String job) {
        this.job.set(job);
    }

    @Override
    public String toString() {
        return "EmailTM{" +
                "id=" + getId() +
                ", email='" + getEmail() + '\'' +
                ", clientName='" + getClientName() + '\'' +
                ", gender='" + getGender() + '\'' +
                ", age=" + getAge() +
                ", job='" + getJob() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EmailTM emailTM = (EmailTM) obj;
        return getId() == emailTM.getId();
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(getId());
    }
}