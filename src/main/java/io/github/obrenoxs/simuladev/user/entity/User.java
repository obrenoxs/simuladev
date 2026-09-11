package io.github.obrenoxs.simuladev.user.entity;

import io.github.obrenoxs.simuladev.user.enums.UserRole;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @Column(length = 254)
    private String email;
    private String stack;
    private String currentLevel;
    private Integer levelPercentage;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(length = 60)
    private String password;

    public User() {
    }

    public User(UUID id, String name, String email, String stack, String currentLevel, Integer levelPercentage, UserRole role, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.stack = stack;
        this.currentLevel = currentLevel;
        this.levelPercentage = levelPercentage;
        this.role = role;
        this.password = password;
    }

    public User(String name, String email, String stack, String currentLevel, Integer levelPercentage, UserRole role, String password) {
        this.name = name;
        this.email = email;
        this.stack = stack;
        this.currentLevel = currentLevel;
        this.levelPercentage = levelPercentage;
        this.role = role;
        this.password = password;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public String getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(String currentLevel) {
        this.currentLevel = currentLevel;
    }

    public Integer getLevelPercentage() {
        return levelPercentage;
    }

    public void setLevelPercentage(Integer levelPercentage) {
        this.levelPercentage = levelPercentage;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
