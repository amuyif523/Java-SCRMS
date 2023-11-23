package scrms.model;

import scrms.utils.IdGenerator;
import scrms.utils.JsonUtils;
import scrms.utils.PasswordUtils;

import java.util.Map;

/**
 * Administrative user capable of performing privileged tasks.
 */
public class Admin extends User {

    private final String adminId;
    private String fullName;

    public Admin(String adminId, String username, String passwordHash, String fullName) {
        super(username, passwordHash);
        this.adminId = adminId;
        this.fullName = fullName;
    }

    public static Admin create(String username, String rawPassword, String fullName) {
        return new Admin(IdGenerator.newId("ADM"), username, PasswordUtils.hash(rawPassword), fullName);
    }

    public String getAdminId() {
        return adminId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toJSON() {
        return "{" + "\"adminId\":" + JsonUtils.quote(adminId) + ","
                + baseJsonFields() + ","
                + "\"fullName\":" + JsonUtils.quote(fullName) + "}";
    }

    /**
     * Recreates an administrator from JSON text.
     *
     * @param json json object
     * @return admin instance
     */
    public static Admin fromJSON(String json) {
        Map<String, String> values = JsonUtils.parseJsonObject(json);
        String adminId = JsonUtils.unquote(values.get("adminId"));
        String username = JsonUtils.unquote(values.get("username"));
        String passwordHash = JsonUtils.unquote(values.get("passwordHash"));
        String fullName = JsonUtils.unquote(values.get("fullName"));
        return new Admin(adminId, username, passwordHash, fullName);
    }

    @Override
    public String toString() {
        return "Admin{" +
                "adminId='" + adminId + '\'' +
                ", username='" + getUsername() + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
