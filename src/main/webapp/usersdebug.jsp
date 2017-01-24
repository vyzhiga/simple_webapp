<%--
  Created by IntelliJ IDEA.
  User: duzer
  Date: 20.01.2017
  Time: 23:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<html>
<head>
    <title>Users debug</title>
</head>
<body>

    <%@ include file="header.jsp"%>

    <sql:setDataSource var="h2db" driver="org.h2.Driver"
                       url="jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
                       user=""  password=""/>

    <sql:query dataSource="${h2db}" var="result">
        SELECT id, name FROM users;
    </sql:query>

    <form action="${pageContext.request.contextPath}/hw/setuser" method="get">
        <fieldset>
            <legend>Select user:</legend>
            User:
            <select name="username">
                <c:forEach var="row" items="${result.rows}">
                    <option value="<c:out value="${row.name}"/>"><c:out value="${row.name}"/></option>
                </c:forEach>
            </select>
            <input type="submit" value="Set">
        </fieldset>
    </form>

</body>
</html>
