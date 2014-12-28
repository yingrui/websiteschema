Ext.BLANK_IMAGE_URL = 'resources/s.gif';

LeftTreePanel = function() {
    LeftTreePanel.superclass.constructor.call(this, {
        id:'main-tree-panel',
        region:'west',
        split:true,
        header: false,
        width: 200,
        collapsible: true,
        margins:'0 0 0 0',
        rootVisible:true,
        lines:false,
        autoScroll:true,
        animCollapse:false,
        animate: false,
        collapseMode:'mini',
        //        frame: true,
        useArrows: true,
        enableDD: true,
        containerScroll: true,
        border: false,

        loader: new Ext.tree.TreeLoader({
            clearOnLoad: false,
            url: 'rest/category/'
        }),

        root: new Ext.tree.AsyncTreeNode({
            text:'所有分类',
            id:'0',
            draggable: false,
            expanded:true
        })
    //        collapseFirst:false
    });
    this.getSelectionModel().on('beforeselect', function(sm, node){
        return !Ext.isEmpty(node.attributes.href);
    });
};

Ext.extend(LeftTreePanel, Ext.tree.TreePanel, {
    initComponent: function(){
        Ext.apply(this, {
            tbar:[ ' ',
            //'系统导航',' ',' ',' ',' ',
            {
                iconCls: 'icon-expand-all',
                tooltip: '展开全部',
                handler: function(){
                    this.root.expand(true);
                },
                scope: this
            }, '-', {
                iconCls: 'icon-collapse-all',
                tooltip: '关闭全部',
                handler: function(){
                    this.root.collapse(true);
                },
                scope: this
            }, '-', {
                text: '刷新',
                handler: function(){
                    this.root.reload();
                },
                scope: this
            }]
        })
        LeftTreePanel.superclass.initComponent.call(this);
    },
    autoSelectPath : function(cls){
        if(cls){
            if(cls.indexOf("tab-")==0)
                cls = cls.substring(4);
            var parts = cls.split('.');
            var res = [];
            var pkg = [];
            for(var i = 0; i < parts.length-1; i++){
                pkg.push(parts[i]);
                res.push(pkg.join('.'));
            }
            res.push(cls);
            this.selectPath('/0/'+res.join('/'));
        }
    }
});

