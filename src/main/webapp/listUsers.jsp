<%--
  Created by IntelliJ IDEA.
  User: duzer
  Date: 07.01.2017
  Time: 23:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:forEach items="${requestScope.userList}" var="user">
    <tr>
        <td><c:out value="${user.userId}"></c:out></td>
        <td><c:out value="${user.userName}"></c:out></td>
        <!-- кнопка удаления -->
        <td><input type="button" value="Удалить"></td>
        <%-- <td><input type="button" value="Удалить" onclick="jsDeleteBook(<c:out value="${book.idBook}"></c:out>)"></td> --%>
    </tr>
</c:forEach>
