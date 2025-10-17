/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author MinHeee
 */
public class Car {

    private int carId;
    private String licensePlate;
    private String brand;
    private String model;
    private int year;
    private String color;
    private String engineNumber;
    private String chassisNumber;
    private User owner;
    private String purchaseDate;
    private String lastMaintenanceDate;
    private String nextMaintenanceDate;
    private String createdDate;
    private Integer currentOdometer;

    public Car() {
    }

    public Car(int carId, String licensePlate, String brand, String model, int year, String color, String engineNumber, String chassisNumber, User owner, String purchaseDate, String lastMaintenanceDate, String nextMaintenanceDate, String createdDate, Integer currentOdometer) {
        this.carId = carId;
        this.licensePlate = licensePlate;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.color = color;
        this.engineNumber = engineNumber;
        this.chassisNumber = chassisNumber;
        this.owner = owner;
        this.purchaseDate = purchaseDate;
        this.lastMaintenanceDate = lastMaintenanceDate;
        this.nextMaintenanceDate = nextMaintenanceDate;
        this.createdDate = createdDate;
        this.currentOdometer = currentOdometer;
    }
    

    
    
    

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEngineNumber() {
        return engineNumber;
    }

    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }

    public String getChassisNumber() {
        return chassisNumber;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public void setLastMaintenanceDate(String lastMaintenanceDate) {
        this.lastMaintenanceDate = lastMaintenanceDate;
    }

    public String getNextMaintenanceDate() {
        return nextMaintenanceDate;
    }

    public void setNextMaintenanceDate(String nextMaintenanceDate) {
        this.nextMaintenanceDate = nextMaintenanceDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getCurrentOdometer() {
        return currentOdometer;
    }

    public void setCurrentOdometer(Integer currentOdometer) {
        this.currentOdometer = currentOdometer;
    }

    @Override
    public String toString() {
        return "Car{" + "carId=" + carId + ", licensePlate=" + licensePlate + ", brand=" + brand + ", model=" + model + ", year=" + year + ", color=" + color + ", engineNumber=" + engineNumber + ", chassisNumber=" + chassisNumber + ", owner=" + owner + ", purchaseDate=" + purchaseDate + ", lastMaintenanceDate=" + lastMaintenanceDate + ", nextMaintenanceDate=" + nextMaintenanceDate + ", createdDate=" + createdDate + ", currentOdometer=" + currentOdometer + '}';
    }
    

   
}
