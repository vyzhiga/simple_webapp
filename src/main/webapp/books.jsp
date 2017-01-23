<%--
  Created by IntelliJ IDEA.
  User: duzer
  Date: 21.12.2016
  Time: 0:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Книги</title>
    <script type="text/javascript"
            src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>

    <style type="text/css">
        .booksTbl {
            border: 4px double black; /* Рамка вокруг таблицы */
            border-collapse: collapse; /* Отображать только одинарные линии */
        }

        .booksTbl th {
            text-align: left; /* Выравнивание по левому краю */
            background: #ccc; /* Цвет фона ячеек */
            padding: 5px; /* Поля вокруг содержимого ячеек */
            border: 1px solid black; /* Граница вокруг ячеек */
        }

        .booksTbl td {
            padding: 5px; /* Поля вокруг содержимого ячеек */
            border: 1px solid black; /* Граница вокруг ячеек */
        }
    </style>
</head>

<body>

<%@ include file="header.jsp"%>

<c:if test="${empty sessionScope.sesCurUser}">
    Debug: Current user is not set yet.
</c:if>
<c:if test="${not empty sessionScope.sesCurUser}">
    Debug: Current user is set to <c:out value="${sesCurUser}"></c:out>
</c:if>

<br>

<div style="margin: 5px 0">
    <input id="load" type="button" value="Load Books"/>
    <input id="recqnt" type="number" min="1" defaultValue="5" value="5"/>
</div>

<table class="booksTbl">
    <thead>
    <tr>
        <th>ID</th>
        <th>NameBook</th>
        <th>ISBNBook</th>
        <th>Кем взята</th>
        <th>Удалить</th>
    </tr>
    </thead>
    <tbody style="display: none;"></tbody>
</table>

<script type="text/javascript" language="javascript">

    var getBooksUrl = "${pageContext.request.contextPath}/hw/getbooks"
    var numPage = 1;
    var recPerPage = 5;
    $(document).ready(function () {
        recPerPage = $("#recqnt").val();
        console.log("recpp", recPerPage);
        $("<tbody></tbody>").insertAfter("tbody:last").load(getBooksUrl + '?page=' + numPage + '&recPerPage=' + recPerPage);
        numPage = numPage + 1;
        $("#recqnt").click(function () {
            recPerPage = $("#recqnt").val();
            console.log("recpp", recPerPage);
        });
        $("#load").click(function () {
            $("<tbody></tbody>").insertAfter("tbody:last").load(getBooksUrl + '?page=' + numPage + '&recPerPage=' + recPerPage);
            numPage = numPage + 1;
        });
    });

    //удаляем книгу
    function jsDeleteBook(bookid) {
        var r = confirm("Удалить книгу с id="+bookid +"?");
        if (r == true) {
            $.get("${pageContext.request.contextPath}/hw/delbook?idDelBook="+bookid)
                .done(function() {
                    location.reload();
                })
        }
    }

    function jsChangeTaker(bookid, action, username) {
        // bookid - id книги
        // action - действие, 1 - взять, 0 - вернуть
        // username - имя "текущего" пользователя
        console.log("bookid", bookid);
        console.log("action", action);
        console.log("username", username);
        $.get("${pageContext.request.contextPath}/hw/changetaker?bookid="+bookid+"&action="+action+"&username="+username)
            .done(function() {
                location.reload();
            })
    }

</script>

</body>
</html>
