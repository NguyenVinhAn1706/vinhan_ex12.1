<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Murach's Java Servlets and JSP</title>
    <link rel="stylesheet" href="styles/main.css" type="text/css"/>
</head>
<body>

<%-- ✅ Nếu chưa có câu SQL thì set mặc định --%>
<c:if test="${sqlStatement == null}">
    <c:set var="sqlStatement" value="select * from [User];" />
</c:if>

<h1>The SQL Gateway</h1>
<p>Enter an SQL statement and click the Execute button.</p>

<p><b>SQL statement:</b></p>
<form action="sqlGateway" method="post">
    <textarea name="sqlStatement" cols="60" rows="8"><%=
    (session.getAttribute("sqlStatement") != null)
            ? session.getAttribute("sqlStatement")
            : "select * from [User];" %></textarea>
    <br>
    <input type="submit" value="Execute">
</form>

<p><b>SQL result:</b></p>
<div class="result">
    <c:out value="${sqlResult}" escapeXml="false"/>
</div>

</body>
</html>
