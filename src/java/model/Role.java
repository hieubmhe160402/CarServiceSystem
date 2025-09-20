/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;

/**
 *
 * @author MinHeee
 */
public class Role {

    private int roleID;
    private String roleName;
    private String description;
    private List<Permission> permissions;
 
    public Role() {
    }

    public Role(int roleID, String roleName, String description) {
        this.roleID = roleID;
        this.roleName = roleName;
        this.description = description;
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Role{" + "roleID=" + roleID + ", roleName=" + roleName + ", description=" + description + '}';
    }

}
