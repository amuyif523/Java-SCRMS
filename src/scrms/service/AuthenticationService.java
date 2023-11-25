package scrms.service;

import scrms.data.DataStore;
import scrms.exceptions.AuthenticationException;
import scrms.exceptions.ResourceNotFoundException;
import scrms.model.Admin;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles admin authentication and registration.
 */
public class AuthenticationService {

    private final DataStore<Admin> dataStore;
    private final List<Admin> admins;

    public AuthenticationService() {
        this.dataStore = new DataStore<>("admins.json", Admin::fromJSON, Admin::toJSON);
        this.admins = new ArrayList<>(dataStore.load());
        if (admins.isEmpty()) {
            Admin defaultAdmin = Admin.create("admin", "admin123", "Default Administrator");
            admins.add(defaultAdmin);
            persist();
        }
    }

    public Admin login(String username, String password) {
        return admins.stream()
                .filter(admin -> admin.getUsername().equals(username))
                .filter(admin -> admin.verifyPassword(password))
                .findFirst()
                .orElseThrow(() -> new AuthenticationException("Invalid username or password"));
    }

    public Admin register(String username, String password, String fullName) {
        boolean exists = admins.stream().anyMatch(admin -> admin.getUsername().equals(username));
        if (exists) {
            throw new AuthenticationException("Username already exists");
        }
        Admin admin = Admin.create(username, password, fullName);
        admins.add(admin);
        persist();
        return admin;
    }

    public Admin findById(String adminId) {
        return admins.stream()
                .filter(admin -> admin.getAdminId().equals(adminId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found: " + adminId));
    }

    public List<Admin> findAll() {
        return new ArrayList<>(admins);
    }

    public void reload() {
        admins.clear();
        admins.addAll(dataStore.load());
    }

    public void flush() {
        persist();
    }

    private void persist() {
        dataStore.save(admins);
    }
}
