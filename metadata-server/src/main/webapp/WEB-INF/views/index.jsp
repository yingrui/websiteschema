<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
    <head>
        <base href="<%=basePath%>"/>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Websiteschema Metadata Server</title>

        <meta name="description" content="websiteschema" />
        <link rel="stylesheet" type="text/css" href="resources/css/ext-all.css" />
        <link rel="stylesheet" type="text/css" href="resources/css/xtheme-gray.css" />
        <link rel="stylesheet" type="text/css" href="resources/docs.css" />
        <link rel="stylesheet" type="text/css" href="resources/style.css" />
        <link rel="stylesheet" type="text/css" href="style/style.css" />
        <style type="text/css"></style>
        <!-- GC -->
        <script type="text/javascript" src="js/packages.js"></script>
        <script type="text/javascript" src="js/console.js"></script>
    </head>
    <body scroll="no" id="console">
        <div id="loading-mask"></div>
        <div id="loading">
            <div class="loading-indicator">
                <img src="resources/extanim32.gif" width="32" height="32" style="margin-right:8px;" align="absmiddle" />
                Loading&hellip;
            </div>
        </div>


        <div id="top">
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td height="33">
                        <table width="100%" border="0" cellpadding="0" cellspacing="0" background="../static/images/window_main_them_title_bg.jpg">
                            <tr>
                                <td height="34">&nbsp;&nbsp;&nbsp;<span class="top_white_time"><b><%//=LocaleServer.getString("platform.lable.name", "")%></b></span></td>
                                <td width="200" align="center"><span>欢迎您 : <sec:authentication property="name"/></span>&nbsp;<span><a href="../j_spring_security_logout">注销</a></span></td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </div>

<!--        <div id="main"></div>-->

        <div id="bottom">
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td height="3" background="../static/images/copyright_bg.jpg"><img src="../static/images/copyright_top.gif" width="100%" height="3"></td>
                </tr>
                <tr>
                    <td height="28" background="../static/images/copyright_bg.jpg" class="copyrights_td">websiteschema：了解网站的结构</td>
                </tr>
            </table>
        </div>
    </body>
</html>