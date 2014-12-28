/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var wrapperProxy = new Ext.data.DWRProxy(WrapperService.getResults, true);
var wrt = new Ext.data.Record.create(wrapperRecordType);
var wrapper_type_store = new Ext.data.Store({
    proxy : wrapperProxy,
    reader : new Ext.data.ListRangeReader(
    {
        id : 'id',
        totalProperty : 'totalSize'
    }, wrt
    ),
    remoteSort: false
});
wrapper_type_store.load({
    params :{
        start : 0,
        limit : 100,
        sort : 'id desc'
    }
});

JobEditorFormPanel = function(){
    return new Ext.FormPanel({
        id : 'jobEditorForm',
        //        frame: true,
        labelAlign: 'left',
        labelWidth: 130,
        autoScroll: true,
        items: [
        {
            fieldLabel: '任务ID',
            id: 'fp_id',
            width: 200,
            height: 20,
            xtype: 'textfield',
            allowBlank: false,
            disabled: true
        }, {
            fieldLabel: '选择任务类型',
            id: 'fp_jobType',
            width: 200,
            height: 20,
            xtype: 'combo',
            valueField: 'value',
            displayField: 'name',
            store: job_type_store,
            //            selectOnFocus: true,
            emptyText: '请选择任务类型',
            editable: false,
            mode: 'local',
            forceSelection: true,
            typeAhead: true,
            triggerAction: 'all'
        }, {
            fieldLabel: '选择抽取器类型',
            id: 'fp_wrapperType',
            width: 200,
            height: 20,
            xtype: 'combo',
            valueField: 'id',
            displayField: 'name',
            store: wrapper_type_store,
            emptyText: '请选择抽取器类型',
            editable: false,
            mode: 'local',
            forceSelection: true,
            typeAhead: true,
            triggerAction: 'all'
        }, {
            fieldLabel: '配置',
            id: 'fp_job',
            width: '100%',
            height: '100%',
            xtype: 'textarea',
            allowBlank: true
        }
        ]
    });
};