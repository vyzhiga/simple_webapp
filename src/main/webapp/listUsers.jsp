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
        <td><a href="#"><c:out value="${user.userName}"></c:out></a></td>
        <!-- кнопка удаления -->
        <td><input type="button" value="Удалить" onclick="jsDeleteUser(<c:out value="${user.userId}"></c:out>)"></td>
    </tr>
</c:forEach>
