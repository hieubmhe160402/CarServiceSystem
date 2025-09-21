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
public class PaymentTransaction {
    private int transactionId;
    private CarMaintenance maintenance;
    private String paymentMethod;
    private BigDecimal amount;
    private String paymentDate;
    private String status;
    private String transactionReference;
    private String notes;
    private User processedBy;

    public PaymentTransaction() {
    }

    public PaymentTransaction(int transactionId, CarMaintenance maintenance, String paymentMethod, BigDecimal amount, String paymentDate, String status, String transactionReference, String notes, User processedBy) {
        this.transactionId = transactionId;
        this.maintenance = maintenance;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.status = status;
        this.transactionReference = transactionReference;
        this.notes = notes;
        this.processedBy = processedBy;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public CarMaintenance getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(CarMaintenance maintenance) {
        this.maintenance = maintenance;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public User getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(User processedBy) {
        this.processedBy = processedBy;
    }

    @Override
    public String toString() {
        return "PaymentTransaction{" + "transactionId=" + transactionId + ", maintenance=" + maintenance + ", paymentMethod=" + paymentMethod + ", amount=" + amount + ", paymentDate=" + paymentDate + ", status=" + status + ", transactionReference=" + transactionReference + ", notes=" + notes + ", processedBy=" + processedBy + '}';
    }

  
}