//定义为全局变量便于其他页面操作
var leftPanel;
var mainPanel;
var selectedNode;
var recordType = new Ext.data.Record.create(categoryRecordType);
Ext.onReady(function(){

    Ext.QuickTips.init();
    leftPanel = new LeftTreePanel();
    
    mainPanel = new Ext.FormPanel({
        border:false,
        bodyStyle:'padding:5px 5px 0',
        width: 350,

        items: [{
            xtype:'fieldset',
            title: '分类信息',
            autoHeight:true,
            defaults: {
                width: 210
            },
            defaultType: 'textfield',
            collapsible: true,
            items :[{
                id:'f_id',
                fieldLabel: '分类ID',
                disabled: true
            },{
                id:'f_Name',
                fieldLabel: '分类名称',
                allowBlank:false
            },{
                id:'f_Desc',
                fieldLabel: '分类描述',
                xtype : 'textarea'
            },{
                id:'f_parentId',
                fieldLabel: '父ID',
                disabled: true,
                hidden: true
            },{
                id:'f_leaf',
                fieldLabel: '叶子节点',
                disabled: true,
                hidden: true
            },{
                id:'f_createUser',
                fieldLabel: '创建者',
                disabled: true
            },{
                id:'f_createTime',
                fieldLabel: '创建时间',
                disabled: true
            },{
                id:'f_lastUpdateUser',
                fieldLabel: '更新者',
                disabled: true
            },{
                id:'f_updateTime',
                fieldLabel: '更新时间',
                disabled: true
            }
            ]
        }],
    
        buttons: [{
            text: '保存',
            handler: function() {
                var record = new recordType();
                var data = record.data;
                data.id = Ext.getCmp('f_id').getValue();
                data.name = Ext.getCmp('f_Name').getValue();
                data.description = Ext.getCmp('f_Desc').getValue();
                data.parentId = Ext.getCmp('f_parentId').getValue();
                data.leaf = Ext.getCmp('f_leaf').getValue();
                CategoryService.update(data,{
                    callback:function(){
                        MsgTip.msg("", "成功更新信息", true, 2);
                        refreshPanel(data.id)
                    },
                    errorHandler:function(errorString, exception) {
                        MsgTip.msg("", "更新信息失败: " + errorString, true, 2);
                    }
                });
            }
        }]
    });

    mainPanel.render("category-panel");

    leftPanel.on('click', function(node, e){
        //修正非叶子节点无法点击
        if(!Ext.isEmpty(node.attributes.id)){
            e.stopEvent();
            refreshPanel(node.id);
        }
    });

    function refreshPanel(id){
        if(id > 0) {
            CategoryService.getById(id, {
                callback: function(data) {
                    Ext.getCmp('f_id').setValue(data.id);
                    Ext.getCmp('f_Name').setValue(data.name);
                    Ext.getCmp('f_Desc').setValue(data.description);
                    Ext.getCmp('f_parentId').setValue(data.parentId);
                    Ext.getCmp('f_leaf').setValue(data.leaf);
                    Ext.getCmp('f_createUser').setValue(data.createUser);
                    Ext.getCmp('f_createTime').setValue(data.createTime);
                    Ext.getCmp('f_lastUpdateUser').setValue(data.lastUpdateUser);
                    Ext.getCmp('f_updateTime').setValue(data.updateTime);
                },
                errorHandler:function(errorString, exception) {
                    MsgTip.msg("", "添加新网站类型失败: " + errorString, true, 2);
                }
            });
        }
    }

    //异步加载子节点时，用来动态的生成url。
    leftPanel.on('beforeload', function(node) {
        leftPanel.loader.dataUrl = 'rest/category/' + node.id; // 定义子节点的Loader
    });

    //声明一个弹出框
    var rightClick = new Ext.menu.Menu({
        id : 'rightClickCont',
        items : [{
            id : 'rMenu1',
            text : '添加分类',
            // 增加菜单点击事件
            handler : handleAdd
        }, {
            id : 'rMenu3',
            text : '删除分类',
            handler : handleDelete
        }]

    });

    //在右键点击的时候，显示弹出框
    leftPanel.on('contextmenu', function(node, event) {// 声明菜单类型
        event.preventDefault();// 阻止浏览器默认右键菜单显示
        selectedNode = node;
        rightClick.showAt(event.getXY());// 取得鼠标点击坐标，展示菜单
    });

    //在页面上显示
    new Ext.Viewport({
        layout: 'border',
        items:[ leftPanel, {
            title: '编辑分类',
            region: 'center',
            contentEl: 'category-panel',
            split: true,
            border: true,
            collapsible: true
        }]
    });

    //事件处理函数
    function handleAdd() {
        //获取选中的节点。
        var parentId = selectedNode.id;
        var windowTitle = '添加新站点';
        var record = new recordType();
        if(null != record) {
            var addPanel = new CategoryAddFormPanel();
            Ext.getCmp('fp_parentId').setValue(parentId);
            var AddWin = new Ext.Window({
                title: windowTitle,
                width: 380,
                autoHeight: true,
                plain: true,
                items: addPanel,
                buttons: [{
                    text: '确定',
                    handler: function(){
                        if(addPanel.form.isValid()) {
                            var data = record.data;
                            data.parentId = parentId;
                            data.name = addPanel.getComponent('fp_Name').getValue();
                            data.description = addPanel.getComponent('fp_Desc').getValue();
                            CategoryService.insert(data,{
                                callback:function(){
                                    MsgTip.msg("", "成功添加新网站类型", true, 2);
                                    AddWin.close();
                                    if(!selectedNode.leaf) {
                                        selectedNode.reload();
                                    } else {
                                        selectedNode.parentNode.reload();
                                    }
                                },
                                errorHandler:function(errorString, exception) {
                                    MsgTip.msg("", "添加新网站类型失败: " + errorString, true, 2);
                                }
                            });
                        //增加树节点
                        }
                    }
                }, {
                    text: '取消',
                    handler: function(){
                        MsgTip.msg("", "您取消了添加新网站类型", true, 2);
                        AddWin.close();
                    }
                }]
            });
            AddWin.show(this);
        }


    }

    function handleDelete() {
        var nodes = leftPanel.getChecked();
        if(!Ext.isEmpty(nodes)) {
            for (var i = 0,len = nodes.length; i < len; i++){
                var node = nodes[i];
                CategoryService.deleteById(node.id,{
                    callback:function(){
                        MsgTip.msg("", "成功删除站点", true, 2);
                        //删除树节点
                        var parent = node.parentNode;
                        node.remove();
                        if(!Ext.isEmpty(parent)){
                            parent.reload();
                        }
                    },
                    errorHandler:function(errorString, exception) {
                        MsgTip.msg("", "删除站点失败: " + errorString, true, 2);
                    }
                });
            }
        }
    }
});