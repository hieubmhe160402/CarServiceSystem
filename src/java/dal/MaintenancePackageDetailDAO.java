/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import context.DBContext;
import java.util.ArrayList;
import java.util.List;
import model.Product;
import java.sql.*;
import model.MaintenancePackage;
import model.MaintenancePackageDetail;
/**
 *
 * @author MinHeee
 */
public class MaintenancePackageDetailDAO  extends DBContext{
     public List<Product> getAllProductIdAndName() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT ProductID, Name FROM Product ORDER BY Name";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("ProductID"));
                p.setName(rs.getString("Name"));
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
     
     public List<MaintenancePackageDetail> getAllPackageDetails() {
        List<MaintenancePackageDetail> list = new ArrayList<>();
        String sql = """
            SELECT 
                mpd.PackageDetailID,
                mp.PackageCode,
                p.Name AS ProductName,
                mpd.Quantity,
                mpd.IsRequired,
                mpd.DisplayOrder,
                mpd.Notes
            FROM MaintenancePackageDetail AS mpd
            INNER JOIN MaintenancePackage AS mp ON mp.PackageID = mpd.PackageID
            INNER JOIN Product AS p ON p.ProductID = mpd.ProductID
            ORDER BY mp.PackageCode, mpd.DisplayOrder;
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MaintenancePackageDetail detail = new MaintenancePackageDetail();

                // Gán dữ liệu cho MaintenancePackage
                MaintenancePackage pkg = new MaintenancePackage();
                pkg.setPackageCode(rs.getString("PackageCode"));
                detail.setMaintenancePackage(pkg);

                // Gán dữ liệu cho Product
                Product product = new Product();
                product.setName(rs.getString("ProductName"));
                detail.setProduct(product);

                // Các field còn lại
                detail.setPackageDetailId(rs.getInt("PackageDetailID"));
                detail.setQuantity(rs.getBigDecimal("Quantity"));
                detail.setIsRequired(rs.getBoolean("IsRequired"));
                detail.setDisplayOrder(rs.getInt("DisplayOrder"));
                detail.setNotes(rs.getString("Notes"));

                list.add(detail);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

     
}
