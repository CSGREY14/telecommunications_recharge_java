import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/BroadbandPlansServlet")
public class BroadbandPlansServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        
        try {
            ResultSet rs = JDBCUtil.getAllBroadbandPlans();
            while (rs.next()) {
                int planId = rs.getInt("plan_id");
                String planName = rs.getString("plan_name");
                String planCategory = rs.getString("plan_category");
                String dataAndSpeed = rs.getString("data_and_speed");
                int validityInDays = rs.getInt("validity_in_days");
                double price = rs.getDouble("price");
                String voiceCalls = rs.getString("voice_calls");
                String staticIp = rs.getString("static_ip");
                String bundledOtt = rs.getString("bundled_ott");

                StringBuilder sb = new StringBuilder();
                sb.append(planId).append("___");
                sb.append(planName).append("___");
                sb.append(planCategory).append("___");
                sb.append(dataAndSpeed).append("___");
                sb.append(validityInDays).append("___");
                sb.append(price).append("___");
                sb.append(voiceCalls).append("___");
                sb.append(staticIp).append("___");
                sb.append(bundledOtt);

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
