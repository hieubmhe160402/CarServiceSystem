/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import context.DBContext;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.util.List;
import model.MaintenancePackageDetail;
import java.sql.SQLException;
import model.Product;
import model.User;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.MaintenancePackage;

/**
 *
 * @author nxtru
 */
public class MaintenancePackageDAO extends DBContext {

    public List<MaintenancePackageDetail> getAll() {
        List<MaintenancePackageDetail> list = new ArrayList<>();

        String sql = """
    SELECT 
        mp.PackageID,
        mp.PackageCode,
        mp.Name AS PackageName,
        mp.Description AS PackageDescription,
        mp.KilometerMilestone,
        mp.MonthMilestone,
        mp.BasePrice,
        mp.DiscountPercent, 
        mp.FinalPrice,
        mp.EstimatedDurationHours,
        mp.ApplicableBrands,
        mp.Image,
        mp.DisplayOrder,
        mp.IsActive,
        mp.CreatedDate,
        mp.CreatedBy,
        u.FullName AS CreatedByFullName,
        mpd.PackageDetailID,
        mpd.DisplayOrder AS DetailDisplayOrder,
        p.ProductID,
        p.Name AS ProductName
    FROM MaintenancePackageDetail mpd
    INNER JOIN MaintenancePackage mp ON mp.PackageID = mpd.PackageID
    INNER JOIN Product p ON p.ProductID = mpd.ProductID
    LEFT JOIN Users u ON mp.CreatedBy = u.UserID
    ORDER BY mp.PackageCode, mpd.DisplayOrder
""";

        try (PreparedStatement stm = connection.prepareStatement(sql); ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                // Gói bảo dưỡng
                MaintenancePackage mp = new MaintenancePackage();
                mp.setPackageId(rs.getInt("PackageID"));
                mp.setPackageCode(rs.getString("PackageCode"));
                mp.setName(rs.getString("PackageName"));
                mp.setDescription(rs.getString("PackageDescription"));
                mp.setKilometerMilestone(rs.getInt("KilometerMilestone"));
                mp.setMonthMilestone(rs.getInt("MonthMilestone"));
                mp.setBasePrice(rs.getBigDecimal("BasePrice"));
                mp.setDiscountPercent(rs.getBigDecimal("DiscountPercent"));
                mp.setFinalPrice(rs.getBigDecimal("FinalPrice"));
                mp.setEstimatedDurationHours(rs.getBigDecimal("EstimatedDurationHours"));
                mp.setApplicableBrands(rs.getString("ApplicableBrands"));
                mp.setImage(rs.getString("Image"));
                mp.setDisplayOrder(rs.getInt("DisplayOrder"));
                mp.setIsActive(rs.getBoolean("IsActive"));
                mp.setCreatedDate(rs.getString("CreatedDate"));

                // Sản phẩm
                Product p = new Product();
                p.setProductId(rs.getInt("ProductID"));
                p.setName(rs.getString("ProductName"));

                User createdBy = new User();
                createdBy.setUserId(rs.getInt("CreatedBy"));
                createdBy.setFullName(rs.getString("CreatedByFullName"));
                mp.setCreatedBy(createdBy);
                // Chi tiết gói
                MaintenancePackageDetail detail = new MaintenancePackageDetail();
                detail.setPackageDetailId(rs.getInt("PackageDetailID"));
                detail.setDisplayOrder(rs.getInt("DetailDisplayOrder"));
                detail.setMaintenancePackage(mp);
                detail.setProduct(p);

                list.add(detail);
            }

        } catch (SQLException ex) {
            Logger.getLogger(MaintenancePackageDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }
}
