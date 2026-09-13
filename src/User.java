public class User {
    private int userID;
    private String userName;
    private String role;
    private String email;
    private String phone;

    public User(int userID, String userName, String role, String email, String phone) {
        this.userID = userID;
        this.userName = userName;
        this.role= role;
        this.email = email;
        this.phone = phone;
    }
    public User(String userName, String role, String email, String phone) {
        this.userName = userName;
        this.role= role;
        this.email = email;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
