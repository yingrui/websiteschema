<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
            String scheId = (String) request.getParameter("scheId");
            scheId = scheId != null ? scheId : "";
%>
<html>
    <head>
        <base href="<%=basePath%>">
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Websiteschema Scheduler Management</title>

        <link rel="stylesheet" type="text/css" href="resources/css/Ext.ux.form.LovCombo.css">
        <script type="text/javascript" src="js/packages.js"></script>
        <script type="text/javascript" src="js/Ext.ux.form.LovCombo.js"></script>
        <script type="text/javascript" src="js/Ext.ux.ThemeCombo.js"></script>
        <script type="text/javascript" src="js/dwrproxy.js"></script>
        <script type="text/javascript" src="dwr/engine.js"></script>
        <script type="text/javascript" src="dwr/interface/ScheduleTaskService.js"></script>
    </head>

    <body>

        <div id="gridpanel"></div>

        <div id="info-panel"></div>

        <script type="text/javascript">
            var start = 0;
            var pageSize = 14;
            Ext.onReady(function(){
                Ext.QuickTips.init();
                var proxy = new Ext.data.DWRProxy(ScheduleTaskService.getResults, true);
                var recordType = new Ext.data.Record.create(scheduleTaskRecordType);
                var store=new Ext.data.Store({
                    proxy : proxy,
                    reader : new Ext.data.ListRangeReader(
                    {
                        id : 'id',
                        totalProperty : 'totalSize'
                    }, recordType
                ),
                    remoteSort: false

                });
                proxy.on('beforeload', function(thiz, params) {
                    params.status = Ext.getCmp('STATUS').getValue();
                    params.scheduleId = Ext.getCmp('SCHEDULEID').getValue();
                    params.sort = 'id desc';
                });

                var status_type_store = new Ext.data.SimpleStore(
                {
                    fields :['name','value'],
                    data:[
                        ['新建','0'],
                        ['已发送','1'],
                        ['未发送','2'],
                        ['启动','3'],
                        ['超时','4'],
                        ['结束','5'],
                        ['异常','6']
                    ]
                });

                var task_type_store = new Ext.data.SimpleStore(
                {
                    fields :['name','value'],
                    data:[
                        ['子链接','0'],
                        ['起始链接','1'],
                        ['内容链接','2']
                    ]
                });

                // the column model has information about grid columns
                // dataIndex maps the column to the specific data field in
                // the data store
                //var nm = new Ext.grid.RowNumberer();
                var fm = Ext.form;
                var sm = new Ext.grid.CheckboxSelectionModel();  // add checkbox column
                var cm = new Ext.grid.ColumnModel([
                    //nm,
                    sm,
                    {
                        header: 'ID',
                        dataIndex: 'id',
                        width: 40
                    },
                    {
                        header: '调度Id',
                        dataIndex: 'scheduleId',
                        width: 80,
                        editor: new fm.TextField({
                            allowBlank: false,
                            readOnly : true
                        })
                    },
                    {
                        header: '消息',
                        dataIndex: 'message',
                        width: 150,
                        editor: new fm.TextField({
                            allowBlank: false,
                            readOnly : true
                        })
                    },
                    {
                        header: '状态',
                        dataIndex: 'status',
                        width: 60,
                        hidden : false,
                        editor: new fm.ComboBox({
                            store : status_type_store,
                            triggerAction: 'all',
                            allowBlank: false,
                            forceSelection: true,
                            mode: 'local',
                            displayField:'name',
                            valueField:'value'

                        }),
                        renderer: function(value,metadata,record){
                            var index = status_type_store.find('value',value);
                            if(index!=-1){
                                return status_type_store.getAt(index).data.name;
                            }
                            return value;
                        }
                    },
                    {
                        header: '链接类型',
                        dataIndex: 'taskType',
                        width: 60,
                        hidden : false,
                        editor: new fm.ComboBox({
                            store : task_type_store,
                            triggerAction: 'all',
                            allowBlank: false,
                            forceSelection: true,
                            mode: 'local',
                            displayField:'name',
                            valueField:'value'

                        }),
                        renderer: function(value,metadata,record){
                            var index = task_type_store.find('value',value);
                            if(index!=-1){
                                return task_type_store.getAt(index).data.name;
                            }
                            return value;
                        }
                    },
                    {
                        header: '创建时间',
                        dataIndex: 'createTime',
                        width: 200,
                        editor: new fm.DateField({
                            allowBlank: false,
                            readOnly : true,
                            format: 'Y-m-d H:i:s'
                        })
                    },
                    {
                        header: '更新时间',
                        dataIndex: 'updateTime',
                        width: 200,
                        editor: new fm.DateField({
                            allowBlank: false,
                            readOnly : true,
                            format: 'Y-m-d H:i:s'
                        })
                    }
                ]);

                // by default columns are sortable
                cm.defaultSortable = false;

                var grid = new Ext.grid.EditorGridPanel({
                    //el:'topic-grid',
                    renderTo: 'gridpanel',
                    width: '100%',
                    autoHeight: true,
                    clicksToEdit:1,
                    autoScroll: true,
                    //title: '分页和排序列表',
                    store: store,
                    trackMouseOver: false,
                    loadMask: true,
                    enableHdMenu: true,
                    sm: sm,
                    cm: cm,

                    // inline toolbars
                    tbar: [ '->',
                        ' ', '调度ID', ' ',
                        {
                            xtype: 'textfield',
                            id: 'SCHEDULEID',
                            width: 80,
                            initEvents : function(){
                                var keyPressed = function(e) {
                                    if(e.getKey()==e.ENTER){
                                        handleQuery();
                                    }
                                };
                                this.el.on("keypress", keyPressed, this);
                            }
                        }, ' ',
                        ' ', '状态', ' ',
                        {
                            xtype: 'combo',
                            id: 'STATUS',
                            width: 80,
                            valueField: 'value',
                            displayField: 'name',
                            mode: 'local',
                            emptyText: '',
                            allowblank: true,
                            forceSelection: false,
                            store: status_type_store
                        }, ' ',
                        {
                            text: '检索',
                            handler: handleQuery
                        }, ' ',
                        {
                            text: '清空',
                            handler: function(){
                                Ext.getCmp('STATUS').setValue('');
                                Ext.getCmp('SCHEDULEID').setValue('');
                            }
                        }
                    ],
                    bbar: new Ext.PagingToolbar({
                        height: '22',
                        pageSize: pageSize,
                        store: store,
                        displayInfo: true
                    })
                });

                // render it
                grid.render();
                var j = '<%=scheId%>';
                Ext.getCmp('SCHEDULEID').setValue(j);

                // trigger the data store load
                store.load(
                {
                    params :
                        {
                        start : start,
                        limit : pageSize
                    }
                });

                function handleAdd(){
                    var p = new recordType();
                    grid.stopEditing();
                    p.set("jobId","0");
                    p.set("startURLId","0");
                    p.set("schedule",defaultSchedule)
                    p.set("scheduleType","0");
                    p.set("status","0");
                    store.insert(0, p);
                    grid.startEditing(0, 0);
                    ScheduleService.insert(p.data, function(){
                        store.reload();
                    });
                }

                function handleEdit(){
                    var mr = store.getModifiedRecords();
                    for(var i=0;i<mr.length;i++){
                        if(i == mr.length - 1) {
                            ScheduleService.update(mr[i].data, function(){
                                store.reload();
                            });
                        } else {
                            ScheduleService.update(mr[i].data);
                        }
                    }
                }

                //删除数据
                function handleDelete(){
                    var selections = grid.selModel.getSelections();
                    for (var i = 0,len = selections.length; i < len; i++) {
                        if(i == len - 1) {
                            ScheduleService.deleteRecord(selections[i].data, function(){
                                store.reload();
                            });
                        } else {
                            ScheduleService.deleteRecord(selections[i].data);
                        }
                    }
                }

                //启动调度器
                function handleLaunchScheduler(){
                    ScheduleService.launchScheduler(function(data){
                        if(data) {
                            alert("启动调度器成功！");
                        } else {
                            alert("启动调度器失败！");
                        }
                    });
                }

                //关闭调度器
                function handleShutdownScheduler(){
                    ScheduleService.shutdownScheduler(function(data){
                        if(data) {
                            alert("关闭调度器成功！");
                        } else {
                            alert("关闭调度器失败！");
                        }
                    });
                }

                function getStatusOfScheduler(){
                    ScheduleService.getStatusOfScheduler(function(data){
                        if(data == 1) {
                            alert("调度器正在运行！");
                        } else if(data == 2) {
                            alert("调度器已停止！");
                        } else if(data == 3) {
                            alert("调度器准备就绪！");
                        } else {
                            alert("调度器没有启动或状态异常！");
                        }
                    });
                }

                function handleQuery(){
                    store.load(
                    {
                        params :
                            {
                            start : start,
                            limit : pageSize
                        }
                    });
                }

                function editJob(grid, rowIndex, colIndex) {
                    var job= grid.getStore().getAt(rowIndex);
                    if(null != job) {
                        var id = job.data.jobId;
                        JobService.getById(id, function(data){
                            var editPanel = new JobEditorFormPanel();
                            Ext.getCmp('fp_id').setValue(data.id);
                            Ext.getCmp('fp_job').setValue(data.configure);
                            Ext.getCmp('fp_jobType').setValue(data.jobType);
                            Ext.getCmp('fp_wrapperType').setValue(data.wrapperId);
                            var AddWin = new Ext.Window({
                                title: '新建记录',
                                width: 600,
                                height: 350,
                                plain: true,
                                items: editPanel,
                                buttons: [{
                                        text: '保存',
                                        handler: function(){
                                            data.configure = editPanel.getComponent('fp_job').getValue();
                                            data.jobType = editPanel.getComponent('fp_jobType').getValue();
                                            data.jobType = editPanel.getComponent('fp_jobType').getValue();
                                            JobService.update(data, function(){
                                                store.reload();
                                            });
                                            AddWin.close();
                                        }
                                    }, {
                                        text: '取消',
                                        handler: function(){
                                            AddWin.close();
                                        }
                                    }]
                            });
                            AddWin.show(this);
                        });
                    }
                }

                function createTempJob(grid, rowIndex, colIndex) {
                    var sche= grid.getStore().getAt(rowIndex);
                    if(null != sche) {
                        ScheduleService.createTempJob(sche.data, function(success){
                            if(success) {
                                alert("添加任务成功！");
                            } else {
                                alert("添加任务失败！可能是没有启动调度器，或者你刚刚提交了一个相同的任务还没有执行结束");
                            }
                        });
                    }
                }
            });
        </script>

    </body>
</html>
