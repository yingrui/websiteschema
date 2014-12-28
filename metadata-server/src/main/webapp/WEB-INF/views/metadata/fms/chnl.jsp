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
        <script type="text/javascript" src="dwr/interface/FMSJobService.js"></script>
    </head>

    <body>

        <div id="gridpanel"></div>

        <script type="text/javascript">
            var start = 0;
            var pageSize = 20;
            Ext.onReady(function(){
                Ext.QuickTips.init();
                var proxy = new Ext.data.DWRProxy(FMSJobService.getResults, true);
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
                        name : 'jobname',
                        type : 'string'
                    },
                    {
                        name : 'url',
                        type : 'string'
                    },
                    {
                        name : 'fetchType',
                        type : 'int'
                    },
                    {
                        name : 'status',
                        type : 'int'
                    },
                    {
                        name : 'sourceId',
                        type : 'long'
                    },
                    {
                        name : 'site_url',
                        type : 'string'
                    },
                    {
                        name : 'source',
                        type : 'string'
                    },
                    {
                        name : 'createTime',
                        type : 'date'
                    },
                    {
                        name : 'createUser',
                        type : 'string'
                    },
                    {
                        name : 'updateTime',
                        type : 'date'
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
                //store.setDefaultSort('title', 'desc');
                proxy.on('beforeload', function(thiz, params) {
                    params.match = Ext.getCmp('MATCH').getValue();
                    params.sort = 'id desc';
                    params.source = Ext.getCmp('SOURCE').getValue();;
                });

                var fetch_type_store = new Ext.data.SimpleStore(
                {
                    fields :['name','value'],
                    data:[
                        ['HTTPFETCH爬虫','1'],
                        ['非核心信源爬虫','2'],
                        ['数据库爬虫','3'],
                        ['BLOG爬虫','4'],
                        ['E-PAPER爬虫','5'],
                        ['想象力爬虫','6'],
                        ['火车头爬虫','7']
                    ]
                });

                var job_status_store = new Ext.data.SimpleStore(
                {
                    fields :['name','value'],
                    data:[
                        ['录入未审核','1'],
                        ['审核未部署','2'],
                        ['已配置','3'],
                        ['错误待修改','4'],
                        ['审核保留','5'],
                        ['被删除','6']
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
                        width: 60
                    },
                    {
                        header: '栏目名称',
                        dataIndex: 'name',
                        width: 100,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: 'JOBNAME',
                        dataIndex: 'jobname',
                        width: 150,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: '网站名称',
                        dataIndex: 'source',
                        width: 50,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: '栏目URL',
                        dataIndex: 'url',
                        width: 200,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: '爬虫类型',
                        dataIndex: 'fetchType',
                        width: 100,
                        editor: new fm.ComboBox({
                            store : fetch_type_store,
                            triggerAction: 'all',
                            allowBlank: false,
                            forceSelection: true,
                            mode: 'local',
                            displayField:'name',
                            valueField:'value'

                        }),
                        renderer: function(value,metadata,record){
                            var index = fetch_type_store.find('value',value);
                            if(index!=-1){
                                return fetch_type_store.getAt(index).data.name;
                            }
                            return value;
                        }
                    },
                    {
                        header: '栏目状态',
                        dataIndex: 'status',
                        width: 80,
                        editor: new fm.ComboBox({
                            store : job_status_store,
                            triggerAction: 'all',
                            allowBlank: false,
                            forceSelection: true,
                            mode: 'local',
                            displayField:'name',
                            valueField:'value'

                        }),
                        renderer: function(value,metadata,record){
                            var index = job_status_store.find('value',value);
                            if(index!=-1){
                                return job_status_store.getAt(index).data.name;
                            }
                            return value;
                        }
                    },
                    {
                        header: '网站ID',
                        dataIndex: 'sourceId',
                        width: 50,
                        hidden:true,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: '网站URL',
                        dataIndex: 'site_url',
                        width: 50,
                        hidden:true,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: '添加',
                        width: 35,
                        xtype: 'actioncolumn',
                        items: [
                            {
                                icon   : 'resources/icons/fam/add.gif',  // Use a URL in the icon config
                                tooltip: '添加此栏目至起始URL',
                                handler: function(grid, rowIndex) {
                                    var record= grid.getStore().getAt(rowIndex);
                                    if(null != record) {
                                        var data = record.data;
                                        FMSJobService.addStartURL(data, function(){
                                            MsgTip.msg("", "添加成功", true, 3);
                                        })
                                    }
                                }
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
                            text: '批量添加',
                            tooltip: '批量添加',
                            iconCls: 'icon-add',
                            handler: handleAddURLs
                        }, '-',
                        '->', ' ',
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
                        ' ', 'JOBNAME', ' ',
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

            // 批量添加url
            function handleAddURLs(){
                var selections = grid.selModel.getSelections();
                for (var i = 0,len = selections.length; i < len; i++) {
                    if(i == len - 1) {
                        FMSJobService.addStartURL(selections[i].data, function(){
                            store.reload();
                            MsgTip.msg("", "添加成功", true, 3);
                        });
                    } else {
                        FMSJobService.addStartURL(selections[i].data);
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
