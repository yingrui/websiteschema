<%-- 
    Document   : main_page
    Created on : Sep 12, 2011, 10:13:28 AM
    Author     : ray
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
    <head>
        <base href="<%=basePath%>"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Personal Info</title>

        <script type="text/javascript" src="dwr/interface/UserService.js"></script>
        <script type="text/javascript" src="dwr/engine.js"></script>
        <script type="text/javascript" src="js/md5.js"></script>
        <script type="text/javascript" src="js/packages.js"></script>
    </head>
    <body>
        <div id='user' username='<sec:authentication property="name"/>'/>
        <div id="main-panel"/>
        <script type="text/javascript" >
            var username = Ext.get('user').getAttribute('username');
            var personInfoPanelName = '个人信息';
            var passwdPanelName = '密码管理';

            var callback = function(userInfo){
                var tabs = new Ext.FormPanel({
                    border:false,
                    items: {
                        xtype:'tabpanel',
                        activeTab: 0,
                        defaults:{autoHeight:true, bodyStyle:'padding:10px'},
                        items:[{
                                title: personInfoPanelName,
                                layout:'form',
                                defaults: {width: 230},
                                defaultType: 'textfield',

                                items: [{
                                        fieldLabel: '姓名',
                                        name: 'name',
                                        id:'name',
                                        allowBlank:false,
                                        value: userInfo.name
                                    }, {
                                        fieldLabel: '电子邮件',
                                        name: 'email',
                                        id:'email',
                                        vtype:'email',
                                        value: userInfo.email
                                    }]
                            },{
                                title: passwdPanelName,
                                layout:'form',
                                defaults: {width: 230},
                                defaultType: 'textfield',

                                items: [{
                                        fieldLabel: '旧密码',
                                        inputType:'password',
                                        name: 'oldpasswd',
                                        id: 'oldpasswd'
                                    },{
                                        fieldLabel: '新密码',
                                        inputType:'password',
                                        name: 'newpasswd',
                                        id: 'newpasswd'
                                    },{
                                        fieldLabel: '重复输入密码',
                                        inputType:'password',
                                        name: 'repeatpasswd',
                                        id: 'repeatpasswd'
                                    }]
                            }]
                    },

                    buttons: [{
                            text: '保存',
                            handler: function() {
                                var activePanel = tabs.getComponent(0).getActiveTab()
                                if(personInfoPanelName == activePanel.title) {
                                    var email = activePanel.getComponent('email').getValue();
                                    var name = activePanel.getComponent('name').getValue();
                                    userInfo.email = email;
                                    userInfo.name = name;
                                    UserService.update(userInfo, function(){
                                        Ext.MessageBox.alert("信息保存成功！");
                                    });
                                } else {
                                    var oldpasswd = hex_md5(activePanel.getComponent('oldpasswd').getValue());
                                    var newpasswd = hex_md5(activePanel.getComponent('newpasswd').getValue());
                                    var repeatpasswd = hex_md5(activePanel.getComponent('repeatpasswd').getValue());
                                    if(oldpasswd == userInfo.passwd) {
                                        Ext.MessageBox.alert(activePanel.getComponent('oldpasswd').getValue());
                                        if(newpasswd == repeatpasswd) {
                                            userInfo.passwd = repeatpasswd;
                                            UserService.update(userInfo, function(){
                                                Ext.MessageBox.alert("密码更新成功！");
                                            });
                                        } else {
                                            Ext.MessageBox.alert("重复密码不一致！");
                                        }
                                    } else {
                                        Ext.MessageBox.alert("原有密码输入错误！");
                                    }
                                }
                            }
                        }]
                });

                tabs.render("main-panel");
            }


            Ext.onReady(function(){
                UserService.getByUserId(username, callback);
            });
        </script>
    </body>
</html>
