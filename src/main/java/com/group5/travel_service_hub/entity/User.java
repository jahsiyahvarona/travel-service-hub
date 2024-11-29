package com.group5.travel_service_hub.entity;

import jakarta.persistence.*;
import java.util.List;

/**
 * Entity class representing a User in the Travel Service Hub system.
 * It holds common user details, such as username, password, email, role, and account status.
 */
@Entity
@Table(name = "users")
public class User {

    // Primary key for the users table, auto-generated
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Unique username for each user
    @Column(nullable = false, unique = true)
    private String username;

    // Encrypted password for user authentication
    @Column(nullable = false)
    private String password;

    // Unique email address for the user
    @Column(nullable = false, unique = true)
    private String email;

    // Role of the user (e.g., PROVIDER, CUSTOMER, SYSADMIN)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Indicates whether the user account is active; default is true
    @Column(nullable = false)
    private boolean active = true;

    // Indicates whether the user account is banned; default is false
    @Column(nullable = false)
    private boolean banned = false;

    // Path or URL to the user's profile picture
    private String profilePic;

    // List of packages associated with the user if they are a provider
    @OneToMany(mappedBy = "providerDetails", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Package> packages;

    // Notifications sent by the user
    @OneToMany(mappedBy = "notifier")
    private List<Notification> sentNotifications;

    // Notifications received by the user
    @OneToMany(mappedBy = "notifee")
    private List<Notification> receivedNotifications;

    // Constructors

    /**
     * Default constructor required by JPA.
     */
    public User() {}

    /**
     * Full constructor including all fields.
     *
     * @param id         The user's ID.
     * @param username   The user's username.
     * @param password   The user's encrypted password.
     * @param email      The user's email address.
     * @param role       The user's role.
     * @param active     Whether the account is active.
     * @param banned     Whether the account is banned.
     * @param profilePic Path or URL to the user's profile picture.
     */
    public User(Long id, String username, String password, String email, Role role, boolean active, boolean banned, String profilePic) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.active = active;
        this.banned = banned;
        this.profilePic = profilePic;
    }

    /**
     * Constructor excluding ID, useful for new user creation.
     *
     * @param username   The user's username.
     * @param password   The user's encrypted password.
     * @param email      The user's email address.
     * @param role       The user's role.
     * @param active     Whether the account is active.
     * @param banned     Whether the account is banned.
     * @param profilePic Path or URL to the user's profile picture.
     */
    public User(String username, String password, String email, Role role, boolean active, boolean banned, String profilePic) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.active = active;
        this.banned = banned;
        this.profilePic = profilePic;
    }

    /**
     * Alternative constructor with reordered parameters.
     *
     * @param username   The user's username.
     * @param password   The user's encrypted password.
     * @param role       The user's role.
     * @param active     Whether the account is active.
     * @param banned     Whether the account is banned.
     * @param email      The user's email address.
     * @param profilePic Path or URL to the user's profile picture.
     */
    public User(String username, String password, Role role, boolean active, boolean banned, String email, String profilePic) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.active = active;
        this.banned = banned;
        this.email = email;
        this.profilePic = profilePic;
    }

    // Getters and Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public List<Package> getPackages() {
        return packages;
    }

    public void setPackages(List<Package> packages) {
        this.packages = packages;
    }

    public List<Notification> getSentNotifications() {
        return sentNotifications;
    }

    public void setSentNotifications(List<Notification> sentNotifications) {
        this.sentNotifications = sentNotifications;
    }

    public List<Notification> getReceivedNotifications() {
        return receivedNotifications;
    }

    public void setReceivedNotifications(List<Notification> receivedNotifications) {
        this.receivedNotifications = receivedNotifications;
    }
}
