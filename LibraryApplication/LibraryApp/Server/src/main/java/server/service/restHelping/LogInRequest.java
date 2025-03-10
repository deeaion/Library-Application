package server.service.restHelping;

public class LogInRequest {
    private String username;
    private String password;

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public LogInRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LogInRequest() {
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
