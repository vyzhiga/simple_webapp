<%--
  Created by IntelliJ IDEA.
  User: duzer
  Date: 19.12.2016
  Time: 1:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:forEach items="${requestScope.bookList}" var="book">
    <tr>
        <td><c:out value="${book.idBook}"></c:out></td>
        <td><c:out value="${book.nameBook}"></c:out></td>
        <td><c:out value="${book.ISBNBook}"></c:out></td>
        <td><c:out value="${book.bookTaker}"></c:out></td>
        <!-- кнопка удаления -->
        <td><input type="button" value="Удалить" onclick="jsDeleteBook(<c:out value="${book.idBook}"></c:out>)"></td>
    </tr>
</c:forEach>