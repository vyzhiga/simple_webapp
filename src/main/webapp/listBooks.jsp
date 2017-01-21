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
        <!-- колонка "Кем взята" -->
        <c:choose>
            <%-- вернуть, если пользователи совпадают --%>
            <c:when test="${book.bookTaker==curUser}">
                <td>
                    <input type="button" value="Вернуть">
                </td>
            </c:when>
            <%-- взять, если книга никем не взята --%>
            <c:when test="${book.bookTaker==null && curUser!=\"\"}">
                <td>
                    <input type="button" value="Взять">
                </td>
            </c:when>
            <%-- вернуть имя пользователя, вхявшего книгу --%>
            <c:otherwise>
                <td>
                    ${book.bookTaker}
                </td>
            </c:otherwise>
        </c:choose>
        <!-- кнопка удаления -->
        <td><input type="button" value="Удалить" onclick="jsDeleteBook(<c:out value="${book.idBook}"></c:out>)"></td>
    </tr>
</c:forEach>