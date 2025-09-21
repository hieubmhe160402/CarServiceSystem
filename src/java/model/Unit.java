/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author MinHeee
 */
public class Unit {

    private int unitId;
    private String name;
    private String type;
    private String description;

    public Unit() {
    }

    public Unit(int unitId, String name, String type, String description) {
        this.unitId = unitId;
        this.name = name;
        this.type = type;
        this.description = description;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Unit{" + "unitId=" + unitId + ", name=" + name + ", type=" + type + ", description=" + description + '}';
    }

}
