package dal;

import context.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import model.Appointment;

public class AppointmentDAO extends DBContext {

    public boolean insertAppointment(Appointment a) {
        String sql = "INSERT INTO Appointment "
                   + "(CarId, AppointmentDate, RequestedServices, Status, Notes, CreatedBy, CreatedDate, RequestedPackageID) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, a.getCar().getCarId());
            ps.setString(2, a.getAppointmentDate());
            ps.setString(3, a.getRequestedServices());
            ps.setString(4, a.getStatus());
            ps.setString(5, a.getNotes());
            ps.setInt(6, a.getCreatedBy().getUserId());
            ps.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            ps.setInt(8, a.getRequestedPackage().getPackageId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
