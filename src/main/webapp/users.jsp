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

<%@ include file="header.jsp"%>

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
            <td><a href="#" onclick="jsGetUserDetails(<c:out value="${user.userId}"></c:out>)"><c:out value="${user.userName}"></c:out></a></td>
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

    function jsGetUserDetails(userid) {
        $.get("${pageContext.request.contextPath}/hw/getuserdetails?userid="+userid,
            function(data) {
                if (data.Result == 1) {
                    $("#username").prop("disabled", true);
                    $("#password").prop("disabled", true);
                    $("#username").val(data.user);
                    $("#password").val(data.pass);
                    $("#dialog").dialog("option", {
                            title : "Пользователь "+data.user ,
                            buttons : { OK : function() {$(this).dialog("close");}}
                        }
                    );
                    $("#dialog").dialog("open");
                }
            }, "json"
        )
    }

    //вызов модального диалога
    $("#dialog").dialog({
        autoOpen: false,
        closeOnEscape: false,
        resizable: false,
        modal: true,
        close: function() {
            window.location.href = "${pageContext.request.contextPath}/hw/getusers";
        }
    });
    $( "#opener" ).click(function() {
        $("#username").val("");
        $("#password").val("");
        $("#username").prop("disabled", false);
        $("#password").prop("disabled", false);
        $("#dialog").dialog("option", {
                title : "Добавить пользователя",
                buttons: {
                    OK: function() {
                        var addUser = $("#username").val();
                        var addUserPass = $("#password").val();
                        console.log("username", addUser);
                        $.get("${pageContext.request.contextPath}/hw/adduser?addUser="+addUser+"&addPass="+addUserPass,
                            function(data) {
                                if (data.Result == 1) {
                                    alert("Пользователь с именем "+addUser+" уже существует! Укажите другое имя.");
                                }
                            }, "json"
                        )
                            .done(function() {
                                $("#dialog").dialog("close");
                            })
                    },
                    Cancel: function() {
                        $(this).dialog("close")
                    }

                }
            }
        );
        $("#dialog").dialog("open");
    });

</script>

</body>
</html>
