import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/BroadbandDetailsServlet")
public class BroadbandDetailsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int accId = Integer.parseInt(request.getParameter("accId"));
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        
        try {
            ResultSet rs = JDBCUtil.getBroadbandDetails(accId);
            while (rs.next()) {
                int planId = rs.getInt("plan_id");
                String phoneNo = rs.getString("phone_no");
                double currentDataUsage = rs.getDouble("cuurent_data_usage_in_GB");
                double maxDataUsage = rs.getDouble("max_data_in_GB");
                String lastTransactionDate = rs.getString("last_transaction_date");
                String nextDueDate = rs.getString("next_due_date");

                ResultSet planRs = JDBCUtil.getPlanDetails(planId);
                if (planRs.next()) {
                    String planName = planRs.getString("plan_name");
                    String planCategory = planRs.getString("plan_category");
                    String dataAndSpeed = planRs.getString("data_and_speed");
                    int otherBenefitsId = planRs.getInt("other_benefits_id");
                    int validity = planRs.getInt("validity_in_days");
                    double price = planRs.getDouble("price");

                    ResultSet benefitsRs = JDBCUtil.getOtherBenefits(otherBenefitsId);
                    String voiceCalls = " ";
                    String staticIp = " ";
                    String bundledOtt = " ";
                    if (benefitsRs.next()) {
                        voiceCalls = benefitsRs.getString("voice_calls");
                        staticIp = benefitsRs.getString("static_ip");
                        bundledOtt = benefitsRs.getString("bundled_ott");
                        
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append(planId).append("___");
                    sb.append(planName).append("___");
                    sb.append(planCategory).append("___");
                    sb.append(dataAndSpeed).append("___");
                    sb.append(price).append("___");
                    sb.append(validity).append("___");
                    sb.append(currentDataUsage).append("___");
                    sb.append(maxDataUsage).append("___");
                    sb.append(lastTransactionDate).append("___");
                    sb.append(nextDueDate).append("___");
                    sb.append(voiceCalls).append("___");
                    sb.append(staticIp).append("___");
                    sb.append(bundledOtt).append("___");
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
