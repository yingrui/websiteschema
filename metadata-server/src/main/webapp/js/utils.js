/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function getCookie(name)//取cookies函数
{
    var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
    if(arr != null) return unescape(arr[2]);
    return null;
}

//可以按照如下方法来添加自动消失的消息。
//MsgTip.msg("", "添加用户失败: " + errorString, true, 5);
MsgTip = function(){
    var msgCt;
    function createBox(t, s){
        return ['<div class="msg">',
                '<div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>',
                '<div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc" style="font-size=12px;"><h3>', t, '</h3>', s, '</div></div></div>',
                '<div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>',
                '</div>'].join('');
    }
    return {
        msg : function(title, message,autoHide,pauseTime,showCloseBtn){
            if(!msgCt){
                msgCt = Ext.DomHelper.insertFirst(document.body, {id:'msg-div22',style:'position:absolute;top:10px;width:300px;margin:0 auto;z-index:20000;'}, true);
            }
            msgCt.alignTo(document, 't-t');
            //给消息框右下角增加一个关闭按钮
            if(Ext.isEmpty(showCloseBtn)||showCloseBtn==true){
                 message+='<br><span style="text-align:right;font-size:12px; width:100%;">' +
                 '<font color="blank"><u style="cursor:hand;" onclick="MsgTip.hide(this);">关闭</u></font></span>'
            }
            var m = Ext.DomHelper.append(msgCt, {html:createBox(title, message)}, true);
            m.slideIn('t');
            if(!Ext.isEmpty(autoHide)&&autoHide==true){
             if(Ext.isEmpty(pauseTime)){
              pauseTime=5;
             }
             m.pause(pauseTime).ghost("tr", {remove:true});
            }
        },
        hide:function(v){
         var msg=Ext.get(v.parentElement.parentElement.parentElement.parentElement.parentElement.parentElement);
         msg.ghost("tr", {remove:true});
        }
    };
}();