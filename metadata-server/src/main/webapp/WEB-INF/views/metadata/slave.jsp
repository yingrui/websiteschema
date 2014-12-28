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

        <link rel="stylesheet" type="text/css" href="/resources/css/Ext.ux.form.LovCombo.css">
        <style type="text/css"></style>
        <script type="text/javascript" src="/js/packages.js"></script>
        <script type="text/javascript" src="/js/Ext.ux.form.LovCombo.js"></script>
        <script type="text/javascript" src="/js/Ext.ux.ThemeCombo.js"></script>
        <script type="text/javascript" src="/js/dwrproxy.js"></script>
        <script type="text/javascript" src="/dwr/engine.js"></script>
        <script type="text/javascript" src="/dwr/interface/SlaveService.js"></script>
    </head>

    <body>

        <div id="gridpanel"></div>

        <script type="text/javascript">
            var start = 0;
            var pageSize = 10;
            Ext.onReady(function(){

                var proxy = new Ext.data.DWRProxy(SlaveService.getSlaves, true);

                var recordType = new Ext.data.Record.create([
                                    {
                                        name : 'slave_id',
                                        type : 'long'
                                    },

                                    {
                                        name : 'slave_name',
                                        type : 'string'
                                    },
                                    {
                                        name : 'slave_status',
                                        type : 'string'
                                    },
                                    {
                                        name : 'slave_type',
                                        type : 'string'
                                    },
                                    {
                                        name : 'slave_ip',
                                        type : 'string'
                                    },
                                    {
                                        name : 'create_time',
                                        type : 'string'
                                    }
                                ]);
                var store=new Ext.data.Store({
                    proxy : proxy,
                    reader : new Ext.data.ListRangeReader(
                                {
                                    id : 'chl_id',
                                    totalProperty : 'totalSize'
                                }, recordType
                             )

                });

                //alert(store);


                var slave_status_store = new Ext.data.SimpleStore(
                                    {
                                        fields :['name','value'],
                                        data:[
                                            ['在线',1],
                                            ['离线',2]
                                        ]
                                });
                var slave_type_store = new Ext.data.SimpleStore(
                                    {
                                        fields :['name','value'],
                                        data:[
                                            ['CRAWLER',1],
                                            ['WRAPPER',2]
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
                        header: '从节点ID',
                        dataIndex: 'slave_id',
                        width: 100
                    },
                    {
                        header: '从节点名称',
                        dataIndex: 'slave_name',
                        width: 200
                    },
                    {
                        header: '从节点状态',
                        dataIndex: 'slave_status',
                        width: 100,
                        editor: new fm.ComboBox({
                                    store : slave_status_store,
                                    triggerAction: 'all',
                                    allowBlank: false,
                                    forceSelection: true,
                                    mode: 'local',
                                    displayField:'name',
                                    valueField:'value'

                                }),
                        renderer: function(value,metadata,record){
                            var index = slave_status_store.find('value',value);
                            if(index!=-1){
                                return slave_status_store.getAt(index).data.name;
                            }
                            return value;
                        }
                    },
                    {
                        header: '从节点类型',
                        dataIndex: 'slave_type',
                        width: 100,
                        editor: new fm.ComboBox({
                                    store : slave_type_store,
                                    triggerAction: 'all',
                                    allowBlank: false,
                                    forceSelection: true,
                                    mode: 'local',
                                    displayField:'name',
                                    valueField:'value'

                                }),
                        renderer: function(value,metadata,record){
                            var index = slave_type_store.find('value',value);
                            if(index!=-1){
                                return slave_type_store.getAt(index).data.name;
                            }
                            return value;
                        }
                    },
                    {
                        header: '从节点IP',
                        dataIndex: 'slave_ip',
                        width: 100
                    },
                    {
                        header: '创建时间',
                        dataIndex: 'create_time',
                        width: 100
                    }
                ]);

                // by default columns are sortable
                cm.defaultSortable = false;

               // trigger the data store load
                store.load({params : {
					start : start,
					limit : pageSize
				}});


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
                    var p = new recordType();
                    grid.stopEditing();
                    store.insert(0, p);
                    grid.startEditing(0, 0);
                    URLService.insertURL(p);
                }


                function handleEdit(){
                    var mr = store.getModifiedRecords();                   
                    for(var i=0;i<mr.length;i++){
                        Ext.MessageBox.alert("是否要更改" + mr[i].data["url"]+ "的配置");


                        URLService.updateURL(mr[i].data);
                    }
                    
                }

                //删除数据
                function handleDelete(){
                    var selections = grid.selModel.getSelections();                   
                    for (var i = 0,len = selections.length; i < len; i++) {
                        alert(selections[i].id);
                    }
                }

            });
        </script>

    </body>
</html>
