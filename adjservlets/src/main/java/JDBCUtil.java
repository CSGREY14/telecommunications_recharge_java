
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class JDBCUtil {

    private static final String URL = "jdbc:mysql://localhost:3306/bsnl_database";
    private static final String USER = "root";
    private static final String PASSWORD = "skmuku123";

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static ResultSet getUserDetails(String email) throws Exception {
        Connection conn = getConnection();
        String query = "SELECT * FROM registration WHERE email = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, email);
        return stmt.executeQuery();
    }

    public static ResultSet getBroadbandDetails(int accId) throws Exception {
      Connection conn = getConnection();
      String query = "SELECT * FROM broadband_subscriber WHERE acc_id = ?";
      PreparedStatement stmt = conn.prepareStatement(query);
      stmt.setInt(1, accId);
      return stmt.executeQuery();
  }

  public static ResultSet getPlanDetails(int planId) throws Exception {
      Connection conn = getConnection();
      String query = "SELECT * FROM broadband_plan_list WHERE plan_id = ?";
      PreparedStatement stmt = conn.prepareStatement(query);
      stmt.setInt(1, planId);
      return stmt.executeQuery();
  }

  public static ResultSet getOtherBenefits(int otherBenefitsId) throws Exception {
      Connection conn = getConnection();
      String query = "SELECT * FROM broadband_other_details WHERE other_benefits_id = ?";
      PreparedStatement stmt = conn.prepareStatement(query);
      stmt.setInt(1, otherBenefitsId);
      return stmt.executeQuery();
  }
  
  public static ResultSet getPrepaidSubscriberDetails(String phoneNo) throws Exception {
	    Connection conn = getConnection();
	    String query = "SELECT * FROM prepaid_subscriber WHERE phone_no = ?";
	    PreparedStatement stmt = conn.prepareStatement(query);
	    stmt.setString(1, phoneNo);
	    return stmt.executeQuery();
	}

	public static ResultSet getPrepaidPlanDetails(int planId) throws Exception {
	    Connection conn = getConnection();
	    String query = "SELECT * FROM prepaid_plan_list WHERE plan_id = ?";
	    PreparedStatement stmt = conn.prepareStatement(query);
	    stmt.setInt(1, planId);
	    return stmt.executeQuery();
	}
	
	public static ResultSet getAllBroadbandPlans() throws Exception {
	    Connection conn = getConnection();
	    String query = "SELECT bpl.plan_id, bpl.plan_name, bpl.plan_category, bpl.data_and_speed, bpl.validity_in_days, bpl.price, bod.voice_calls, bod.static_ip, bod.bundled_ott " +
	                   "FROM broadband_plan_list bpl " +
	                   "LEFT JOIN broadband_other_details bod ON bpl.other_benefits_id = bod.other_benefits_id";
	    PreparedStatement stmt = conn.prepareStatement(query);
	    return stmt.executeQuery();
	}
	
	public static void addBroadbandSubscriber(int accId, String phoneNo, int planId, double currentDataUsage, double maxDataUsage, String lastTransactionDate, String nextDueDate) throws Exception {
	    Connection conn = getConnection();
	    String query = "INSERT INTO broadband_subscriber (acc_id, phone_no, plan_id, cuurent_data_usage_in_GB, max_data_in_GB, last_transaction_date, next_due_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
	    PreparedStatement stmt = conn.prepareStatement(query);
	    stmt.setInt(1, accId);
	    stmt.setString(2, phoneNo);
	    stmt.setInt(3, planId);
	    stmt.setDouble(4, currentDataUsage);
	    stmt.setDouble(5, maxDataUsage);
	    stmt.setString(6, lastTransactionDate);
	    stmt.setString(7, nextDueDate);
	    stmt.executeUpdate();
	    stmt.close();
	    conn.close();
	}

	public static ResultSet getAllPrepaidPlans() throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
            conn = getConnection(); // Implement this method to get connection
            String sql = "SELECT * FROM prepaid_plan_list";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            return rs;
	}
	
	public static void addPrepaidSubscriber(String phoneNo, int planId, double currentDataUsage,
            double maxDataUsage,double currentTalktime, double maxTalktime, double currentSms,
            double maxSms, double totalTariff, String lastTransactionDate, String nextDueDate, String cityName) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        
            conn = getConnection(); // Implement this method to get connection
            String sql = "INSERT INTO prepaid_subscriber (phone_no, plan_id, current_data_in_GB, max_data_in_GB, current_talktime, max_talktime, current_sms, max_sms, total_tariff, last_transaction_date, next_due_date, city_name)"
            		+ " VALUES (?, ?, ?, ?, ?, ?, ?,?,?,?,?,?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, phoneNo);
            stmt.setInt(2, planId);
            stmt.setDouble(3, currentDataUsage);
            stmt.setDouble(4, maxDataUsage);
            stmt.setDouble(5, currentTalktime);
            stmt.setDouble(6, maxTalktime);
            stmt.setDouble(7, currentSms);
            stmt.setDouble(8, maxSms);
            stmt.setDouble(9, totalTariff);
            stmt.setString(10, lastTransactionDate);
            stmt.setString(11, nextDueDate);
            stmt.setString(12, cityName);
            
            stmt.executeUpdate();
	}
	
	public static ResultSet getComplaints(int accId) throws Exception {
        String query = "SELECT complaint_id, acc_id, details, filed_date, status FROM complaints WHERE acc_id = ?";
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, accId);
        return pstmt.executeQuery();
    }

}
