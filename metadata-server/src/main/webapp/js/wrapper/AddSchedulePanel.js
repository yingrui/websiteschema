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

var schedule_type_store = new Ext.data.SimpleStore(
{
    fields :['name','value'],
    data:[
    ['开始停止次数',1],
    ['CRONTAB',0],
    ['无效类型',-1]
    ]
});

AddSchedulePanel = function(){
    return new Ext.FormPanel({
        id : 'addScheduleForm',
        //        frame: true,
        labelAlign: 'left',
        labelWidth: 130,
        autoScroll: true,
        items: [
        {
            fieldLabel: '起始URLId',
            id: 'fp_startURLId',
            hidden: true,
            xtype: 'textfield',
            allowBlank: false
        }, {
            fieldLabel: '起始URL',
            id: 'fp_startURL',
            width: 200,
            height: 20,
            xtype: 'textfield',
            allowBlank: false,
            editable: false
        }, {
            fieldLabel: '新建调度',
            id: 'fp_createSchedule',
            width: 200,
            height: 20,
            xtype: 'checkbox'
        }, {
            fieldLabel: '选择调度方式',
            id: 'fp_scheduleType',
            width: 200,
            height: 20,
            xtype: 'combo',
            valueField: 'value',
            displayField: 'name',
            store: schedule_type_store,
            //            selectOnFocus: true,
            emptyText: '请选择调度方式',
            editable: false,
            mode: 'local',
            forceSelection: true,
            typeAhead: true,
            triggerAction: 'all'
        }, {
            fieldLabel: '请填写调度配置',
            id: 'fp_schedule',
            width: 200,
            height: 20,
            xtype: 'textfield',
            allowBlank: false
        }, {
            fieldLabel: '新建JOB',
            id: 'fp_createJob',
            width: 200,
            height: 20,
            xtype: 'checkbox'
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
        },{
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