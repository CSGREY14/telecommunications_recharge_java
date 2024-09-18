import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/PrepaidDetailsServlet")
public class PrepaidDetailsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String phoneNo = request.getParameter("phoneNo");
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        
        try {
            ResultSet rs = JDBCUtil.getPrepaidSubscriberDetails(phoneNo);
            while (rs.next()) {
                int planId = rs.getInt("plan_id");
                double currentData = rs.getDouble("current_data_in_GB");
                double maxData = rs.getDouble("max_data_in_GB");
                double currentTalktime = rs.getDouble("current_talktime");
                double maxTalktime = rs.getDouble("max_talktime");
                int currentSms = rs.getInt("current_sms");
                int maxSms = rs.getInt("max_sms");
                double totalTariff = rs.getDouble("total_tariff");
                String lastTransactionDate = rs.getString("last_transaction_date");
                String nextDueDate = rs.getString("next_due_date");
                String cityName = rs.getString("city_name");

                ResultSet planRs = JDBCUtil.getPrepaidPlanDetails(planId);
                if (planRs.next()) {
                    String planName = planRs.getString("plan_name");
                    int validity = planRs.getInt("validity_in_days");
                    double cost = planRs.getDouble("cost");
                    double freeTalktime = planRs.getDouble("free_talktime_in_rs");
                    double freeData = planRs.getDouble("free_data_in_gb");
                    double freeDailyData = planRs.getDouble("free_daily_data_in_gb");
                    int freeSms = planRs.getInt("free_sms");
                    int freeSmsPerDay = planRs.getInt("free_sms_per_day");
                    double dataCharges = planRs.getDouble("data_charges_rs_per_gb");
                    double callChargesOnNet = planRs.getDouble("call_charges_on_net");
                    double callChargesOffNet = planRs.getDouble("call_charges_off_net");
                    String category = planRs.getString("category");

                    StringBuilder sb = new StringBuilder();
                    sb.append(planId).append(",");
                    sb.append(planName).append(",");
                    sb.append(validity).append(",");
                    sb.append(cost).append(",");
                    sb.append(freeTalktime).append(",");
                    sb.append(freeData).append(",");
                    sb.append(freeDailyData).append(",");
                    sb.append(freeSms).append(",");
                    sb.append(freeSmsPerDay).append(",");
                    sb.append(dataCharges).append(",");
                    sb.append(callChargesOnNet).append(",");
                    sb.append(callChargesOffNet).append(",");
                    sb.append(category).append(",");
                    sb.append(currentData).append(",");
                    sb.append(maxData).append(",");
                    sb.append(currentTalktime).append(",");
                    sb.append(maxTalktime).append(",");
                    sb.append(currentSms).append(",");
                    sb.append(maxSms).append(",");
                    sb.append(totalTariff).append(",");
                    sb.append(lastTransactionDate).append(",");
                    sb.append(nextDueDate).append(",");
                    sb.append(cityName);

                    out.println(sb.toString());
                }
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
