/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 *
 * @author MinHeee
 */
public class User {
    private int userId;
    private String userCode;
    private String fullName;
    private String userName;
    private String password;
    private String email;
    private String phone;
    private String image;
    private Boolean male;
    private LocalDate dateOfBirth;
    private Role role;
    private boolean isActive;
    private LocalDateTime createdDate;
    private LocalDateTime lastLoginDate;

    public User() {
    }

    public User(int userId, String userCode, String fullName, String userName, String password, String email, String phone, String image, Boolean male, LocalDate dateOfBirth, Role role, boolean isActive, LocalDateTime createdDate, LocalDateTime lastLoginDate) {
        this.userId = userId;
        this.userCode = userCode;
        this.fullName = fullName;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.image = image;
        this.male = male;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
        this.isActive = isActive;
        this.createdDate = createdDate;
        this.lastLoginDate = lastLoginDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getMale() {
        return male;
    }

    public void setMale(Boolean male) {
        this.male = male;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(LocalDateTime lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    @Override
    public String toString() {
        return "User{" + "userId=" + userId + ", userCode=" + userCode + ", fullName=" + fullName + ", userName=" + userName + ", password=" + password + ", email=" + email + ", phone=" + phone + ", image=" + image + ", male=" + male + ", dateOfBirth=" + dateOfBirth + ", role=" + role + ", isActive=" + isActive + ", createdDate=" + createdDate + ", lastLoginDate=" + lastLoginDate + '}';
    }

    
    
}
