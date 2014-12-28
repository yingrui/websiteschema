/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

Ext.QuickTips.init();//支持tips提示
//Ext.form.Field.prototype.msgTarget= 'qtip';//提示的方式，枚举值为”qtip”,”title”,”under”,”side”,id(元素id)
Ext.form.Field.prototype.msgTarget = 'under';

CategoryAddFormPanel = function(){
    return new Ext.FormPanel({
        id : 'CategoryAddForm',
        labelWidth: 125,
        frame: true,
        defaults: {
            width: 175
        },
        defaultType: 'textfield',
        items: [
        {
            fieldLabel: '父节点',
            id: 'fp_parentId',
            xtype: 'textfield',
            allowBlank: false,
            disabled: true
        },{
            fieldLabel: '新类型',
            id: 'fp_Name',
            xtype: 'textfield',
            allowBlank: false,
            disabled: false,
            blankText: '请填写类型名称'
        },
        {
            fieldLabel: '类型描述',
            id: 'fp_Desc',
            xtype: 'textfield',
            allowBlank: false,
            blankText: '请填写类型描述'
        }
        ]
    });
};