package models.user;

public class UserUpdate {
    private String username;
    private int role_id;

    public UserUpdate() {}

    public UserUpdate(String username, int role_id) {
        this.username = username;
        this.role_id = role_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }
}
