<%--
  Created by IntelliJ IDEA.
  User: duzer
  Date: 21.12.2016
  Time: 1:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="utf-8">
    <title>Пользователи</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>

    <%-- Loading jquery-ui --%>
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/smoothness/jquery-ui.css">
    <script src="//code.jquery.com/jquery-1.12.4.js"></script>
    <script src="//code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

</head>
<body>

    <table>
        <tr>
            <td><a href="${pageContext.request.contextPath}/books.jsp">Книги</a></td>
            <td><a href="${pageContext.request.contextPath}/hw/getusers">Пользователи</a></td>
        </tr>
    </table>

    <button type="button" id="opener">Добавить пользователя</button>
    <div id="dialog" title="Добавить пользователя">
        Имя пользователя: <input type="text" id="username"><br>
        Пароль: <input type="text" id="password">
    </div>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Пользователь</th>
                <th></th>
            </tr>
        </thead>
        <tbody>
        <c:forEach items="${requestScope.userList}" var="user">
            <tr>
                <td><c:out value="${user.userId}"></c:out></td>
                <td><a href="#"><c:out value="${user.userName}"></c:out></a></td>
                <!-- кнопка удаления -->
                <td><input type="button" value="Удалить" onclick="jsDeleteUser(<c:out value="${user.userId}"></c:out>)"></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <script type="text/javascript" language="javascript">

        //удаляем книгу
        function jsDeleteUser(userid) {
            var r = confirm("Удалить пользователя с id="+userid +"?");
            if (r == true) {
                window.location.href = "${pageContext.request.contextPath}/hw/deluser?id="+userid;
            }
        }

        //вызов модального диалога
        $( "#dialog" ).dialog({
            autoOpen: false,
            closeOnEscape: false,
            resizable: false,
            modal: true,
            buttons: {
                OK: function() {
                    var addUser = $("#username").val();
                    var addUserPass = $("#password").val();
                    console.log("username", addUser);
                    $.get("${pageContext.request.contextPath}/hw/adduser?addUser="+addUser+"&addPass="+addUserPass)
                    $(this).dialog("close");
                },
                Cancel: function() {
                    $(this).dialog("close")
                }

            }
        });
        $( "#opener" ).click(function() {
            $( "#dialog" ).dialog( "open" );
        });

    </script>

</body>
</html>
