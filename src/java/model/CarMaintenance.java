/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author MinHeee
 */
public class CarMaintenance {
    private int maintenanceId;
    private Car car;
    private Appointment appointment;
    private LocalDateTime maintenanceDate;
    private int odometer;
    private String status;
    private BigDecimal totalAmount;
    private String notes;
    private User createdBy;
    private User assignedTechnician;
    private LocalDateTime createdDate;
    private LocalDateTime completedDate;

    public CarMaintenance() {
    }

    public CarMaintenance(int maintenanceId, Car car, Appointment appointment, LocalDateTime maintenanceDate, int odometer, String status, BigDecimal totalAmount, String notes, User createdBy, User assignedTechnician, LocalDateTime createdDate, LocalDateTime completedDate) {
        this.maintenanceId = maintenanceId;
        this.car = car;
        this.appointment = appointment;
        this.maintenanceDate = maintenanceDate;
        this.odometer = odometer;
        this.status = status;
        this.totalAmount = totalAmount;
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

    public LocalDateTime getMaintenanceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceDate(LocalDateTime maintenanceDate) {
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(LocalDateTime completedDate) {
        this.completedDate = completedDate;
    }
    
    
}
