/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author MinHeee
 */
public class TransferOrder {
     private int transferOrderId;
    private int type; 
    private int status;
    private String note;
    private LocalDate date;
    private User approvedBy;
    private User createdBy;
    private LocalDateTime createdDate;
    private CarMaintenance relatedMaintenance;

    public TransferOrder() {
    }

    public TransferOrder(int transferOrderId, int type, int status, String note, LocalDate date, User approvedBy, User createdBy, LocalDateTime createdDate, CarMaintenance relatedMaintenance) {
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
    
}
