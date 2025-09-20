/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDateTime;

/**
 *
 * @author MinHeee
 */
public class Appointment {
    private int appointmentId;
    private Car car;
    private LocalDateTime appointmentDate;
    private String requestedServices;
    private String status;
    private String notes;
    private User createdBy;
    private LocalDateTime createdDate;
    private User confirmedBy;
    private LocalDateTime confirmedDate;

    public Appointment() {
    }

    public Appointment(int appointmentId, Car car, LocalDateTime appointmentDate, String requestedServices, String status, String notes, User createdBy, LocalDateTime createdDate, User confirmedBy, LocalDateTime confirmedDate) {
        this.appointmentId = appointmentId;
        this.car = car;
        this.appointmentDate = appointmentDate;
        this.requestedServices = requestedServices;
        this.status = status;
        this.notes = notes;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.confirmedBy = confirmedBy;
        this.confirmedDate = confirmedDate;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getRequestedServices() {
        return requestedServices;
    }

    public void setRequestedServices(String requestedServices) {
        this.requestedServices = requestedServices;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public User getConfirmedBy() {
        return confirmedBy;
    }

    public void setConfirmedBy(User confirmedBy) {
        this.confirmedBy = confirmedBy;
    }

    public LocalDateTime getConfirmedDate() {
        return confirmedDate;
    }

    public void setConfirmedDate(LocalDateTime confirmedDate) {
        this.confirmedDate = confirmedDate;
    }

    @Override
    public String toString() {
        return "Appointment{" + "appointmentId=" + appointmentId + ", car=" + car + ", appointmentDate=" + appointmentDate + ", requestedServices=" + requestedServices + ", status=" + status + ", notes=" + notes + ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", confirmedBy=" + confirmedBy + ", confirmedDate=" + confirmedDate + '}';
    }
    
}
