import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/SubscribeServlet")
public class SubscribeServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int accId = Integer.parseInt(request.getParameter("accId"));
        String phoneNo = request.getParameter("phoneNo");
        int planId = Integer.parseInt(request.getParameter("planId"));
        double currentDataUsage = 0.0;
        double maxDataUsage = Double.parseDouble(request.getParameter("maxDataUsage"));
        String lastTransactionDate = request.getParameter("lastTransactionDate");
        String nextDueDate = request.getParameter("nextDueDate");

        try {
            JDBCUtil.addBroadbandSubscriber(accId, phoneNo, planId, currentDataUsage, maxDataUsage, lastTransactionDate, nextDueDate);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
