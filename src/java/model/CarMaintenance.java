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
public class CarMaintenance {

    private int maintenanceId;
    private Car car;
    private Appointment appointment;
    private String maintenanceDate;
    private int odometer;
    private String status;
    private BigDecimal totalAmount;
     private BigDecimal discountAmount;   // 🔹 Cột mới
    private BigDecimal finalAmount;
    private String notes;
    private User createdBy;
    private User assignedTechnician;
    private String createdDate;
    private String completedDate;

    public CarMaintenance() {
    }

    public CarMaintenance(int maintenanceId, Car car, Appointment appointment, String maintenanceDate, int odometer, String status, BigDecimal totalAmount, BigDecimal discountAmount, BigDecimal finalAmount, String notes, User createdBy, User assignedTechnician, String createdDate, String completedDate) {
        this.maintenanceId = maintenanceId;
        this.car = car;
        this.appointment = appointment;
        this.maintenanceDate = maintenanceDate;
        this.odometer = odometer;
        this.status = status;
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.finalAmount = finalAmount;
        this.notes = notes;
        this.createdBy = createdBy;
        this.assignedTechnician = assignedTechnician;
        this.createdDate = createdDate;
        this.completedDate = completedDate;
    }

    
    public int getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(int maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public String getMaintenanceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceDate(String maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }

    public int getOdometer() {
        return odometer;
    }

    public void setOdometer(int odometer) {
        this.odometer = odometer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getAssignedTechnician() {
        return assignedTechnician;
    }

    public void setAssignedTechnician(User assignedTechnician) {
        this.assignedTechnician = assignedTechnician;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }

    @Override
    public String toString() {
        return "CarMaintenance{" + "maintenanceId=" + maintenanceId + ", car=" + car + ", appointment=" + appointment + ", maintenanceDate=" + maintenanceDate + ", odometer=" + odometer + ", status=" + status + ", totalAmount=" + totalAmount + ", discountAmount=" + discountAmount + ", finalAmount=" + finalAmount + ", notes=" + notes + ", createdBy=" + createdBy + ", assignedTechnician=" + assignedTechnician + ", createdDate=" + createdDate + ", completedDate=" + completedDate + '}';
    }
 
   
}
