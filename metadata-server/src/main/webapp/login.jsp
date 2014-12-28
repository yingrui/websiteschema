<%-- 
    Document   : login
    Created on : Sep 8, 2011, 1:19:20 PM
    Author     : ray
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <title>Login Page</title>
        <!--        <link rel="stylesheet" type="text/css" href="resources/css/xtheme-gray.css" />-->
        <link rel="stylesheet" type="text/css" href="resources/docs.css" />
        <!-- GC -->
        <style type="text/css" >
            .main{
                width:100%;
                height:100%;
                text-align:center;
                vertical-align:middle;
                background-color: gray;
            }

            .login{
                text-align:center;
                vertical-align:middle;
                margin-right:auto;
                margin-left:auto;
                /*                margin-left:auto;
                                width:500px;
                                height:100%;*/
            }
        </style>
        <script language="JavaScript">
            if (window != top)
                top.location.href = location.href;
        </script>   
    </head>
    <body>
        <table class="main" id="main">
            <tr>
                <td>&nbsp;</td>
                <td>

                    <form name='f' action='/security_check' method='POST'>
                        <table  class="login" id="login">
                            <tr>
                            <h3>登录Websiteschema</h3>
                            </tr>
                            <tr>
                                <td>用户名:</td><td><input type='text' name='j_username' value=''></td>
                            </tr>
                            <tr>
                                <td>密码:</td><td><input type='password' name='j_password'/></td>
                            </tr>
                            <tr>
                                <td><input type='checkbox' name='_spring_security_remember_me'/></td>
                                <td>记住本机的登录状态.</td>
                            </tr>
                            <tr>
                            </tr>
                        </table>
                        <input name="submit" value="登录" type="submit"/><input name="reset" value="清空" type="reset"/>
                    </form>
                </td>
                <td>&nbsp;</td>
            </tr>
        </table>
    </body>
</html>