/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author MinHeee
 */
public class Product {
    private int productId;
    private String code;
    private String name;
    private String type; // PART / SERVICE
    private BigDecimal price;
    private String description;
    private String image;
    private Unit unit;
    private Category category;
    private int warrantyPeriodMonths;
    private int minStockLevel;
    private BigDecimal estimatedDurationHours;
    private boolean isActive;
    private LocalDateTime createdDate;

    public Product() {
    }

    public Product(int productId, String code, String name, String type, BigDecimal price, String description, String image, Unit unit, Category category, int warrantyPeriodMonths, int minStockLevel, BigDecimal estimatedDurationHours, boolean isActive, LocalDateTime createdDate) {
        this.productId = productId;
        this.code = code;
        this.name = name;
        this.type = type;
        this.price = price;
        this.description = description;
        this.image = image;
        this.unit = unit;
        this.category = category;
        this.warrantyPeriodMonths = warrantyPeriodMonths;
        this.minStockLevel = minStockLevel;
        this.estimatedDurationHours = estimatedDurationHours;
        this.isActive = isActive;
        this.createdDate = createdDate;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getWarrantyPeriodMonths() {
        return warrantyPeriodMonths;
    }

    public void setWarrantyPeriodMonths(int warrantyPeriodMonths) {
        this.warrantyPeriodMonths = warrantyPeriodMonths;
    }

    public int getMinStockLevel() {
        return minStockLevel;
    }

    public void setMinStockLevel(int minStockLevel) {
        this.minStockLevel = minStockLevel;
    }

    public BigDecimal getEstimatedDurationHours() {
        return estimatedDurationHours;
    }

    public void setEstimatedDurationHours(BigDecimal estimatedDurationHours) {
        this.estimatedDurationHours = estimatedDurationHours;
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

    @Override
    public String toString() {
        return "Product{" + "productId=" + productId + ", code=" + code + ", name=" + name + ", type=" + type + ", price=" + price + ", description=" + description + ", image=" + image + ", unit=" + unit + ", category=" + category + ", warrantyPeriodMonths=" + warrantyPeriodMonths + ", minStockLevel=" + minStockLevel + ", estimatedDurationHours=" + estimatedDurationHours + ", isActive=" + isActive + ", createdDate=" + createdDate + '}';
    }
    
    
}
