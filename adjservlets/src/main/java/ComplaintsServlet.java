import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ComplaintsServlet")
public class ComplaintsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int accId = Integer.parseInt(request.getParameter("accId"));
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        
        try {
            ResultSet rs = JDBCUtil.getComplaints(accId);
            while (rs.next()) {
                int complaintId = rs.getInt("complaint_id");
                int accountId = rs.getInt("acc_id");
                String details = rs.getString("details");
                String filedDate = rs.getString("filed_date");
                String status = rs.getString("status");

                StringBuilder sb = new StringBuilder();
                sb.append(complaintId).append("___");
                sb.append(accountId).append("___");
                sb.append(details).append("___");
                sb.append(filedDate).append("___");
                sb.append(status);
                out.println(sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println("Error retrieving data");
        } finally {
            out.flush();
            out.close();
        }
    }
}
