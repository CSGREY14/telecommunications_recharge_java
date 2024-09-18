
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/UserDetailsServlet")
public class UserDetailsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve email parameter from request
        String email = request.getParameter("email");

        // Set response content type
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        try {
            // Get user details from JDBCUtil
            ResultSet rs = JDBCUtil.getUserDetails(email);
            
            // Process result set and construct response
            if (rs.next()) {
                String username = rs.getString("uname");
                String phoneNo = rs.getString("phone_no");
                int accId = rs.getInt("acc_id");

                // Construct response as comma-separated values
                String userDetails = username + "," + email + "," + phoneNo + "," + accId;
                out.print(userDetails);
            } else {
                out.print("User not found");
            }

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.print("Error: " + e.getMessage());
        } finally {
            out.flush();
            out.close();
        }
    }
}
