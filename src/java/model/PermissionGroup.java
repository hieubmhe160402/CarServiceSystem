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
public class PermissionGroup {

    private int groupID;
    private String groupName;
    private String description;
    private List<Permission> listPermissions;

    // Constructors
    public PermissionGroup() {
    }

    public PermissionGroup(int groupID, String groupName, String description, List<Permission> listPermissions) {
        this.groupID = groupID;
        this.groupName = groupName;
        this.description = description;
        this.listPermissions = listPermissions;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Permission> getListPermissions() {
        return listPermissions;
    }

    public void setListPermissions(List<Permission> listPermissions) {
        this.listPermissions = listPermissions;
    }

    @Override
    public String toString() {
        return "PermissionGroup{" + "groupID=" + groupID + ", groupName=" + groupName + ", description=" + description + ", listPermissions=" + listPermissions + '}';
    }

}
