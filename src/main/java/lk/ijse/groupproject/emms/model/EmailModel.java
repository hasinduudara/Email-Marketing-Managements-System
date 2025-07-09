package lk.ijse.groupproject.emms.model;

public class EmailModel {
    private String email;
    private String clientName;
    private String gender;
    private int age;
    private String job;

    public EmailModel(String email, String clientName, String gender, int age, String job) {
        this.email = email;
        this.clientName = clientName;
        this.gender = gender;
        this.age = age;
        this.job = job;
    }

    public String getEmail() {
        return email;
    }

    public String getClientName() {
        return clientName;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public String getJob() {
        return job;
    }

    @Override
    public String toString() {
        return email + " (" + clientName + ")";
    }
}
