package com.group5.travel_service_hub.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "like_dislikes")
public class LikeDislike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean isLike; // true for like, false for dislike

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", nullable = false)
    private Package pkg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private User provider;

    // Constructors
    public LikeDislike() {}



    public LikeDislike(boolean like, Package pkg, User user, User provider) {
        isLike = like;
        this.pkg = pkg;
        this.user = user;
        this.provider = provider;
    }

    public LikeDislike(Long id, boolean like, Package pkg, User user, User provider) {
        this.id = id;
        isLike = like;
        this.pkg = pkg;
        this.user = user;
        this.provider = provider;
    }


    // Getters and Setters
    public User getProvider() {
        return provider;
    }

    public void setProvider(User provider) {
        this.provider = provider;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public Package getPkg() {
        return pkg;
    }

    public void setPkg(Package pkg) {
        this.pkg = pkg;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
