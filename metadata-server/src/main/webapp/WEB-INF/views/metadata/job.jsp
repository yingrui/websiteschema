<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
    <head>
        <base href="<%=basePath%>">
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Websiteschema Job Management</title>

        <link rel="stylesheet" type="text/css" href="resources/css/Ext.ux.form.LovCombo.css">
        <script type="text/javascript" src="js/packages.js"></script>
        <script type="text/javascript" src="js/Ext.ux.form.LovCombo.js"></script>
        <script type="text/javascript" src="js/Ext.ux.ThemeCombo.js"></script>
        <script type="text/javascript" src="js/dwrproxy.js"></script>
        <script type="text/javascript" src="dwr/engine.js"></script>
        <script type="text/javascript" src="dwr/interface/JobService.js"></script>
        <script type="text/javascript" src="dwr/interface/WrapperService.js"></script>
        <script type="text/javascript" src="js/wrapper/JobEditorPanel.js"></script>
    </head>

    <body>

        <div id="gridpanel"></div>

        <div id="info-panel"></div>

        <script type="text/javascript">
            var start = 0;
            var pageSize = 20;
            Ext.onReady(function(){
                Ext.QuickTips.init();
                var proxy = new Ext.data.DWRProxy(JobService.getResults, true);
                var recordType = new Ext.data.Record.create(jobRecordType);
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
                    params.user = Ext.getCmp('CRUSER').getValue();
                    params.jobType = Ext.getCmp('JOBTYPE').getValue();
                    params.sort = 'j.updateTime desc';
                });
                var type_store = job_type_store;

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
                    },{
                        header: '抽取器',
                        dataIndex: 'wrapperId',
                        width: 100,
                        hidden : false,
                        
                        editor: new fm.ComboBox({
                            store : wrapper_type_store,
                            triggerAction: 'all',
                            allowBlank: false,
                            mode: 'local',
                            forceSelection: true,
                            displayField:'name',
                            valueField:'id'
                        }),
                        renderer: function(value,metadata,record){
                            var index = wrapper_type_store.find('id',value);
                            if(index!=-1){
                                return wrapper_type_store.getAt(index).data.name;
                            }
                            return value;
                        }
                    },
                    {
                        header: '配置',
                        dataIndex: 'configure',
                        width: 300,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: '类型',
                        dataIndex: 'jobType',
                        width: 200,
                        hidden : false,
                        editor: new fm.ComboBox({
                            store : type_store,
                            triggerAction: 'all',
                            allowBlank: false,
                            forceSelection: true,
                            mode: 'local',
                            displayField:'name',
                            valueField:'value'
                        }),
                        renderer: function(value,metadata,record){
                            var index = type_store.find('value',value);
                            if(index!=-1){
                                return type_store.getAt(index).data.name;
                            }
                            return value;
                        }
                    },
                    {
                        header: '编辑配置',
                        width: 60,
                        xtype: 'actioncolumn',
                        items: [
                            {
                                icon   : 'resources/icons/fam/cog_edit.png',  // Use a URL in the icon config
                                tooltip: '编辑配置',
                                handler: editJob
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
                        ' ', '状态', ' ',
                        {
                            xtype: 'combo',
                            id: 'JOBTYPE',
                            width: 100,
                            valueField: 'value',
                            displayField: 'name',
                            mode: 'local',
                            emptyText: '',
                            allowblank: true,
                            forceSelection: false,
                            store: job_type_store
                        }, ' ',
                        ' ', '创建用户', ' ',
                        {
                            xtype: 'textfield',
                            id: 'CRUSER',
                            initEvents : function(){
                                var keyPressed = function(e) {
                                    if(e.getKey()==e.ENTER){
                                        handleQuery();
                                    }
                                };
                                this.el.on("keypress", keyPressed, this);
                            }
                        },
                        ' ', '网站ID', ' ',
                        {
                            xtype: 'textfield',
                            id: 'MATCH',
                            initEvents : function(){
                                var keyPressed = function(e) {
                                    if(e.getKey()==e.ENTER){
                                        handleQuery();
                                    }
                                };
                                this.el.on("keypress", keyPressed, this);
                            }
                        }, ' ',
                        {
                            text: '检索',
                            handler: handleQuery
                        }, ' ',
                        {
                            text: '清空',
                            handler: function(){
                                Ext.getCmp('CRUSER').setValue('');
                                Ext.getCmp('MATCH').setValue('');
                                Ext.getCmp('JOBTYPE').setValue('');
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

                var p = '<p>任务配置是为相应的抽取器设置参数，每一个任务关联一个抽取器，这些抽取器需要的参数就来自这里的配置<br/>\n\
                任务类型1：基于消息队列的任务，将相关的起始URL加入至采集队列中。任务类型2：更新历史新闻的转发和点击信息，将起始URL相关的内容页加入至采集队列中，主要用于更新点击量、转发、回帖等信息。</p>'


                new Ext.Panel({
                    title: '任务配置与抽取器',
                    preventBodyReset: true,
                    renderTo: 'info-panel',
                    width: '100%',
                    html: p
                });
                
                function handleAdd(){
                    var p = new recordType();
                    grid.stopEditing();
                    p.set("configure","");
                    p.set("jobType","websiteschema.schedule.job.JobAMQPQueueV1");
                    store.insert(0, p);
                    grid.startEditing(0, 0);
                    JobService.insert(p.data, function(){
                        store.reload();
                    });
                }

                function handleEdit(){
                    var mr = store.getModifiedRecords();
                    for(var i=0;i<mr.length;i++){
                        if(i == mr.length - 1) {
                            JobService.update(mr[i].data, function(){
                                store.reload();
                            });
                        } else {
                            JobService.update(mr[i].data);
                        }
                    }
                }

                //删除数据
                function handleDelete(){
                    var selections = grid.selModel.getSelections();
                    for (var i = 0,len = selections.length; i < len; i++) {
                        if(i == len - 1) {
                            JobService.deleteRecord(selections[i].data, function(){
                                store.reload();
                            });
                        } else {
                            JobService.deleteRecord(selections[i].data);
                        }
                    }
                }

                function handleQuery(){
                    alert(wrapper_type_store.getTotalCount())
                    store.reload();
                }

                function editJob(grid, rowIndex, colIndex) {
                    var record= grid.getStore().getAt(rowIndex);
                    if(null != record) {
                        var data = record.data;
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
                    }
                }
            });
        </script>

    </body>
</html>
