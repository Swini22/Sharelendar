package ch.gibb.share.sharelendar.model;

public class Admin {
    private String username;
    private String password;

    // -----Konstruktor------
    public Admin() {
        super();
    }

    // --------Getter---------
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // --------Setter---------
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        String result = "Admin: " + username ;
        return result;
    }
}
