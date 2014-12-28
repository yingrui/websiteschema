<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
            String analyzerTips = (String) request.getAttribute("AnalyzerTips");
%>
<html>
    <head>
        <base href="<%=basePath%>">
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Websiteschema Start URL Management</title>

        <link rel="stylesheet" type="text/css" href="resources/css/Ext.ux.form.LovCombo.css">
        <script type="text/javascript" src="js/packages.js"></script>
        <script type="text/javascript" src="js/Ext.ux.form.LovCombo.js"></script>
        <script type="text/javascript" src="js/Ext.ux.ThemeCombo.js"></script>
        <script type="text/javascript" src="js/dwrproxy.js"></script>
        <script type="text/javascript" src="dwr/engine.js"></script>
        <script type="text/javascript" src="dwr/interface/StartURLService.js"></script>
        <script type="text/javascript" src="dwr/interface/ScheduleService.js"></script>
        <script type="text/javascript" src="dwr/interface/WrapperService.js"></script>
        <script type="text/javascript" src="dwr/interface/JobService.js"></script>
        <script type="text/javascript" src="js/wrapper/AddSchedulePanel.js"></script>
    </head>

    <body>

        <div id="gridpanel"></div>

        <script type="text/javascript">
            var start = 0;
            var pageSize = 20;
            Ext.onReady(function(){
                Ext.QuickTips.init();
                var proxy = new Ext.data.DWRProxy(StartURLService.getResults, true);
                var recordType = new Ext.data.Record.create(startURLRecordType);
                var Scheduler = new Ext.data.Record.create(scheduleRecordType);
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
                    params.match = Ext.getCmp('MATCH').getValue();
                    params.jobname = Ext.getCmp('JOBNAME').getValue();
                    params.name = Ext.getCmp('NAME').getValue();
                    params.url = Ext.getCmp('URL').getValue();
                    params.user = Ext.getCmp('CRUSER').getValue();
                    params.status = Ext.getCmp('STATUS').getValue();
                    params.sort = 'updateTime desc';
                });
                var url_status_store = new Ext.data.SimpleStore(
                {
                    fields :['name','value'],
                    data:[
                        ['已配置','1'],
                        ['未配置','0']
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
                        width: 50
                    },
                    {
                        header: '起始URL',
                        dataIndex: 'startURL',
                        width: 300,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: '网站ID',
                        dataIndex: 'siteId',
                        width: 120,
                        //                        hidden:true,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: '爬虫名称',
                        dataIndex: 'jobname',
                        width: 120,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: '栏目简称',
                        dataIndex: 'name',
                        width: 80,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: '状态',
                        dataIndex: 'status',
                        width: 50,
                        hidden : false,
                        editor: new fm.ComboBox({
                            store : url_status_store,
                            triggerAction: 'all',
                            allowBlank: false,
                            forceSelection: true,
                            mode: 'local',
                            displayField:'name',
                            valueField:'value'

                        }),
                        renderer: function(value,metadata,record){
                            var index = url_status_store.find('value',value);
                            if(index!=-1){
                                return url_status_store.getAt(index).data.name;
                            }
                            return value;
                        }
                    },
                    {
                        header: '配置',
                        width: 80,
                        xtype: 'actioncolumn',
                        items: [
                            {
                                icon   : 'resources/icons/fam/add.gif',  // Use a URL in the icon config
                                tooltip: '添加调度配置',
                                handler: handleAddScheduler
                            },
                            {
                                icon   : 'resources/icons/fam/accept.gif',  // Use a URL in the icon config
                                tooltip: '<%=analyzerTips%>',
                                handler: function(grid, rowIndex, colIndex) {
                                    var cookie = getCookie("websiteschema");
                                    if("analyzer" != cookie) {
                                        MsgTip.msg("", "您使用的浏览器不是websiteschema analyzer！", true, 5);
                                    }
                                }
                            },
                            {
                                icon   : 'resources/icons/fam/application_view_list.png',  // Use a URL in the icon config
                                tooltip: '查看采集到的数据',
                                handler: handleViewLog
                            },
                            {
                                icon   : 'resources/icons/fam/application_go.png',  // Use a URL in the icon config
                                tooltip: '查看调度',
                                handler: handleViewSchedule
                            }
                        ]
                    },
                    {
                        header: '创建时间',
                        dataIndex: 'createTime',
                        width: 200,
                        hidden : true,
                        editor: new fm.DateField({
                            allowBlank: false,
                            readOnly : true,
                            format: 'Y-m-d H:i:s'
                        })
                    },
                    {
                        header: '创建人',
                        dataIndex: 'createUser',
                        width: 100,
                        hidden : true,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: '修改时间',
                        dataIndex: 'updateTime',
                        width: 130,
                        editor: new fm.DateField({
                            allowBlank: false,
                            readOnly : true,
                            format: 'Y-m-d H:i:s'
                        })
                    },
                    {
                        header: '修改人',
                        dataIndex: 'lastUpdateUser',
                        width: 100,
                        editor: new fm.TextField({
                            allowBlank: false
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
                    tbar: [ {
                            text: '新建',
                            tooltip: '新建记录',
                            iconCls: 'icon-add',
                            handler: handleAdd
                        }, '-',
                        {
                            text: '提交',
                            tooltip: '提交修改记录',
                            iconCls: 'icon-edit',
                            handler: handleEdit
                        }, '-',
                        {
                            text: '删除',
                            tooltip: '删除记录',
                            iconCls: 'icon-delete',
                            handler: handleDelete
                        }, '->',
                        ' ', 'URL', ' ',
                        {
                            xtype: 'textfield',
                            id: 'URL',
                            width: 100,
                            initEvents : function(){
                                var keyPressed = function(e) {
                                    if(e.getKey()==e.ENTER){
                                        handleQuery();
                                    }
                                };
                                this.el.on("keypress", keyPressed, this);
                            }
                        }, ' ',
                        ' ', '爬虫名称', ' ',
                        {
                            xtype: 'textfield',
                            id: 'JOBNAME',
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
                        ' ', '网站Id', ' ',
                        {
                            xtype: 'textfield',
                            id: 'MATCH',
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
                        ' ', '栏目名称', ' ',
                        {
                            xtype: 'textfield',
                            id: 'NAME',
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
                        ' ', '创建者', ' ',
                        {
                            xtype: 'textfield',
                            id: 'CRUSER',
                            width: 50,
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
                            width: 60,
                            valueField: 'value',
                            displayField: 'name',
                            mode: 'local',
                            emptyText: '',
                            allowblank: true,
                            forceSelection: false,
                            store: url_status_store
                        }, ' ',
                        {
                            text: '检索',
                            handler: handleQuery
                        }, ' ',
                        {
                            text: '清空',
                            handler: function(){
                                Ext.getCmp('STATUS').setValue('');
                                Ext.getCmp('CRUSER').setValue('');
                                Ext.getCmp('MATCH').setValue('');
                                Ext.getCmp('JOBNAME').setValue('');
                                Ext.getCmp('NAME').setValue('');
                                Ext.getCmp('URL').setValue('');
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
                // trigger the data store load
                store.load(
                {
                    params : {
                        start : start,
                        limit : pageSize
                    }
                });
                function handleAdd(){
                    var p = new recordType();
                    grid.stopEditing();
                    p.set("siteId","siteId_here");
                    p.set("jobname","jobname_here");
                    p.set("status","0");
                    p.set("startURL","URL_here");
                    store.insert(0, p);
                    grid.startEditing(0, 0);
                    StartURLService.insert(p.data, function(){
                        store.reload();
                    });
                }

                function handleEdit(){
                    var mr = store.getModifiedRecords();
                    for(var i=0;i<mr.length;i++){
                        if(i == mr.length - 1) {
                            StartURLService.update(mr[i].data, function(){
                                store.reload();
                            });
                        } else {
                            StartURLService.update(mr[i].data);
                        }
                    }
                }

                //删除数据
                function handleDelete(){
                    var selections = grid.selModel.getSelections();
                    for (var i = 0,len = selections.length; i < len; i++) {
                        if(i == len - 1) {
                            StartURLService.deleteRecord(selections[i].data, function(){
                                store.reload();
                            });
                        } else {
                            StartURLService.deleteRecord(selections[i].data);
                        }
                    }
                }

                function handleAddScheduler(grid, rowIndex, colIndex){

                    var record= grid.getStore().getAt(rowIndex);
                    if(null != record) {
                        var editPanel = new AddSchedulePanel();
                        var data = record.data;
                        Ext.getCmp('fp_startURLId').setValue(data.id);
                        Ext.getCmp('fp_startURL').setValue(data.startURL);
                        var AddWin = new Ext.Window({
                            title: '新建记录',
                            width: 500,
                            height: 350,
                            plain: true,
                            items: editPanel,
                            buttons: [{
                                    text: '保存',
                                    handler: function(){
                                        var startURLId = data.id;
                                        var createSchedule = editPanel.getComponent('fp_createSchedule').getValue();
                                        var scheduleType = editPanel.getComponent('fp_scheduleType').getValue();
                                        var schedule = editPanel.getComponent('fp_schedule').getValue();
                                        //alert(startURLId);
                                        var createJob = editPanel.getComponent('fp_createJob').getValue();
                                        var jobType = editPanel.getComponent('fp_jobType').getValue();
                                        var configure = editPanel.getComponent('fp_job').getValue();
                                        var wrapperId = editPanel.getComponent('fp_wrapperType').getValue();
                                        if(createJob && createSchedule) {
                                            var JobRecordType = new Ext.data.Record.create(jobRecordType);
                                            var job = new JobRecordType();
                                            job.set("configure",configure);
                                            job.set("jobType",jobType);
                                            job.set("wrapperId", wrapperId);
                                            JobService.insert(job.data,
                                            {
                                                callback:function(jobId){
                                                    var ScheduleRecordType = new Ext.data.Record.create(scheduleRecordType);
                                                    var sche = new ScheduleRecordType();
                                                    sche.set("startURLId",startURLId);
                                                    sche.set("scheduleType",scheduleType);
                                                    sche.set("schedule",schedule);
                                                    sche.set("jobId",jobId);
                                                    ScheduleService.insert(sche.data,
                                                    {
                                                        callback:function(scheId){
                                                            MsgTip.msg("", "任务和调度添加成功", true, 3);
                                                            record.set("status","1");
                                                            StartURLService.update(record.data);
                                                        },
                                                        errorHandler:function(errorString, exception) {
                                                            MsgTip.msg("", "任务和调度添加失败: " + errorString, true, 5);
                                                        }
                                                    });
                                                },
                                                errorHandler:function(errorString, exception) {
                                                    MsgTip.msg("", "任务和调度添加失败: " + errorString, true, 5);
                                                }
                                            });
                                            AddWin.close();
                                            store.reload();
                                        } else {
                                            alert("调度未配置");
                                        }
                                    }
                                }, {
                                    text: '取消',
                                    handler: function(){
                                        AddWin.close();
                                    }
                                }]
                        });
                        AddWin.show(this);
                    }
                }

                function handleViewLog(grid, rowIndex, colIndex){
                    var record= grid.getStore().getAt(rowIndex);
                    var jobname = record.data.jobname;
                    var win = new Ext.Window({
                        id:'win',
                        width:600,
                        height:535,
                        closable:true,
                        resizable:true,
                        minimizable:true,
                        maximizable:true,
                        html: "<iframe src='views/metadata/url_log?jobname="+record.data.jobname+"' style='width:100%; height:100%;'></iframe>"
                    });
                    win.show();
                }

                function handleViewSchedule(grid, rowIndex, colIndex){
                    var record= grid.getStore().getAt(rowIndex);
                    var jobname = record.data.jobname;
                    var win = new Ext.Window({
                        id:'win',
                        width:600,
                        height:480,
                        closable:true,
                        resizable:true,
                        minimizable:true,
                        maximizable:true,
                        html: "<iframe src='views/metadata/url/schedule?jobname="+jobname+"' style='width:100%; height:100%;'></iframe>"
                    });
                    win.show();
                }

                function handleQuery(){
                    store.load(
                    {
                        params : {
                            start : start,
                            limit : pageSize
                        }
                    });
                }
            });
        </script>

    </body>
</html>
