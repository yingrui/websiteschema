<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
            String analyzerTips = (String) request.getAttribute("AnalyzerTips");
%>
<html>
    <head>
        <base href="<%=basePath%>"/>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Websiteschema Site Management</title>

        <link rel="stylesheet" type="text/css" href="resources/css/Ext.ux.form.LovCombo.css">
        <script type="text/javascript" src="js/packages.js"></script>
        <script type="text/javascript" src="js/Ext.ux.form.LovCombo.js"></script>
        <script type="text/javascript" src="js/Ext.ux.ThemeCombo.js"></script>
        <script type="text/javascript" src="js/dwrproxy.js"></script>
        <script type="text/javascript" src="dwr/engine.js"></script>
        <script type="text/javascript" src="dwr/interface/WeiboService.js"></script>
    </head>

    <body>

        <div id="gridpanel"></div>

        <script type="text/javascript">
            var start = 0;
            var pageSize = 20;
            
            Ext.onReady(function(){

                var proxy = new Ext.data.DWRProxy(WeiboService.getResults, true);
                var recordType = new Ext.data.Record.create(weiboRecordType);

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
                //store.setDefaultSort('title', 'desc');
                proxy.on('beforeload', function(thiz, params) {
                    params.match = Ext.getCmp('MATCH').getValue();
                    params.sort = 'updateTime desc';
                });

                var status_store = new Ext.data.SimpleStore(
                {
                    fields :['name','value'],
                    data:[
                        ['无效',0],
                        ['有效',1]
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
                        header: 'id',
                        dataIndex: 'id',
                        width: 30
                    },
                    {
                        header: '用户名',
                        dataIndex: 'userId',
                        width: 300,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: '网站ID',
                        dataIndex: 'siteId',
                        width: 150,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: '密码',
                        dataIndex: 'passwd',
                        width: 150,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: '状态',
                        dataIndex: 'status',
                        width: 50,
                        editor: new fm.ComboBox({
                            store : status_store,
                            triggerAction: 'all',
                            allowBlank: false,
                            forceSelection: true,
                            mode: 'local',
                            displayField:'name',
                            valueField:'value'

                        }),
                        renderer: function(value,metadata,record){
                            var index = status_store.find('value',value);
                            if(index!=-1){
                                return status_store.getAt(index).data.name;
                            }
                            return value;
                        }
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
                        hidden : false,
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
                        ' ', '关键词', ' ',
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
                            iconCls: 'icon-query',
                            handler: handleQuery
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
                    params :
                        {
                        start : start,
                        limit : pageSize
                    }
                });

                

                function handleAdd(){
                    var p = new recordType();
                    grid.stopEditing();
                    p.set("userId","userId_here");
                    p.set("siteId","www_weibo_com_7");
                    p.set("status","0");
                    store.insert(0, p);
                    grid.startEditing(0, 0);
                    WeiboService.insert(p.data);
                    store.reload();
                }


                function handleEdit(){

                    var mr = store.getModifiedRecords();
                    for(var i=0;i<mr.length;i++){
                        WeiboService.update(mr[i].data);
                    }
                    store.reload();
                }

                //删除数据
                function handleDelete(){
                    var selections = grid.selModel.getSelections();
                    Ext.MessageBox.alert("是否要继续删除？");
                    for (var i = 0,len = selections.length; i < len; i++) {
                        WeiboService.deleteRecord(selections[i].data);
                    }
                    store.reload();
                }

                function handleQuery(){
                    store.reload();
                }
            });
        </script>

    </body>
</html>
