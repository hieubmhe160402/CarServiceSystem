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
public class SupplierProduct {

    private Supplier supplier;
    private Product product;
    private int deliveryDuration;
    private BigDecimal estimatedPrice;
    private String policies;
    private boolean isActive;

    public SupplierProduct() {
    }

    public SupplierProduct(Supplier supplier, Product product, int deliveryDuration, BigDecimal estimatedPrice, String policies, boolean isActive) {
        this.supplier = supplier;
        this.product = product;
        this.deliveryDuration = deliveryDuration;
        this.estimatedPrice = estimatedPrice;
        this.policies = policies;
        this.isActive = isActive;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getDeliveryDuration() {
        return deliveryDuration;
    }

    public void setDeliveryDuration(int deliveryDuration) {
        this.deliveryDuration = deliveryDuration;
    }

    public BigDecimal getEstimatedPrice() {
        return estimatedPrice;
    }

    public void setEstimatedPrice(BigDecimal estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }

    public String getPolicies() {
        return policies;
    }

    public void setPolicies(String policies) {
        this.policies = policies;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "SupplierProduct{" + "supplier=" + supplier + ", product=" + product + ", deliveryDuration=" + deliveryDuration + ", estimatedPrice=" + estimatedPrice + ", policies=" + policies + ", isActive=" + isActive + '}';
    }
}
