Ext.BLANK_IMAGE_URL = 'resources/s.gif';

Console = {};

Console.actionData = [
       {
           text:"聚宝盆",id:"dh",title:"聚宝盆",cls:"cls",singleClickExpand:true,
           children:[
                        {
                            text:"数据管理",id:"dh-fetch",title:"数据管理",cls:"cls",singleClickExpand:true,
                            children:[
                                {href:"views/metadata/site",text:"网站配置",id:"site",title:"网站配置",cls:"cls",iconCls:"icon-cls",leaf:true},
                                {href:"views/metadata/cipher",text:"网站登录管理",id:"cipher",title:"网站登录管理",cls:"cls",iconCls:"icon-cls",leaf:true},
                                {href:"views/metadata/category",text:"分类管理",id:"category",title:"分类管理",cls:"cls",iconCls:"icon-cls",leaf:true},
                                {href:"views/metadata/channel",text:"网站栏目",id:"channel",title:"网站栏目",cls:"cls",iconCls:"icon-cls",leaf:true},
                                {href:"views/metadata/keyword",text:"关键词管理",id:"keyword",title:"关键词管理",cls:"cls",iconCls:"icon-cls",leaf:true},
                                {
                                    text:"宏爵FMS",id:"fms-admin",title:"宏爵FMS",cls:"cls",singleClickExpand:true,
                                    children:[
                                        {href:"views/metadata/fms/regalSite",text:"站点浏览",id:"siteview",title:"站点浏览",cls:"cls",iconCls:"icon-cls",leaf:true},
                                        {href:"views/metadata/fms/chnl",text:"栏目浏览",id:"chnlview",title:"栏目浏览",cls:"cls",iconCls:"icon-cls",leaf:true}
                                    ]
                                }
                            ]
                        },
                        {
                            text:"采集设置",id:"extractor-admin",title:"采集设置",cls:"cls",singleClickExpand:true,
                            children:[
                                {href:"views/metadata/url",text:"起始URL地址",id:"url",title:"URL地址配置",cls:"cls",iconCls:"icon-cls",leaf:true},
                                {href:"views/metadata/schedule",text:"调度计划",id:"schedule",title:"调度计划",cls:"cls",iconCls:"icon-cls",leaf:true},
                                {href:"views/metadata/job",text:"任务配置",id:"job",title:"任务配置",cls:"cls",iconCls:"icon-cls",leaf:true},
                                {href:"views/metadata/wrapper",text:"抽取器",id:"wrapper",title:"抽取器",cls:"cls",iconCls:"icon-cls",leaf:true},
                                {href:"views/metadata/scheduler",text:"调度器",id:"scheduler",title:"调度器",cls:"cls",iconCls:"icon-cls",leaf:true}
                            ]
                        },
                        { 
                            text:"社交网络",id:"sn-admin",title:"社交网络",cls:"cls",singleClickExpand:true,
                            children:[
                                {
                                    text:"微博",id:"weibo-admin",title:"微博",cls:"cls",singleClickExpand:true,
                                    children:[
                                        {href:"views/metadata/weibo/myWeibo",text:"我的微博",id:"weibo",title:"我的微博",cls:"cls",iconCls:"icon-cls",leaf:true},
                                        {href:"views/metadata/weibo/concernedWeibo",text:"微博主",id:"concernedWeibo",title:"微博主",cls:"cls",iconCls:"icon-cls",leaf:true},
                                        {href:"views/metadata/weibo/follow",text:"加关注",id:"follow",title:"加关注",cls:"cls",iconCls:"icon-cls",leaf:true}
                                    ]
                                }
                            ]
                        },
                        {
                            text:"用户管理",id:"user-admin.task",title:"用户管理",cls:"cls",singleClickExpand:true,
                            children:[
                                {href:"views/metadata/user",text:"用户管理",id:"yhgl",title:"用户管理",cls:"cls",iconCls:"icon-cls",leaf:true}
                            ]
                        },
                        {
                            href:"views/personInfo", text:"个人信息",id:"personal-admin.task",title:"个人信息",cls:"cls",leaf:true
                        },
                        {
                            href:"views/metadata/sysconf",text:"系统配置",id:"sysconf",title:"系统配置",cls:"cls",leaf:true
                        }
                    ]
       }
];

