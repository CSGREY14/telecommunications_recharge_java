

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String accId = request.getParameter("acc_id");
        String category = request.getParameter("acc_category");
        String username = request.getParameter("uname");
        String password = request.getParameter("pword");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone_no");

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bsnl_database", "root", "skmuku123")) {
            String sql = "INSERT INTO registration (acc_id, acc_category, uname, pword, email, phone_no) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, accId);
            statement.setString(2, category);
            statement.setString(3, username);
            statement.setString(4, password);
            statement.setString(5, email);
            statement.setString(6, phone);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                PrintWriter out = response.getWriter();
                out.println("success");
            } else {
                PrintWriter out = response.getWriter();
                out.println("failure");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            PrintWriter out = response.getWriter();
            out.println("failure");
        }
    }
}
