/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
SchedulerEditorFormPanel = function(){
    return new Ext.FormPanel({
        id : 'schedulerEditorForm',         
        labelAlign: 'left',
        labelWidth: 130,
        autoScroll: true,
        items: [
        {
            fieldLabel: '调度器名称',            
            hiddenName: 'name',
            id: 'sch_name',
            width: 200,
            height: '40%',
            xtype: 'textfield',
            allowBlank: false
        }, {
            fieldLabel: '地址',
            name: 'address',
            id: 'sch_address',
            width: 200,
            height: '45%',
            xtype: 'textfield',
            allowBlank: false
        }
        ]
    });
};