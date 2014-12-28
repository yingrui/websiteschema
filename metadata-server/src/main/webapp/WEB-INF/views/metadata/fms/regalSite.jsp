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
        <script type="text/javascript" src="dwr/interface/FMSSiteService.js"></script>
    </head>

    <body>

        <div id="gridpanel"></div>

        <script type="text/javascript">
            var start = 0;
            var pageSize = 20;
            Ext.onReady(function(){
                Ext.QuickTips.init();
                var proxy = new Ext.data.DWRProxy(FMSSiteService.getResults, true);
                var recordType = new Ext.data.Record.create([
                    {
                        name : 'id',
                        type : 'long'
                    },
                    {
                        name : 'name',
                        type : 'string'
                    },
                    {
                        name : 'type_id1',
                        type : 'long'
                    },
                    {
                        name : 'bigKind',
                        type : 'string'
                    },
                    {
                        name : 'type_id2',
                        type : 'long'
                    },
                    {
                        name : 'core',
                        type : 'byte'
                    },
                    {
                        name : 'url',
                        type : 'string'
                    },
                    {
                        name : 'smallKind',
                        type : 'string'
                    }
                ]);

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
                    //                    params.match = Ext.getCmp('MATCH').getValue();
                    params.sort = 'id desc';
                    params.source = Ext.getCmp('SOURCE').getValue();;
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
                        width: 60
                    },
                    {
                        header: '网站名称',
                        dataIndex: 'name',
                        width: 160,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: 'type_id1',
                        dataIndex: 'type_id1',
                        width: 80,
                        hidden:true,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: '大类别',
                        dataIndex: 'bigKind',
                        width: 160,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: 'type_id2',
                        dataIndex: 'type_id2',
                        width: 80,
                        hidden:true,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: 'core',
                        dataIndex: 'core',
                        width: 60,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: '网站URL',
                        dataIndex: 'url',
                        width: 300,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: '添加',
                        width: 40,
                        xtype: 'actioncolumn',
                        items: [
                            {
                                icon   : 'resources/icons/fam/add.gif',  // Use a URL in the icon config
                                tooltip: '添加到网站配置',
                                handler: function(grid, rowIndex) {
                                    var record= grid.getStore().getAt(rowIndex);
                                    if(null != record) {
                                        var data = record.data;
                                        FMSSiteService.addSite(data, function(){
                                            MsgTip.msg("", "添加成功", true, 3);
                                        })
                                    }
                                }
                            }]
                    },
                    {
                        header: '小类别',
                        dataIndex: 'smallKind',
                        width: 160,
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
                    tbar: [  '->',
                        ' ', '网站', ' ',
                        {
                            xtype: 'textfield',
                            id: 'SOURCE',
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
                                Ext.getCmp('SITETYPE').setValue('');
                                Ext.getCmp('MATCH').setValue('');
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
                    params :
                        {
                        start : start,
                        limit : pageSize
                    }
                });

                function handleQuery(){
                    store.reload();
                }
            });
        </script>

    </body>
</html>
