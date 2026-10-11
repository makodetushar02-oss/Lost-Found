public class User {
    private int userId;
    private String userName;
    private String phNo;
    private String email;
    private String role;

    public User(int userId, String userName, String phNo, String email, String role) {
        this.userId = userId;
        this.userName = userName;
        this.phNo = phNo;
        this.email = email;
        this.role = role;
    }

    // Order matches every caller: new User(name, phNo, email, "Student")
    public User(String userName, String phNo, String email, String role) {
        this(0, userName, phNo, email, role);
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getPhNo() { return phNo; }
    public void setPhNo(String phNo) { this.phNo = phNo; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
