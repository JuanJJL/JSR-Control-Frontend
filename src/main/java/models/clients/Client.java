package models.clients;


public class Client {
    private int id;
    private String name;
    private String email;
    private int age;
    private String details;
    private String created_at;
    private String updated_at;

    // Constructor vacío
    public Client() {}

    // Constructor completo
    public Client(int id, String name, String email, int age, String details, String created_at, String updated_at) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.details = details;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getCreated_at() { return created_at; }
    public void setCreated_at(String created_at) { this.created_at = created_at; }

    public String getUpdated_at() { return updated_at; }
    public void setUpdated_at(String updated_at) { this.updated_at = updated_at; }

    @Override
    public String toString() {
        return "Client{id=" + id + ", name='" + name + "', email='" + email + "', age=" + age + "}";
    }
}