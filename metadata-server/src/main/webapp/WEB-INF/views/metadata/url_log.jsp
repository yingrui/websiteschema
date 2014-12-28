<%@page import="websiteschema.utils.DateUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
            String jobname = (String) request.getParameter("jobname");
            jobname = jobname != null ? jobname : "";

            String today = DateUtil.format2(new Date());
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
        <script type="text/javascript" src="dwr/interface/UrlLogService.js"></script>
        <script type="text/javascript" src="js/url_log/ViewUrlLinkPanel.js"></script>
    </head>

    <body>

        <div id="gridpanel"></div>

        <script type="text/javascript">
            var start = 0;
            var pageSize = 20;
            Ext.onReady(function(){
                Ext.QuickTips.init();
                var proxy = new Ext.data.DWRProxy(UrlLogService.getResults, true);
                var recordType = new Ext.data.Record.create(urlLogRecordType);
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
                    params.jobname = Ext.getCmp('JOBNAME').getValue();
                    params.startTime = Ext.getCmp('STARTTIME').getValue();
                    params.sort = 'updateTime desc';
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
                        header: 'RowKey',
                        dataIndex: 'rowKey',
                        width: 400,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: '时间戳',
                        dataIndex: 'createTime',
                        width: 100,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: '查看',
                        width: 60,
                        xtype: 'actioncolumn',
                        items: [
                            {
                                icon   : 'resources/icons/fam/book.png',  // Use a URL in the icon config
                                tooltip: '查看采集到的数据',
                                handler: handleViewUrlLink
                            }
                        ]
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
                    tbar: [{
                            text: '删除',
                            tooltip: '删除记录',
                            iconCls: 'icon-delete',
                            handler: handleDelete
                        }, '->',
                        ' ', '开始时间', ' ',
                        {
                            xtype: 'textfield',
                            id: 'STARTTIME',
                            width: 150,
                            value: '<%=today%>',
                            initEvents : function(){
                                var keyPressed = function(e) {
                                    if(e.getKey()==e.ENTER){
                                        handleQuery();
                                    }
                                };
                                this.el.on("keypress", keyPressed, this);
                            }
                        }, ' ',
                        ' ', 'JOBNAME', ' ',
                        {
                            xtype: 'textfield',
                            id: 'JOBNAME',
                            width: 150,
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
                                Ext.getCmp('JOBNAME').setValue('');
                                Ext.getCmp('STARTTIME').setValue('');
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

                var j = '<%=jobname%>';
                Ext.getCmp('JOBNAME').setValue(j);
                // trigger the data store load
                store.load(
                {
                    params : {
                        start : start,
                        limit : pageSize
                    }
                });


                function handleViewUrlLink(grid, rowIndex, colIndex){
                    
                    var record = grid.getStore().getAt(rowIndex);
                    if(null != record) {
                        UrlLogService.getUrlLink(record.data, {
                            callback:function(urlLink){
                                var viewPanel = new ViewUrlLinkPanel();
                                var data = urlLink;
                                Ext.getCmp('fp_rowKey').setValue(data.rowKey);
                                Ext.getCmp('fp_content').setValue(data.content);
                                Ext.getCmp('fp_url').setValue(data.url);
                                var AddWin = new Ext.Window({
                                    title: '新建记录',
                                    width: 550,
                                    height: 450,
                                    plain: true,
                                    items: viewPanel,
                                    buttons: [{
                                            text: '取消',
                                            handler: function(){
                                                AddWin.close();
                                            }
                                        }]
                                });
                                AddWin.show(this);
                            },
                            errorHandler:function(errorString, exception) {
                                MsgTip.msg("", "任务和调度添加失败: " + errorString, true, 5);
                            }
                        });
                    }
                }

                //删除数据
                function handleDelete(){
                    var selections = grid.selModel.getSelections();
                    for (var i = 0,len = selections.length; i < len; i++) {
                        if(i == len - 1) {
                            UrlLogService.deleteRecord(selections[i].data, function(){
                                store.reload();
                            });
                        } else {
                            UrlLogService.deleteRecord(selections[i].data);
                        }
                    }
                }

                function handleQuery(){
                    store.reload();
                }
            });
        </script>

    </body>
</html>
