/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
/**
 *
 * @author MinHeee
 */
public class Appointment {
    private int appointmentId;
    private Car car;
    private String appointmentDate;
    private String requestedServices;
    private String status;
    private String notes;
    private User createdBy;
    private String createdDate;
    private User confirmedBy;
    private String confirmedDate;

    public Appointment() {
    }

    public Appointment(int appointmentId, Car car, String appointmentDate, String requestedServices, String status, String notes, User createdBy, String createdDate, User confirmedBy, String confirmedDate) {
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

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public User getConfirmedBy() {
        return confirmedBy;
    }

    public void setConfirmedBy(User confirmedBy) {
        this.confirmedBy = confirmedBy;
    }

    public String getConfirmedDate() {
        return confirmedDate;
    }

    public void setConfirmedDate(String confirmedDate) {
        this.confirmedDate = confirmedDate;
    }

    @Override
    public String toString() {
        return "Appointment{" + "appointmentId=" + appointmentId + ", car=" + car + ", appointmentDate=" + appointmentDate + ", requestedServices=" + requestedServices + ", status=" + status + ", notes=" + notes + ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", confirmedBy=" + confirmedBy + ", confirmedDate=" + confirmedDate + '}';
    }

   
    
}
