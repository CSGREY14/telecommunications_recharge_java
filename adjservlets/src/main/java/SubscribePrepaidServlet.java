import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/SubscribePrepaidServlet")
public class SubscribePrepaidServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String phoneNo = request.getParameter("phoneNo");
        int planId = Integer.parseInt(request.getParameter("planId"));
        double currentDataUsage = 0.0,currentTalktime=0.0, currentSms=0.0; // Assuming starting usage is 0.0 for new subscriptions
        // Max data usage can be fetched from the prepaid_plan_list if needed
        double maxDataUsage = Double.parseDouble(request.getParameter("maxDataUsage"));
        double maxTalktime = Double.parseDouble(request.getParameter("maxTalktime"));
        double maxSms = Double.parseDouble(request.getParameter("maxSms"));
        double totalTariff = Double.parseDouble(request.getParameter("totalTariff"));
        
         
        String lastTransactionDate = request.getParameter("lastTransactionDate");
        String nextDueDate = request.getParameter("nextDueDate");
        String cityName = request.getParameter("cityName");

        try {
            JDBCUtil.addPrepaidSubscriber(phoneNo, planId, currentDataUsage, maxDataUsage, currentTalktime, maxTalktime, currentSms, maxSms, totalTariff, lastTransactionDate, nextDueDate, cityName);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
