import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * NEW: the bridge between the website (browser) and your DAO (database).
 *
 * Browser (index.html + JavaScript)  --HTTP-->  WebServer (this file)  --JDBC-->  MySQL
 *
 * Uses only the HTTP server that is built into Java, so no extra libraries are needed.
 * Run THIS class's main() instead of Main.java, then open http://localhost:8080
 */
public class WebServer {
    private static final int PORT = 8080;
    // Same admin password as the console version. Move it out of the code later.
    private static final String ADMIN_PASSWORD = "M@kode(02)";
    private static final DAO dao = new DAO();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // Each URL path is connected to one Java method below.
        server.createContext("/api/register", ex -> handle(ex, WebServer::register));
        server.createContext("/api/items", ex -> handle(ex, WebServer::items));
        server.createContext("/api/claims", ex -> handle(ex, WebServer::claims));
        server.createContext("/api/claims/update", ex -> handle(ex, WebServer::updateClaim));
        server.createContext("/", WebServer::serveHomePage);

        server.start();
        System.out.println("Lost & Found website running at http://localhost:" + PORT);
    }

    // ---------- API endpoints ----------

    // POST /api/register   (name, phNo, email)  ->  {"userId": 5}
    private static void register(HttpExchange ex) throws IOException {
        if (!method(ex, "POST")) return;
        Map<String, String> f = readForm(ex);
        String name = f.getOrDefault("name", "").trim();
        String phNo = f.getOrDefault("phNo", "").trim();
        String email = f.getOrDefault("email", "").trim();
        if (name.isEmpty() || phNo.isEmpty() || email.isEmpty()) {
            error(ex, 400, "Name, phone and email are required.");
            return;
        }
        int id = dao.addUser(new User(name, phNo, email, "Student"));
        if (id < 0) error(ex, 400, "Registration failed. This email may already be registered.");
        else sendJson(ex, 200, "{\"userId\":" + id + "}");
    }

    // GET  /api/items?type=LOST_ITEM|FOUND_ITEM  -> list of items
    // POST /api/items  (type, itemName, description, color, category, userId, location)
    private static void items(HttpExchange ex) throws IOException {
        if (ex.getRequestMethod().equals("GET")) {
            String type = queryParam(ex, "type");
            if (!"LOST_ITEM".equals(type) && !"FOUND_ITEM".equals(type)) {
                error(ex, 400, "type must be LOST_ITEM or FOUND_ITEM");
                return;
            }
            List<Item> list = dao.getItems(type);
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < list.size(); i++) {
                Item it = list.get(i);
                if (i > 0) sb.append(",");
                sb.append("{\"itemId\":").append(it.getItemId())
                        .append(",\"itemName\":").append(q(it.getItemName()))
                        .append(",\"description\":").append(q(it.getDes()))
                        .append(",\"color\":").append(q(it.getColor()))
                        .append(",\"category\":").append(q(it.getCategory()))
                        .append(",\"location\":").append(q(it.getLocation()))
                        .append("}");
            }
            sendJson(ex, 200, sb.append("]").toString());
            return;
        }
        if (!method(ex, "POST")) return;

        Map<String, String> f = readForm(ex);
        String type = f.getOrDefault("type", "");
        String itemName = f.getOrDefault("itemName", "").trim();
        String description = f.getOrDefault("description", "").trim();
        String color = f.getOrDefault("color", "").trim();
        String category = f.getOrDefault("category", "").trim();
        String location = f.getOrDefault("location", "").trim();
        Integer userId = parseInt(f.get("userId"));
        if (itemName.isEmpty() || userId == null) {
            error(ex, 400, "Item name and a valid user ID are required.");
            return;
        }
        Item item;
        if ("LOST_ITEM".equals(type)) item = new LostItem(itemName, description, color, category, userId, location);
        else if ("FOUND_ITEM".equals(type)) item = new FoundItem(itemName, description, color, category, userId, location);
        else {
            error(ex, 400, "type must be LOST_ITEM or FOUND_ITEM");
            return;
        }
        if (dao.addItem(item)) sendJson(ex, 200, "{\"ok\":true}");
        else error(ex, 400, "Could not save. Check that your user ID exists (register first).");
    }

    // POST /api/claims (proof, userId, itemName, location)  -> creates a Pending claim
    // GET  /api/claims  (header X-Admin-Password)          -> list of all claims
    private static void claims(HttpExchange ex) throws IOException {
        if (ex.getRequestMethod().equals("GET")) {
            if (!isAdmin(ex)) {
                error(ex, 401, "Unauthorized access");
                return;
            }
            List<Claim> list = dao.getClaims();
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < list.size(); i++) {
                Claim c = list.get(i);
                if (i > 0) sb.append(",");
                sb.append("{\"claimId\":").append(c.getClaimId())
                        .append(",\"proof\":").append(q(c.getProof()))
                        .append(",\"userId\":").append(c.getClaimedBy())
                        .append(",\"itemName\":").append(q(c.getItemName()))
                        .append(",\"location\":").append(q(c.getLocation()))
                        .append(",\"status\":").append(q(c.getStatus()))
                        .append("}");
            }
            sendJson(ex, 200, sb.append("]").toString());
            return;
        }
        if (!method(ex, "POST")) return;

        Map<String, String> f = readForm(ex);
        String proof = f.getOrDefault("proof", "").trim();
        String itemName = f.getOrDefault("itemName", "").trim();
        String location = f.getOrDefault("location", "").trim();
        Integer userId = parseInt(f.get("userId"));
        if (proof.isEmpty() || itemName.isEmpty() || userId == null) {
            error(ex, 400, "Proof, item name and a valid user ID are required.");
            return;
        }
        if (dao.addClaim(new Claim(proof, userId, itemName, location, "Pending"))) {
            sendJson(ex, 200, "{\"ok\":true}");
        } else {
            error(ex, 400, "Could not save the claim. Check that your user ID exists.");
        }
    }

    // POST /api/claims/update (claimId, status = Approved|Rejected) + header X-Admin-Password
    private static void updateClaim(HttpExchange ex) throws IOException {
        if (!method(ex, "POST")) return;
        if (!isAdmin(ex)) {
            error(ex, 401, "Unauthorized access");
            return;
        }
        Map<String, String> f = readForm(ex);
        Integer claimId = parseInt(f.get("claimId"));
        String status = f.getOrDefault("status", "");
        if (claimId == null || !(status.equals("Approved") || status.equals("Rejected"))) {
            error(ex, 400, "Valid claimId and status (Approved/Rejected) required.");
            return;
        }
        if (dao.updateClaim(claimId, status)) sendJson(ex, 200, "{\"ok\":true}");
        else error(ex, 404, "No claim found with that ID.");
    }

    // ---------- serve the website ----------

    // Serves web/index.html for "/" . (Only this one file is served, so nobody can read other files.)
    private static void serveHomePage(HttpExchange ex) throws IOException {
        String path = ex.getRequestURI().getPath();
        if (!path.equals("/") && !path.equals("/index.html")) {
            error(ex, 404, "Not found");
            return;
        }
        Path file = Paths.get("web", "index.html");
        if (!Files.exists(file)) {
            error(ex, 500, "web/index.html not found. Run from the project root folder: "
                    + Paths.get("").toAbsolutePath());
            return;
        }
        byte[] data = Files.readAllBytes(file);
        ex.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
        ex.sendResponseHeaders(200, data.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(data);
        }
    }

    // ---------- small helpers ----------

    private interface Route {
        void run(HttpExchange ex) throws IOException;
    }

    // Wraps every route so an unexpected exception returns a JSON error instead of a dead connection.
    private static void handle(HttpExchange ex, Route route) throws IOException {
        try {
            route.run(ex);
        } catch (Exception e) {
            e.printStackTrace();
            error(ex, 500, "Server error: " + e.getMessage());
        }
    }

    private static boolean method(HttpExchange ex, String expected) throws IOException {
        if (ex.getRequestMethod().equals(expected)) return true;
        error(ex, 405, "Use " + expected);
        return false;
    }

    private static boolean isAdmin(HttpExchange ex) {
        String given = ex.getRequestHeaders().getFirst("X-Admin-Password");
        return ADMIN_PASSWORD.equals(given);
    }

    // Reads "a=1&b=hello%20world" request bodies into a map.
    private static Map<String, String> readForm(HttpExchange ex) throws IOException {
        Map<String, String> map = new HashMap<>();
        try (InputStream in = ex.getRequestBody()) {
            String body = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            for (String pair : body.split("&")) {
                if (pair.isEmpty()) continue;
                int eq = pair.indexOf('=');
                String k = eq < 0 ? pair : pair.substring(0, eq);
                String v = eq < 0 ? "" : pair.substring(eq + 1);
                map.put(URLDecoder.decode(k, StandardCharsets.UTF_8), URLDecoder.decode(v, StandardCharsets.UTF_8));
            }
        }
        return map;
    }

    private static String queryParam(HttpExchange ex, String name) {
        String query = ex.getRequestURI().getRawQuery();
        if (query == null) return null;
        for (String pair : query.split("&")) {
            int eq = pair.indexOf('=');
            if (eq > 0 && URLDecoder.decode(pair.substring(0, eq), StandardCharsets.UTF_8).equals(name)) {
                return URLDecoder.decode(pair.substring(eq + 1), StandardCharsets.UTF_8);
            }
        }
        return null;
    }

    private static Integer parseInt(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return null;
        }
    }

    // Turns a Java String into a safe JSON string literal (with quotes).
    private static String q(String s) {
        if (s == null) return "\"\"";
        StringBuilder sb = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < 0x20) sb.append(String.format("\\u%04x", (int) c));
                    else sb.append(c);
            }
        }
        return sb.append("\"").toString();
    }

    private static void error(HttpExchange ex, int status, String message) throws IOException {
        sendJson(ex, status, "{\"error\":" + q(message) + "}");
    }

    private static void sendJson(HttpExchange ex, int status, String json) throws IOException {
        byte[] data = json.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        ex.sendResponseHeaders(status, data.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(data);
        }
    }
}