LeftPanel = function() {
    LeftPanel.superclass.constructor.call(this, {
        id:'main-tree',
        region:'west',
        split:true,
        header: false,
        width: 200,
        minSize: 175,
        maxSize: 500,
        collapsible: true,
        margins:'0 0 5 5',
        cmargins:'0 0 0 0',
        rootVisible:false,
        lines:false,
        autoScroll:true,
        animCollapse:false,
        animate: false,
        collapseMode:'mini',

        useArrows: true,
        enableDD: true,
        containerScroll: true,
        border: false,

        loader: new Ext.tree.TreeLoader({
        preloadChildren: true,
            clearOnLoad: false
        }),
        root: new Ext.tree.AsyncTreeNode({
            text:'Ext JS',
            id:'root',
            expanded:true,
            draggable: false,
            children:Console.actionData
         }),
/*
        listeners: {
            click: function(n) {
                Ext.Msg.alert('Navigation Tree Click', 'You clicked: "' + n.getPath() + '"');
            }
        },
*/
        collapseFirst:false
    });
    // no longer needed!
    //new Ext.tree.TreeSorter(this, {folderSort:true,leafAttr:'isClass'});

    this.getSelectionModel().on('beforeselect', function(sm, node){
        return !Ext.isEmpty(node.attributes.href);//node.isLeaf();
    });
};

Ext.extend(LeftPanel, Ext.tree.TreePanel, {
    initComponent: function(){
        Ext.apply(this, {
            tbar:[ ' ',
            //'系统导航',' ',' ',' ',' ',
            {
                iconCls: 'icon-expand-all',
                tooltip: '展开全部',
                handler: function(){ this.root.expand(true); },
                scope: this
            }, '-', {
                iconCls: 'icon-collapse-all',
                tooltip: '关闭全部',
                handler: function(){ this.root.collapse(true); },
                scope: this
            }]
        })
        LeftPanel.superclass.initComponent.call(this);
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
            this.selectPath('/root/'+res.join('/'));
        }
    }
});

MainPanel = function(){

    MainPanel.superclass.constructor.call(this, {
        id:'main-body',
        region:'center',
        margins:'0 5 5 0',
        resizeTabs: true,
        minTabWidth: 135,
        tabWidth: 135,
        enableTabScroll: true,
        activeTab: 0,

        items: {
            id: 'tab-welcome',
            title: 'Websiteschema',
            cls: 'cls',
            autoLoad: {url: '/docs/introduce.html', scope: this, script: true},
            iconCls: 'icon-docs',
            closable: false,
            autoScroll: true
        }
    });
};

Ext.extend(MainPanel, Ext.TabPanel, {

    initEvents : function(){
        MainPanel.superclass.initEvents.call(this);
        this.body.on('click', this.onClick, this);
    },

    onClick: function(e, target){

    },

    loadTabPage : function(href, id, title, cls, iconCls){
        var tabid = 'tab-' + id;
        var tab = this.getComponent(tabid);
        if(tab){
            this.setActiveTab(tab);
        }else{
            var p = this.add({
                closable: true,
                autoScroll: true,
                id: tabid,
                title: title,
                autoLoad: {url: href, scripts: true},
                cls: cls,
                iconCls: iconCls
            });
            this.setActiveTab(p);
        }
    },

    loadTabIframePage : function(href, id, title, cls, iconCls){
        var tabid = 'tab-' + id;
        var tab = this.getComponent(tabid);
        if(tab){
            this.setActiveTab(tab);
        }else{
            var p = this.add({
                closable: true,
                autoScroll: true,
                id: tabid,
                title: title,
                html: '<iframe id="'+id+'-iframe" src="'+href+'" frameborder="0" scrolling="auto" style="border:0px none; height:100%; width:100%;"></iframe>',
                cls : cls,
                iconCls: iconCls
            });
            this.setActiveTab(p);
        }
    }
});

//定义为全局变量便于其他页面操作
var leftPanel;
var mainPanel;

Ext.onReady(function(){

    Ext.QuickTips.init();

    leftPanel = new LeftPanel();
    mainPanel = new MainPanel();

    leftPanel.on('click', function(node, e){
         //if(node.isLeaf()){
         //修正非叶子节点无法点击
         if(!Ext.isEmpty(node.attributes.href)){
            e.stopEvent();
            mainPanel.loadTabIframePage(node.attributes.href, node.id, node.attributes.title, node.attributes.cls, node.attributes.iconCls);
         }
    });

    mainPanel.on('tabchange', function(tp, tab){
        leftPanel.autoSelectPath(tab.id);
    });

    var viewport = new Ext.Viewport({
        layout: 'border',
        items:[ {
            cls: 'top',
//            height: 38,
            region:'north',
            xtype:'box',
            el:'top',
            border:false,
            margins: '0 0 0 0'
        }, leftPanel, mainPanel, {
            //cls: 'bottom',
            height: 31,
            region:'south',
            xtype:'box',
            el:'bottom',
            border:false,
            margins: '0 0 0 0'
        }]
    });

    leftPanel.expandPath('/root/dh');

    viewport.doLayout();

    setTimeout(function(){
        Ext.get('loading').remove();
        Ext.get('loading-mask').fadeOut({remove:true});
    }, 250);
});

Ext.Ajax.on('requestcomplete', function(ajax, xhr, o){
    if(typeof urchinTracker == 'function' && o && o.url){
        urchinTracker(o.url);
    }
});
