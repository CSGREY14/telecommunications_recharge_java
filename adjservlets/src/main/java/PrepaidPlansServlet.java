import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/PrepaidPlansServlet")
public class PrepaidPlansServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        
        try {
            ResultSet rs = JDBCUtil.getAllPrepaidPlans();
            while (rs.next()) {
                int planId = rs.getInt("plan_id");
                String planName = rs.getString("plan_name");
                int validityInDays = rs.getInt("validity_in_days");
                double cost = rs.getDouble("cost");
                double freeTalktime = rs.getDouble("free_talktime_in_rs");
                double freeData = rs.getDouble("free_data_in_gb");
                double freeDailyData = rs.getDouble("free_daily_data_in_gb");
                int freeSms = rs.getInt("free_sms");
                int freeSmsPerDay = rs.getInt("free_sms_per_day");
                double dataCharges = rs.getDouble("data_charges_rs_per_gb");
                double callChargesOnNet = rs.getDouble("call_charges_on_net");
                double callChargesOffNet = rs.getDouble("call_charges_off_net");
                String category = rs.getString("category");

                StringBuilder sb = new StringBuilder();
                sb.append(planId).append("___");
                sb.append(planName).append("___");
                sb.append(validityInDays).append("___");
                sb.append(cost).append("___");
                sb.append(freeTalktime).append("___");
                sb.append(freeData).append("___");
                sb.append(freeDailyData).append("___");
                sb.append(freeSms).append("___");
                sb.append(freeSmsPerDay).append("___");
                sb.append(dataCharges).append("___");
                sb.append(callChargesOnNet).append("___");
                sb.append(callChargesOffNet).append("___");
                sb.append(category);

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
