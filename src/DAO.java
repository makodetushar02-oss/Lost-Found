import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DAO {
    private static final String URL = "jdbc:mysql://localhost:3306/lost_and_found";
    private static final String USER = "root";
    private static final String PASSWORD="M@kode(02)";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Returns the new userId, or -1 on failure.
    public int addUser(User user) {
        String query = "INSERT INTO users(userName, phNo, email, role) VALUES(?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getPhNo());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getRole());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    System.out.println("User registered successfully! Your user ID is: " + id);
                    return id;
                }
            }
        } catch (SQLException e) {
            System.out.println("User registration failed...");
            e.printStackTrace();
        }
        return -1;
    }

    // CHANGED: now returns true/false so the web API can tell the browser if saving worked.
    public boolean addItem(Item item) {
        String query = "INSERT INTO item(itemName, description, color, category, itemType, userId, location) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, item.getItemName());
            ps.setString(2, item.getDes());
            ps.setString(3, item.getColor());
            ps.setString(4, item.getCategory());
            ps.setString(5, item.getType());
            ps.setInt(6, item.getUserId());
            ps.setString(7, item.getLocation());
            ps.executeUpdate();
            System.out.println("Item reported successfully!");
            return true;
        } catch (SQLException e) {
            System.out.println("Item reporting failed....");
            e.printStackTrace();
            return false;
        }
    }

    // CHANGED: returns boolean (see addItem).
    public boolean addClaim(Claim claim) {
        String query = "INSERT INTO claim(proof, userId, itemName, location, status) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, claim.getProof());
            ps.setInt(2, claim.getClaimedBy());
            ps.setString(3, claim.getItemName());
            ps.setString(4, claim.getLocation());
            ps.setString(5, claim.getStatus());
            ps.executeUpdate();
            System.out.println("Claim added successfully");
            return true;
        } catch (SQLException e) {
            System.out.println("Failed to add claim.....");
            e.printStackTrace();
            return false;
        }
    }

    // CHANGED: returns true only if a claim row was really updated.
    public boolean updateClaim(int claimId, String status) {
        String query = "UPDATE claim SET status = ? WHERE claimId = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, status);
            ps.setInt(2, claimId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Claim " + claimId + " marked as " + status);
                return true;
            }
            System.out.println("No claim found with ID " + claimId);
        } catch (SQLException e) {
            System.out.println("Failed to update claim.....");
            e.printStackTrace();
        }
        return false;
    }

    // NEW (for the website): list all items of one type, newest first.
    // type = "LOST_ITEM" or "FOUND_ITEM".
    public List<Item> getItems(String type) {
        List<Item> list = new ArrayList<>();
        String query = "SELECT itemId, itemName, description, color, category, userId, location "
                + "FROM item WHERE itemType = ? ORDER BY itemId DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, type);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("itemId");
                    String name = rs.getString("itemName");
                    String des = rs.getString("description");
                    String color = rs.getString("color");
                    String cat = rs.getString("category");
                    int uid = rs.getInt("userId");
                    String loc = rs.getString("location");
                    if ("FOUND_ITEM".equals(type)) {
                        list.add(new FoundItem(id, name, des, color, cat, uid, loc));
                    } else {
                        list.add(new LostItem(id, name, des, color, cat, uid, loc));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to load items.....");
            e.printStackTrace();
        }
        return list;
    }

    // NEW (for the admin page): list all claims, newest first.
    public List<Claim> getClaims() {
        List<Claim> list = new ArrayList<>();
        String query = "SELECT claimId, proof, userId, itemName, location, status FROM claim ORDER BY claimId DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Claim(rs.getInt("claimId"), rs.getString("proof"), rs.getInt("userId"),
                        rs.getString("itemName"), rs.getString("location"), rs.getString("status")));
            }
        } catch (SQLException e) {
            System.out.println("Failed to load claims.....");
            e.printStackTrace();
        }
        return list;
    }
}
