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
public class InventoryLot {
    private TransferOrder transferOrder;
    private Product product;
    private int quantityDoc;
    private Integer quantityAct;
    private BigDecimal price;
    private int status;
    private String lotNumber;
    private LocalDate expiryDate;

    public InventoryLot() {
    }

    public InventoryLot(TransferOrder transferOrder, Product product, int quantityDoc, Integer quantityAct, BigDecimal price, int status, String lotNumber, LocalDate expiryDate) {
        this.transferOrder = transferOrder;
        this.product = product;
        this.quantityDoc = quantityDoc;
        this.quantityAct = quantityAct;
        this.price = price;
        this.status = status;
        this.lotNumber = lotNumber;
        this.expiryDate = expiryDate;
    }

    public TransferOrder getTransferOrder() {
        return transferOrder;
    }

    public void setTransferOrder(TransferOrder transferOrder) {
        this.transferOrder = transferOrder;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantityDoc() {
        return quantityDoc;
    }

    public void setQuantityDoc(int quantityDoc) {
        this.quantityDoc = quantityDoc;
    }

    public Integer getQuantityAct() {
        return quantityAct;
    }

    public void setQuantityAct(Integer quantityAct) {
        this.quantityAct = quantityAct;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
    
}
