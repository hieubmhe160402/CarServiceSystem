/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;

/**
 *
 * @author MinHeee
 */
public class MaintenancePackageDetail {
    private int packageDetailId;                 
    private MaintenancePackage maintenancePackage; 
    private Product product;                     
    private BigDecimal quantity;                 
    private boolean isRequired;                 
    private Integer displayOrder;                
    private String notes; 

    public MaintenancePackageDetail() {
    }

    public MaintenancePackageDetail(int packageDetailId, MaintenancePackage maintenancePackage, Product product, BigDecimal quantity, boolean isRequired, Integer displayOrder, String notes) {
        this.packageDetailId = packageDetailId;
        this.maintenancePackage = maintenancePackage;
        this.product = product;
        this.quantity = quantity;
        this.isRequired = isRequired;
        this.displayOrder = displayOrder;
        this.notes = notes;
    }

    public int getPackageDetailId() {
        return packageDetailId;
    }

    public void setPackageDetailId(int packageDetailId) {
        this.packageDetailId = packageDetailId;
    }

    public MaintenancePackage getMaintenancePackage() {
        return maintenancePackage;
    }

    public void setMaintenancePackage(MaintenancePackage maintenancePackage) {
        this.maintenancePackage = maintenancePackage;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public boolean isIsRequired() {
        return isRequired;
    }

    public void setIsRequired(boolean isRequired) {
        this.isRequired = isRequired;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "MaintenancePackageDetail{" + "packageDetailId=" + packageDetailId + ", maintenancePackage=" + maintenancePackage + ", product=" + product + ", quantity=" + quantity + ", isRequired=" + isRequired + ", displayOrder=" + displayOrder + ", notes=" + notes + '}';
    }

    

}
