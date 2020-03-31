<%--
  Created by IntelliJ IDEA.
  User: Andrey
  Date: 12/16/2018
  Time: 21:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <title>Main</title>
</head>
<body bgcolor="#fafad2">
<h1>Main</h1><br><br>
<div style=" background-color: aqua">
    <br>
    <div style="position: absolute; left: 5%; width: 95%; background-color: aqua">

        <a href="/people/">People</a><br>
        <a href="/regions/">Regions</a><br>
        <a href="/cities/">Cities</a><br>
        <a href="/houses/">Houses</a><br>
        <a href="/house-owners/">Owners of houses</a><br>

        <security:authorize access="hasRole('ROLE_ADMIN')">
            <a href="/users/">Users</a><br>
            <a href="/users/logs">Logs</a><br><br/>
        </security:authorize>

        <%--<a href="/config1/">Refresh configuration</a><br>--%>
        <br>
        <div>
            <button onClick="location.href='/logout'" class="btn btn-primary">Logout</button>
        </div>
    </div>
    <br><br><br><br><br><br><br><br><br><br><br><br>
</div>
<script type="text/javascript">
    var logout = function() {
        $.post("/logout", function() {
            $("#user").html('');
            $(".unauthenticated").show();
            $(".authenticated").hide();
        })
        return true;
    }
</script>
</body>
</html>
