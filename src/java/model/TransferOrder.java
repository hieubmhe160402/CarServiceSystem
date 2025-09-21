/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author MinHeee
 */
public class TransferOrder {

    private int transferOrderId;
    private int type;
    private int status;
    private String note;
    private String date;
    private User approvedBy;
    private User createdBy;
    private String createdDate;
    private CarMaintenance relatedMaintenance;

    public TransferOrder() {
    }

    public TransferOrder(int transferOrderId, int type, int status, String note, String date, User approvedBy, User createdBy, String createdDate, CarMaintenance relatedMaintenance) {
        this.transferOrderId = transferOrderId;
        this.type = type;
        this.status = status;
        this.note = note;
        this.date = date;
        this.approvedBy = approvedBy;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.relatedMaintenance = relatedMaintenance;
    }

    public int getTransferOrderId() {
        return transferOrderId;
    }

    public void setTransferOrderId(int transferOrderId) {
        this.transferOrderId = transferOrderId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public CarMaintenance getRelatedMaintenance() {
        return relatedMaintenance;
    }

    public void setRelatedMaintenance(CarMaintenance relatedMaintenance) {
        this.relatedMaintenance = relatedMaintenance;
    }

    @Override
    public String toString() {
        return "TransferOrder{" + "transferOrderId=" + transferOrderId + ", type=" + type + ", status=" + status + ", note=" + note + ", date=" + date + ", approvedBy=" + approvedBy + ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", relatedMaintenance=" + relatedMaintenance + '}';
    }

}
