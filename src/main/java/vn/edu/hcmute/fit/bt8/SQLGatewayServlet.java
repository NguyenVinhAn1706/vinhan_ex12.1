package vn.edu.hcmute.fit.bt8;

import java.io.*;
import jakarta.servlet.*;              // Tomcat 11 dùng jakarta.*
import jakarta.servlet.http.*;
import java.sql.*;

public class SQLGatewayServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String sqlStatement = request.getParameter("sqlStatement");
        String sqlResult = "";

        // Nếu người dùng không nhập thì gán mặc định
        if (sqlStatement == null || sqlStatement.trim().isEmpty()) {
            sqlStatement = "SELECT 1";
        }

        try {
            // Load PostgreSQL JDBC driver
            Class.forName("org.postgresql.Driver");

            // Kết nối tới PostgreSQL (Render yêu cầu sslmode=require)
            String dbURL = "jdbc:postgresql://dpg-d36bh2ogjchc73c608v0-a.oregon-postgres.render.com:5432/dbvinhan?sslmode=require";
            String username = "vinhan";
            String password = "aggX9mEugEiBK0r8fiDc1JxVxxh41E8Q";


            try (Connection connection = DriverManager.getConnection(dbURL, username, password);
                 Statement statement = connection.createStatement()) {

                sqlStatement = sqlStatement.trim();
                if (sqlStatement.length() >= 6) {
                    String sqlType = sqlStatement.substring(0, 6);

                    if (sqlType.equalsIgnoreCase("select")) {
                        try (ResultSet resultSet = statement.executeQuery(sqlStatement)) {
                            sqlResult = SQLUtil.getHtmlTable(resultSet);
                        }
                    } else {
                        int i = statement.executeUpdate(sqlStatement);
                        if (i == 0) {
                            sqlResult = "<p>✅ Statement executed successfully (DDL).</p>";
                        } else {
                            sqlResult = "<p>✅ Statement executed successfully.<br>"
                                    + i + " row(s) affected.</p>";
                        }
                    }
                }
            }

        } catch (ClassNotFoundException e) {
            sqlResult = "<p>❌ Error loading SQL Server JDBC driver:<br>"
                    + e.getMessage() + "</p>";
        } catch (SQLException e) {
            sqlResult = "<p>❌ Error executing SQL statement:<br>"
                    + e.getMessage() + "</p>";
        }

        // Lưu kết quả vào session
        HttpSession session = request.getSession();
        session.setAttribute("sqlResult", sqlResult);
        session.setAttribute("sqlStatement", sqlStatement);

        // Quay lại trang JSP
        getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
