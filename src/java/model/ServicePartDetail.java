/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author MinHeee
 */
public class ServicePartDetail {
    private int servicePartDetailId;
    private CarMaintenance maintenance;
    private Product product;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private LocalDate installationDate;
    private LocalDate warrantyExpireDate;
    private String lotNumber;
    private String notes;

    public ServicePartDetail() {
    }

    public ServicePartDetail(int servicePartDetailId, CarMaintenance maintenance, Product product, int quantity, BigDecimal unitPrice, BigDecimal totalPrice, LocalDate installationDate, LocalDate warrantyExpireDate, String lotNumber, String notes) {
        this.servicePartDetailId = servicePartDetailId;
        this.maintenance = maintenance;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.installationDate = installationDate;
        this.warrantyExpireDate = warrantyExpireDate;
        this.lotNumber = lotNumber;
        this.notes = notes;
    }

    public int getServicePartDetailId() {
        return servicePartDetailId;
    }

    public void setServicePartDetailId(int servicePartDetailId) {
        this.servicePartDetailId = servicePartDetailId;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
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

    public LocalDate getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(LocalDate installationDate) {
        this.installationDate = installationDate;
    }

    public LocalDate getWarrantyExpireDate() {
        return warrantyExpireDate;
    }

    public void setWarrantyExpireDate(LocalDate warrantyExpireDate) {
        this.warrantyExpireDate = warrantyExpireDate;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "ServicePartDetail{" + "servicePartDetailId=" + servicePartDetailId + ", maintenance=" + maintenance + ", product=" + product + ", quantity=" + quantity + ", unitPrice=" + unitPrice + ", totalPrice=" + totalPrice + ", installationDate=" + installationDate + ", warrantyExpireDate=" + warrantyExpireDate + ", lotNumber=" + lotNumber + ", notes=" + notes + '}';
    }
    
}
