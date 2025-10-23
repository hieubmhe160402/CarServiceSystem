/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author MinHeee
 */
public class MaintenancePackage {
       private int packageId;                
    private String packageCode;            
    private String name;                   
    private String description;            
    private Integer kilometerMilestone;   
    private Integer monthMilestone;      
    private BigDecimal basePrice;          
    private BigDecimal discountPercent;    
    private BigDecimal finalPrice;        
    private BigDecimal estimatedDurationHours; 
    private String applicableBrands;       
    private String image;                  
    private Integer displayOrder;          
    private boolean isActive;              
    private String createdDate;          
    private User createdBy;
    private List<MaintenancePackageDetail> packageDetails = new ArrayList<>();

    public MaintenancePackage() {
    }

    public MaintenancePackage(int packageId, String packageCode, String name, String description, Integer kilometerMilestone, Integer monthMilestone, BigDecimal basePrice, BigDecimal discountPercent, BigDecimal finalPrice, BigDecimal estimatedDurationHours, String applicableBrands, String image, Integer displayOrder, boolean isActive, String createdDate, User createdBy) {
        this.packageId = packageId;
        this.packageCode = packageCode;
        this.name = name;
        this.description = description;
        this.kilometerMilestone = kilometerMilestone;
        this.monthMilestone = monthMilestone;
        this.basePrice = basePrice;
        this.discountPercent = discountPercent;
        this.finalPrice = finalPrice;
        this.estimatedDurationHours = estimatedDurationHours;
        this.applicableBrands = applicableBrands;
        this.image = image;
        this.displayOrder = displayOrder;
        this.isActive = isActive;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public String getPackageCode() {
        return packageCode;
    }

    public void setPackageCode(String packageCode) {
        this.packageCode = packageCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getKilometerMilestone() {
        return kilometerMilestone;
    }

    public void setKilometerMilestone(Integer kilometerMilestone) {
        this.kilometerMilestone = kilometerMilestone;
    }

    public Integer getMonthMilestone() {
        return monthMilestone;
    }

    public void setMonthMilestone(Integer monthMilestone) {
        this.monthMilestone = monthMilestone;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    public BigDecimal getEstimatedDurationHours() {
        return estimatedDurationHours;
    }

    public void setEstimatedDurationHours(BigDecimal estimatedDurationHours) {
        this.estimatedDurationHours = estimatedDurationHours;
    }

    public String getApplicableBrands() {
        return applicableBrands;
    }

    public void setApplicableBrands(String applicableBrands) {
        this.applicableBrands = applicableBrands;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public List<MaintenancePackageDetail> getPackageDetails() {
        return packageDetails;
    }

    public void setPackageDetails(List<MaintenancePackageDetail> packageDetails) {
        this.packageDetails = packageDetails;
    }

    @Override
    public String toString() {
        return "MaintenancePackage{" + "packageId=" + packageId + ", packageCode=" + packageCode + ", name=" + name + ", description=" + description + ", kilometerMilestone=" + kilometerMilestone + ", monthMilestone=" + monthMilestone + ", basePrice=" + basePrice + ", discountPercent=" + discountPercent + ", finalPrice=" + finalPrice + ", estimatedDurationHours=" + estimatedDurationHours + ", applicableBrands=" + applicableBrands + ", image=" + image + ", displayOrder=" + displayOrder + ", isActive=" + isActive + ", createdDate=" + createdDate + ", createdBy=" + createdBy + '}';
    }

   
    
    
}
