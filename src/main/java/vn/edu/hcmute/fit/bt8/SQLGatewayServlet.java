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
            // Load SQL Server JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Kết nối tới SQL Server
            String dbURL = "jdbc:sqlserver://localhost:1433;"
                    + "databaseName=murach;"
                    + "encrypt=true;trustServerCertificate=true;";
            String username = "mmud";
            String password = "nguyenvinhan1706";

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
