/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

AddCipherPanel = function(){
    return new Ext.FormPanel({
        id : 'addCipherForm',
        //        frame: true,
        labelAlign: 'left',
        labelWidth: 130,
        autoScroll: true,
        items: [
        {
            fieldLabel: 'id',
            id: 'fp_id',
            hidden: true,
            xtype: 'textfield',
            allowBlank: false
        },
        {
            fieldLabel: '网站Id',
            id: 'fp_siteId',
            width: '100%',
            height: '6%',
            xtype: 'textfield',
            allowBlank: false
        }, {
            fieldLabel: '用户名',
            id: 'fp_username',
            width: '100%',
            height: '7%',
            xtype: 'textfield',
            editable: true
        }, {
            fieldLabel: '密码',
            id: 'fp_password',
            width: '100%',
            height: '7%',
            xtype: 'textfield',
            editable: true
        }, {
            fieldLabel: 'Cookie',
            id: 'fp_cookie',
            width: '100%',
            height: '40%',
            xtype: 'textarea',
            allowBlank: true
        }, {
            fieldLabel: 'Header',
            id: 'fp_header',
            width: '100%',
            height: '40%',
            xtype: 'textarea',
            allowBlank: true
        }
        ]
    });
};