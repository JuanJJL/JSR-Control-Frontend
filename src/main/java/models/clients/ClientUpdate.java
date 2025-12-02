package models.clients;

public class ClientUpdate {
    private String name;
    private String email;
    private Integer age;
    private String details;

    // Constructor vacío
    public ClientUpdate() {}

    // Getters y Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}