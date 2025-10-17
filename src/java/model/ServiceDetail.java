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
public class ServiceDetail {

    private int serviceDetailId;
    private CarMaintenance maintenance;
    private Product product;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private String notes;
    private boolean fromPackage;              
    private MaintenancePackage maintenancePackage;

    public ServiceDetail() {
    }

    public ServiceDetail(int serviceDetailId, CarMaintenance maintenance, Product product, BigDecimal quantity, BigDecimal unitPrice, BigDecimal totalPrice, String notes, boolean fromPackage, MaintenancePackage maintenancePackage) {
        this.serviceDetailId = serviceDetailId;
        this.maintenance = maintenance;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.notes = notes;
        this.fromPackage = fromPackage;
        this.maintenancePackage = maintenancePackage;
    }

   

    public int getServiceDetailId() {
        return serviceDetailId;
    }

    public void setServiceDetailId(int serviceDetailId) {
        this.serviceDetailId = serviceDetailId;
    }

    public CarMaintenance getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(CarMaintenance maintenance) {
        this.maintenance = maintenance;
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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isFromPackage() {
        return fromPackage;
    }

    public void setFromPackage(boolean fromPackage) {
        this.fromPackage = fromPackage;
    }

    public MaintenancePackage getMaintenancePackage() {
        return maintenancePackage;
    }

    public void setMaintenancePackage(MaintenancePackage maintenancePackage) {
        this.maintenancePackage = maintenancePackage;
    }

    @Override
    public String toString() {
        return "ServiceDetail{" + "serviceDetailId=" + serviceDetailId + ", maintenance=" + maintenance + ", product=" + product + ", quantity=" + quantity + ", unitPrice=" + unitPrice + ", totalPrice=" + totalPrice + ", notes=" + notes + '}';
    }

}
