/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author MinHeee
 */
public class Permission {

    private int permissionID;
    private String name;
    private String url;
    private String description;
    private PermissionGroup group;

    public Permission() {
    }

    public Permission(int permissionID, String name, String url, String description, PermissionGroup group) {
        this.permissionID = permissionID;
        this.name = name;
        this.url = url;
        this.description = description;
        this.group = group;
    }

    public int getPermissionID() {
        return permissionID;
    }

    public void setPermissionID(int permissionID) {
        this.permissionID = permissionID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PermissionGroup getGroup() {
        return group;
    }

    public void setGroup(PermissionGroup group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "Permission{" + "permissionID=" + permissionID + ", name=" + name + ", url=" + url + ", description=" + description + ", group=" + group + '}';
    }

   

}
