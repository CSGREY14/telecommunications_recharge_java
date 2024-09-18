import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/FileGrievanceServlet")
public class FileGrievanceServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int accId = Integer.parseInt(request.getParameter("accId"));
        String details = request.getParameter("details");
        String status = "initiated";
        String filedDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        try (Connection conn = JDBCUtil.getConnection()) {
            String sql = "INSERT INTO complaints (acc_id, details, filed_date, status) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accId);
            stmt.setString(2, details);
            stmt.setString(3, filedDate);
            stmt.setString(4, status);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                response.setStatus(HttpServletResponse.SC_OK);
                out.println("Complaint filed successfully.");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.println("Failed to file complaint.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println("Error filing complaint.");
        } finally {
            out.flush();
            out.close();
        }
    }
}
