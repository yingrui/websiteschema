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
        <script type="text/javascript" src="dwr/interface/SiteService.js"></script>
        <script type="text/javascript" src="dwr/interface/StartURLService.js"></script>
    </head>

    <body>

        <div id="gridpanel"></div>

        <script type="text/javascript">
            var start = 0;
            var pageSize = 20;
            function getCookie(name)//取cookies函数
            {
                var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
                if(arr != null) return unescape(arr[2]); return null;

            }
            
            Ext.onReady(function(){
                Ext.QuickTips.init();
                var proxy = new Ext.data.DWRProxy(SiteService.getResults, true);
                var recordType = new Ext.data.Record.create(siteRecordType);

                var StartURLRecordType = new Ext.data.Record.create(startURLRecordType);

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
                    params.siteType = Ext.getCmp('SITETYPE').getValue();;
                });

                var site_type_store = new Ext.data.SimpleStore(
                {
                    fields :['name','value'],
                    data:[
                        ['门户','portal'],
                        ['资讯','news'],
                        ['股吧','guba'],
                        ['论坛','bbs'],
                        ['博客','blog'],
                        ['音乐','music'],
                        ['团购','groupon'],
                        ['微博','weibo']
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
                        header: '网站ID',
                        dataIndex: 'siteId',
                        width: 150,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: '网站名称',
                        dataIndex: 'siteName',
                        width: 60,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: '网站域名',
                        dataIndex: 'siteDomain',
                        width: 100,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: '网站类型',
                        dataIndex: 'siteType',
                        width: 50,
                        editor: new fm.ComboBox({
                            store : site_type_store,
                            triggerAction: 'all',
                            allowBlank: false,
                            forceSelection: true,
                            mode: 'local',
                            displayField:'name',
                            valueField:'value'

                        }),
                        renderer: function(value,metadata,record){
                            var index = site_type_store.find('value',value);
                            if(index!=-1){
                                return site_type_store.getAt(index).data.name;
                            }
                            return value;
                        }
                    },
                    {
                        header: '父网站',
                        dataIndex: 'parentId',
                        width: 50,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: 'URL',
                        dataIndex: 'url',
                        width: 200,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: '分析',
                        width: 35,
                        xtype: 'actioncolumn',
                        items: [
                            {
                                icon   : 'resources/icons/fam/accept.gif',  // Use a URL in the icon config
                                tooltip: '<%=analyzerTips%>', //这是analyzer用来触发分析事件的属性，需要特别的记住。
                                handler: function(grid, rowIndex, colIndex) {
                                    var cookie = getCookie("websiteschema");
                                    if("analyzer" != cookie) {
                                        MsgTip.msg("", "您使用的浏览器不是websiteschema analyzer！", true, 5);
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
                        }, '-',
                        {
                            text: '添加起始URL',
                            tooltip: '添加起始URL',
                            iconCls: 'icon-add',
                            handler: handleAddStartURL
                        }, '->',
                        ' ', '类型', ' ',
                        {
                            xtype: 'combo',
                            id: 'SITETYPE',
                            width: 100,
                            valueField: 'value',
                            displayField: 'name',
                            mode: 'local',
                            emptyText: '',
                            allowblank: true,
                            forceSelection: false,
                            store: site_type_store
                        }, ' ',
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

                

                function handleAdd(){
                    var p = new recordType();
                    grid.stopEditing();
                    p.set("siteId","siteId_here");
                    p.set("siteName","siteName_here");
                    p.set("siteDomain","siteDomain_here");
                    p.set("siteType","general");
                    p.set("parentId","0");
                    p.set("url","URL_here");
                    store.insert(0, p);
                    grid.startEditing(0, 0);
                    SiteService.insert(p.data);
                    store.reload();
                }


                function handleEdit(){

                    var mr = store.getModifiedRecords();
                    for(var i=0;i<mr.length;i++){
                        SiteService.update(mr[i].data);
                    }
                    store.reload();
                }

                //删除数据
                function handleDelete(){
                    var selections = grid.selModel.getSelections();
                    Ext.MessageBox.alert("是否要继续删除？");
                    for (var i = 0,len = selections.length; i < len; i++) {
                        SiteService.deleteRecord(selections[i].data);
                    }
                    store.reload();
                }

                function handleAddStartURL(){
                    var selections = grid.selModel.getSelections();

                    for (var i = 0,len = selections.length; i < len; i++) {
                        var site = selections[i];
                        var startURL = new StartURLRecordType();
                        startURL.set("siteId", site.get("siteId"));
                        startURL.set("jobname", site.get("siteId"));
                        startURL.set("status","0");
                        startURL.set("startURL",site.get("url"));
                        StartURLService.insert(startURL.data);
                    }
//                    Ext.MessageBox.alert("添加结束，点击“数据管理->起始URL地址”查看");
                    window.open("/views/metadata/url");
                }

                function handleQuery(){
                    store.reload();
                }
            });
        </script>

    </body>
</html>
