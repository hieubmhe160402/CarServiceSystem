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
public class MaintenancePackageUsage {
     private int usageId;                         
    private CarMaintenance maintenance;           
    private MaintenancePackage maintenancePackage; 
    private BigDecimal appliedPrice;             
    private BigDecimal discountAmount;           
    private String appliedDate; 

    public MaintenancePackageUsage() {
    }

    public MaintenancePackageUsage(int usageId, CarMaintenance maintenance, MaintenancePackage maintenancePackage, BigDecimal appliedPrice, BigDecimal discountAmount, String appliedDate) {
        this.usageId = usageId;
        this.maintenance = maintenance;
        this.maintenancePackage = maintenancePackage;
        this.appliedPrice = appliedPrice;
        this.discountAmount = discountAmount;
        this.appliedDate = appliedDate;
    }

    public int getUsageId() {
        return usageId;
    }

    public void setUsageId(int usageId) {
        this.usageId = usageId;
    }

    public CarMaintenance getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(CarMaintenance maintenance) {
        this.maintenance = maintenance;
    }

    public MaintenancePackage getMaintenancePackage() {
        return maintenancePackage;
    }

    public void setMaintenancePackage(MaintenancePackage maintenancePackage) {
        this.maintenancePackage = maintenancePackage;
    }

    public BigDecimal getAppliedPrice() {
        return appliedPrice;
    }

    public void setAppliedPrice(BigDecimal appliedPrice) {
        this.appliedPrice = appliedPrice;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(String appliedDate) {
        this.appliedDate = appliedDate;
    }

    @Override
    public String toString() {
        return "MaintenancePackageUsage{" + "usageId=" + usageId + ", maintenance=" + maintenance + ", maintenancePackage=" + maintenancePackage + ", appliedPrice=" + appliedPrice + ", discountAmount=" + discountAmount + ", appliedDate=" + appliedDate + '}';
    }

    
    
    
}
