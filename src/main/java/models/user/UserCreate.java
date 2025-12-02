package models.user;

public class UserCreate {
    private String username;
    private String password;
    private int role_id;

    public UserCreate() {}

    public UserCreate(String username, String password, int role_id) {
        this.username = username;
        this.password = password;
        this.role_id = role_id;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getRole_id() { return role_id; }
    public void setRole_id(int role_id) { this.role_id = role_id; }
}