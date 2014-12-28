/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var scheduleTaskRecordType = [
{
    name : 'id',
    type : 'long'
},
{
    name : 'scheduleId',
    type : 'long'
},
{
    name : 'message',
    type : 'string'
},
{
    name : 'status',
    type : 'int'
},
{
    name : 'taskType',
    type : 'int'
},
{
    name : 'createTime',
    type : 'date'
},
{
    name : 'updateTime',
    type : 'date'
}
]

var cipherRecordType = [
{
    name : 'id',
    type : 'long'
},
{
    name : 'siteId',
    type : 'string'
},
{
    name : 'username',
    type : 'string'
},
{
    name : 'password',
    type : 'string'
},
{
    name : 'cookie',
    type : 'string'
},
{
    name : 'header',
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
},
{
    name : 'lastUpdateUser',
    type : 'string'
}
];

var channelRecordType = [
{
    name : 'id',
    type : 'long'
},
{
    name : 'channel',
    type : 'string'
},
{
    name : 'siteId',
    type : 'string'
},
{
    name : 'url',
    type : 'string'
},
{
    name : 'status',
    type : 'int'
},
{
    name : 'parentId',
    type : 'long'
},
{
    name : 'leaf',
    type : 'int'
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
},
{
    name : 'lastUpdateUser',
    type : 'string'
}
];

var urlLinkRecordType = [
{
    name : 'rowKey',
    type : 'string'
},
{
    name : 'content',
    type : 'string'
},
{
    name : 'url',
    type : 'string'
}
];

var urlLogRecordType = [
{
    name : 'rowKey',
    type : 'string'
},
{
    name : 'createTime',
    type : 'long'
}
];

var sysConfRecordType = [
{
    name : 'id',
    type : 'long'
},
{
    name : 'field',
    type : 'string'
},
{
    name : 'name',
    type : 'string'
},
{
    name : 'value',
    type : 'string'
},
{
    name : 'description',
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
},
{
    name : 'lastUpdateUser',
    type : 'string'
}
];

var categoryRecordType = [
{
    name : 'id',
    type : 'long'
},
{
    name : 'name',
    type : 'string'
},
{
    name : 'description',
    type : 'string'
},
{
    name : 'status',
    type : 'int'
},
{
    name : 'parentId',
    type : 'long'
},
{
    name : 'leaf',
    type : 'int'
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
},
{
    name : 'lastUpdateUser',
    type : 'string'
}
];

var scheduleRecordType = [
{
    name : 'id',
    type : 'long'
},
{
    name : 'startURLId',
    type : 'string'
},
{
    name : 'startURL',
    type : 'string'
},
{
    name : 'jobname',
    type : 'string'
},
{
    name : 'channelName',
    type : 'string'
},
{
    name : 'jobId',
    type : 'string'
},
{
    name : 'schedule',
    type : 'string'
},
{
    name:'locationId' ,
    type:'int'
},
{
    name : 'scheduleType',
    type : 'int'
},
{
    name : 'status',
    type : 'int'
},
{
    name : 'createTime',
    type : 'date'
}
];

var startURLRecordType = [
{
    name : 'id',
    type : 'long'
},
{
    name : 'siteId',
    type : 'string'
},
{
    name : 'startURL',
    type : 'string'
},
{
    name : 'jobname',
    type : 'string'
},
{
    name : 'name',
    type : 'string'
},
{
    name : 'status',
    type : 'long'
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
},
{
    name : 'lastUpdateUser',
    type : 'string'
}
];

var jobRecordType =[
{
    name : 'id',
    type : 'long'
},
{
    name : 'jobType',
    type : 'string'
},
{
    name : 'configure',
    type : 'string'
},
{
    name : 'wrapperId',
    type : 'long'
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
},
{
    name : 'lastUpdateUser',
    type : 'string'
}
];

var job_type_store = new Ext.data.SimpleStore(
{
    fields :['name','value'],
    data:[
    ['历史任务整理','websiteschema.schedule.job.JobCheckTask'],
    ['多起始地址采集','websiteschema.schedule.job.JobMultiStartURL'],
    ['基于消息队列的任务','websiteschema.schedule.job.JobAMQPQueueV1']
    //['更新历史新闻的转发和点击信息','websiteschema.schedule.job.RefreshJobAMQPQueueV1']
    ]
});

var wrapperRecordType = [
{
    name : 'id',
    type : 'long'
},
{
    name : 'application',
    type : 'string'
},
{
    name : 'name',
    type : 'string'
},
{
    name : 'wrapperType',
    type : 'string'
},
{
    name : 'visualConfig',
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
},
{
    name : 'lastUpdateUser',
    type : 'string'
}
];

var schedulerRecordType =[
    {
        name : 'id',
        type : 'long'
    },   
    {
        name : 'name',
        type : 'String'
    },  
    {
        name : 'address',
        type : 'String'
    },
    {
        name : 'createTime',
        type : 'date'
    },  
    {
        name : 'createUser',
        type : 'String'
    },
    {
        name : 'updateTime',
        type : 'date'
    },
    {
        name : 'lastUpdateUser',
        type : 'String'
    }    
]

var siteRecordType = [
{
    name : 'id',
    type : 'long'
},
{
    name : 'siteId',
    type : 'string'
},
{
    name : 'siteName',
    type : 'string'
},
{
    name : 'siteDomain',
    type : 'string'
},
{
    name : 'siteType',
    type : 'string'
},
{
    name : 'parentId',
    type : 'long'
},
{
    name : 'url',
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
},
{
    name : 'lastUpdateUser',
    type : 'string'
}
];

var keywordRecordType = [
{
    name : 'id',
    type : 'long'
},
{
    name : 'keywords',
    type : 'string'
},
{
    name : 'referrer',
    type : 'string'
},
{
    name : 'status',
    type : 'int'
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
},
{
    name : 'lastUpdateUser',
    type : 'string'
}
];


var weiboRecordType = [
{
    name : 'id',
    type : 'long'
},
{
    name : 'userId',
    type : 'string'
},
{
    name : 'siteId',
    type : 'string'
},
{
    name : 'passwd',
    type : 'string'
},
{
    name : 'status',
    type : 'int'
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
},
{
    name : 'lastUpdateUser',
    type : 'string'
}
];


var concernedWeiboRecordType = [
{
    name : 'id',
    type : 'long'
},
{
    name : 'name',
    type : 'string'
},
{
    name : 'objectType',
    type : 'int'
},
{
    name : 'title',
    type : 'string'
},
{
    name : 'siteId',
    type : 'string'
},
{
    name : 'weiboURL',
    type : 'string'
},
{
    name : 'org',
    type : 'string'
},
{
    name : 'fans',
    type : 'int'
},
{
    name : 'follow',
    type : 'int'
},
{
    name : 'weibo',
    type : 'int'
},
{
    name : 'notes',
    type : 'string'
},
{
    name : 'certification',
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
},
{
    name : 'lastUpdateUser',
    type : 'string'
}
];

var followRecordType = [
{
    name : 'id',
    type : 'long'
},
{
    name : 'wid',
    type : 'long'
},
{
    name : 'cwid',
    type : 'long'
},
{
    name : 'weibo',
    type : 'string'
},
{
    name : 'concernedWeibo',
    type : 'string'
},
{
    name : 'status',
    type : 'int'
},
{
    name : 'createTime',
    type : 'date'
}
];