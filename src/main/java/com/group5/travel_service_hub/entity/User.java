package com.group5.travel_service_hub.entity;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    // Common fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;  // Store hashed passwords

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // field for account status
    @Column(nullable = false)
    private boolean active = true; // Default to active

    @Column(nullable = false)
    private boolean banned = false;

    public void setAccountStatus(String status) {
    }

    //sysadmin
    public enum Role{
        ACTIVE,
        SUSPENDED,
        BANNED
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus accountStatus;

    private String profilePic; // URL or path to the profile picture

    @OneToMany(mappedBy = "providerDetails", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Package> packages;






    // Constructors

    public User() {
    }

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

    public User(String username, String password, String email, Role role, boolean active, boolean banned, String profilePic) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.active = active;
        this.banned = banned;
        this.profilePic = profilePic;
    }

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
    // ... (omitted for brevity)

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
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





    public Long getId() {
        return id;
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

}