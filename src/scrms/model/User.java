package scrms.model;

import scrms.utils.JsonUtils;
import scrms.utils.PasswordUtils;

/**
 * Abstract representation for authenticated users inside SCRMS.
 */
public abstract class User {

    private final String username;
    private String passwordHash;

    /**
     * Creates a user with the provided credentials.
     *
     * @param username     login name
     * @param passwordHash hashed password
     */
    protected User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Checks whether the provided password matches the stored hash.
     *
     * @param password raw password text
     * @return true if the password matches
     */
    public boolean verifyPassword(String password) {
        return PasswordUtils.matches(password, passwordHash);
    }

    /**
     * Serializes the user into JSON.
     *
     * @return JSON string
     */
    public abstract String toJSON();

    /**
     * Helper for subclasses that provides the shared JSON fragment.
     *
     * @return JSON fragment with username and password hash
     */
    protected String baseJsonFields() {
        return "\"username\":" + JsonUtils.quote(username)
                + ",\"passwordHash\":" + JsonUtils.quote(passwordHash);
    }
}
