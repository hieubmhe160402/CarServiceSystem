/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDateTime;

/**
 *
 * @author MinHeee
 */
public class TokenForgetPassword {
    private int id;
    private String token;
    private LocalDateTime expiryTime;
    private boolean isUsed;
    private User user;

    public TokenForgetPassword() {
    }

    public TokenForgetPassword(int id, String token, LocalDateTime expiryTime, boolean isUsed, User user) {
        this.id = id;
        this.token = token;
        this.expiryTime = expiryTime;
        this.isUsed = isUsed;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

    public boolean isIsUsed() {
        return isUsed;
    }

    public void setIsUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "TokenForgetPassword{" + "id=" + id + ", token=" + token + ", expiryTime=" + expiryTime + ", isUsed=" + isUsed + ", user=" + user + '}';
    }
    
}
