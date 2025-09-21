/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author MinHeee
 */
public class ServiceEvaluation {

    private int evaluationId;
    private CarMaintenance maintenance;
    private User customer;
    private int serviceRating;
    private int qualityRating;
    private int timelinessRating;
    private int overallRating;
    private String comments;
    private String createdDate;

    public ServiceEvaluation() {
    }

    public ServiceEvaluation(int evaluationId, CarMaintenance maintenance, User customer, int serviceRating, int qualityRating, int timelinessRating, int overallRating, String comments, String createdDate) {
        this.evaluationId = evaluationId;
        this.maintenance = maintenance;
        this.customer = customer;
        this.serviceRating = serviceRating;
        this.qualityRating = qualityRating;
        this.timelinessRating = timelinessRating;
        this.overallRating = overallRating;
        this.comments = comments;
        this.createdDate = createdDate;
    }

    public int getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(int evaluationId) {
        this.evaluationId = evaluationId;
    }

    public CarMaintenance getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(CarMaintenance maintenance) {
        this.maintenance = maintenance;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public int getServiceRating() {
        return serviceRating;
    }

    public void setServiceRating(int serviceRating) {
        this.serviceRating = serviceRating;
    }

    public int getQualityRating() {
        return qualityRating;
    }

    public void setQualityRating(int qualityRating) {
        this.qualityRating = qualityRating;
    }

    public int getTimelinessRating() {
        return timelinessRating;
    }

    public void setTimelinessRating(int timelinessRating) {
        this.timelinessRating = timelinessRating;
    }

    public int getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(int overallRating) {
        this.overallRating = overallRating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "ServiceEvaluation{" + "evaluationId=" + evaluationId + ", maintenance=" + maintenance + ", customer=" + customer + ", serviceRating=" + serviceRating + ", qualityRating=" + qualityRating + ", timelinessRating=" + timelinessRating + ", overallRating=" + overallRating + ", comments=" + comments + ", createdDate=" + createdDate + '}';
    }

}
