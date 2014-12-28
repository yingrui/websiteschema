<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
    <head>
        <base href="<%=basePath%>">
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>宏爵财经资讯（北京）有限公司</title>

        <link rel="stylesheet" type="text/css" href="resources/css/Ext.ux.form.LovCombo.css">
        <style type="text/css"></style>
        <script type="text/javascript" src="js/packages.js"></script>
        <script type="text/javascript" src="js/Ext.ux.form.LovCombo.js"></script>
        <script type="text/javascript" src="js/Ext.ux.ThemeCombo.js"></script>
        <script type="text/javascript" src="js/dwrproxy.js"></script>
        <script type="text/javascript" src="dwr/engine.js"></script>
        <script type="text/javascript" src="dwr/interface/TaskLogService.js"></script>
    </head>

    <body>

        <div id="gridpanel"></div>

        <script type="text/javascript">
            var start = 0;
            var pageSize = 10;
            Ext.onReady(function(){

                var proxy = new Ext.data.DWRProxy(TaskLogService.getTaskLogs, true);
                var recordType = new Ext.data.Record.create([
                                    {
                                        name : 'task_id',
                                        type : 'long'
                                    },
                                    {
                                        name : 'task_type',
                                        type : 'string'
                                    },
                                    {
                                        name : 'task_status',
                                        type : 'string'
                                    },
                                    {
                                        name : 'uid',
                                        type : 'string'
                                    },
                                    {
                                        name : 'slave_id',
                                        type : 'string'
                                    },
                                    {
                                        name : 'info',
                                        type : 'string'
                                    },
                                    {
                                        name : 'create_time',
                                        type : 'string'
                                    },
                                    {
                                        name : 'update_time',
                                        type : 'string'
                                    }

                                ]);
                var store=new Ext.data.Store({
                    proxy : proxy,
                    reader : new Ext.data.ListRangeReader(
                                {
                                    id : 'task_id',
                                    totalProperty : 'totalSize'
                                }, recordType
                             ),
                             remoteSort: false

                });

         
                var task_type_store = new Ext.data.SimpleStore(
                                    {
                                        fields :['name','value'],
                                        data:[
                                            ['仅采集',1],
                                            ['仅抽取',2],
                                            ['既抽取又采集',3]
                                        ]
                                });

                var task_status_store = new Ext.data.SimpleStore(
                                    {
                                        fields :['name','value'],
                                        data:[
                                            ['已开始',1],
                                            ['开始抽取',2],
                                            ['抽取任务已分配',3],
                                            ['抽取任务已受理',4]
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
                        header: '任务ID',
                        dataIndex: 'task_id',
                        width: 100
                    },
                    {
                        header: '任务类型',
                        dataIndex: 'task_type',
                        width: 200,
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
                        header: '任务状态',
                        dataIndex: 'task_status',
                        width: 100,
                        editor: new fm.ComboBox({
                                    store : task_status_store,
                                    triggerAction: 'all',
                                    allowBlank: false,
                                    forceSelection: true,
                                    mode: 'local',
                                    displayField:'name',
                                    valueField:'value'

                                }),
                        renderer: function(value,metadata,record){
                            var index = task_status_store.find('value',value);
                            if(index!=-1){
                                return task_status_store.getAt(index).data.name;
                            }
                            return value;
                        }
                    },
                    {
                        header: 'URL',
                        dataIndex: 'uid',
                        width: 100
                    },
                    {
                        header: '从节点',
                        dataIndex: 'slave_id',
                        width: 100
                    },
                    {
                        header: '任务信息',
                        dataIndex: 'info',
                        width: 100
                    },
                    {
                        header: '创建时间',
                        dataIndex: 'create_time',
                        width: 100
                    },
                    {
                        header: '更新时间',
                        dataIndex: 'update_time',
                        width: 100
                    }
                ]);

                // by default columns are sortable
                cm.defaultSortable = false;

               // trigger the data store load
                store.load({params : {
					start : start,
					limit : pageSize
				},
                            arg : []});

                
                var grid = new Ext.grid.EditorGridPanel({
                    //el:'topic-grid',
                    renderTo: 'gridpanel',
                    width: '100%',
                    height: 530,
                    clicksToEdit:1,
                    autoScroll: true,
                    //title: '分页和排序列表',
                    store: store,
                    trackMouseOver: false,
                    loadMask: true,
                    enableHdMenu: false,
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
                            text: '修改',
                            tooltip: '修改记录',
                            iconCls: 'icon-edit',
                            handler: handleEdit
                        }, '-',
                        {
                            text: '删除',
                            tooltip: '删除记录',
                            iconCls: 'icon-delete',
                            handler: handleDelete
                        }
                    ],
                    bbar: new Ext.PagingToolbar({
                        pageSize: 20,
                        store: store,
                        displayInfo: true
                    })
                });

                // render it
                grid.render();

                
              
                function handleAdd(){

                }


                function handleEdit(){

                  
                }

                //删除数据
                function handleDelete(){

                }

            });
        </script>

    </body>
</html>
