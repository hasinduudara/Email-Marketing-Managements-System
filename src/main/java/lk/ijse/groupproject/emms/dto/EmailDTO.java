package lk.ijse.groupproject.emms.dto;

public class EmailDTO {
    private int id;
    private String email;
    private String clientName;
    private String gender;
    private int age;
    private String job;

    // Default constructor
    public EmailDTO() {
    }

    // Constructor with all parameters
    public EmailDTO(int id, String email, String clientName, String gender, int age, String job) {
        this.id = id;
        this.email = email;
        this.clientName = clientName;
        this.gender = gender;
        this.age = age;
        this.job = job;
    }

    // Constructor without id (for new records)
    public EmailDTO(String email, String clientName, String gender, int age, String job) {
        this.email = email;
        this.clientName = clientName;
        this.gender = gender;
        this.age = age;
        this.job = job;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    @Override
    public String toString() {
        return "EmailDTO{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", clientName='" + clientName + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", job='" + job + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EmailDTO emailDTO = (EmailDTO) obj;
        return id == emailDTO.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}