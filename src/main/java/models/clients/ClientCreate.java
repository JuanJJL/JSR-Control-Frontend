package models.clients;

public class ClientCreate {
    private String name;
    private String email;
    private int age;
    private String details;

    // Constructor
    public ClientCreate(String name, String email, int age, String details) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.details = details;
    }

    // Getters y Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}