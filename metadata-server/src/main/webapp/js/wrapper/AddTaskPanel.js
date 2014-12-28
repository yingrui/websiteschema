/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

AddTaskPanel = function(){
    return new Ext.FormPanel({
        id : 'addTaskForm',
        //        frame: true,
        labelAlign: 'left',
        labelWidth: 130,
        autoScroll: true,
        items: [
        {
            fieldLabel: 'scheId',
            id: 'fp_scheId',
            hidden: true,
            xtype: 'textfield',
            allowBlank: false
        }, {
            fieldLabel: '起始URL',
            id: 'fp_startURL',
            width: '100%',
            height: 20,
            xtype: 'textfield',
            allowBlank: false,
            editable: false
        }, {
            fieldLabel: '开始页码',
            id: 'fp_start',
            width: 200,
            height: 20,
            xtype: 'textfield'
        }, {
            fieldLabel: '结束页码',
            id: 'fp_end',
            width: 200,
            height: 20,
            xtype: 'textfield'
        }
        ]
    });
};