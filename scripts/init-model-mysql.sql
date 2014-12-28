/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2011-11-26 23:34:57                          */
/*==============================================================*/

drop table if exists Cipher;

drop table if exists Category;

drop table if exists Channel;

drop table if exists ClusterModel;

drop table if exists ConcernedWeibo;

drop table if exists Follow;

drop table if exists Job;

drop table if exists Keyword;

drop table if exists Location;

drop table if exists RelatedCategory;

drop table if exists Sample;

drop table if exists Schedule;

drop table if exists Site;

drop table if exists StartURL;

drop table if exists SysConf;

drop table if exists UrlLink;

drop table if exists UrlLog;

drop table if exists User;

drop table if exists Websiteschema;

drop table if exists Weibo;

drop table if exists Wrapper;

drop table if exists ScheduleTask;

drop table if exists ScheduleTaskArchive;

/*==============================================================*/
/* Table: Category                                              */
/*==============================================================*/
create table Category
(
   id                   bigint not null auto_increment,
   name                 varchar(300),
   description          varchar(1000),
   parentId             bigint,
   status               int,
   leaf                 int,
   createTime           datetime,
   createUser           varchar(30),
   updateTime           datetime,
   lastUpdateUser       varchar(30),
   primary key (id)
);
insert into Category(name,parentId,status,leaf,createTime,createUser) 
values ('网站类型',0,0,0,now(),'system');

/*==============================================================*/
/* Table: Channel                                               */
/*==============================================================*/
create table Channel
(
   id                   bigint not null auto_increment,
   channel              varchar(300),
   siteId               varchar(100),
   status               int,
   parentId             bigint,
   url                  varchar(200),
   leaf                 int,
   createTime           datetime,
   createUser           varchar(30),
   updateTime           datetime,
   lastUpdateUser       varchar(30),
   primary key (id)
);

insert into Channel (channel, siteId, status, parentId, url, leaf, createTime, createUser) values('(600132)重庆啤酒','guba_eastmoney_com_58','0','0','http://guba.eastmoney.com/topic,600132.html',0,now(),'system');

/*==============================================================*/
/* Table: ClusterModel                                          */
/*==============================================================*/
create table ClusterModel
(
   id                   bigint not null auto_increment,
   rowKey               varchar(100),
   clusters             mediumtext,
   totalSamples         int,
   statInfo             mediumtext,
   clustererType        varchar(100),
   primary key (id),
   unique (rowKey)
);

/*==============================================================*/
/* Table: ConcernedWeibo                                        */
/*==============================================================*/
create table ConcernedWeibo
(
   id                   bigint not null auto_increment,
   name                 varchar(300),
   objectType           int,
   title                varchar(300),
   siteId               varchar(100),
   weiboURL             varchar(1000),
   org                  varchar(300),
   fans                 int,
   follow               int,
   weibo                int,
   notes                varchar(1000),
   certification        varchar(4000),
   createTime           datetime,
   createUser           varchar(30),
   updateTime           datetime,
   lastUpdateUser       varchar(30),
   primary key (id)
);

insert into ConcernedWeibo(name,objectType,title,siteId,weiboURL,org,fans,follow,weibo,notes,certification,createTime,createUser,updateTime,lastUpdateUser) 
values ('yingrui',0,'','www_weibo_com_7','http://weibo.com/yingruif','nyapc','0','0','0','','',now(),'system',now(),'system');

/*==============================================================*/
/* Table Cipher                                                 */
/*==============================================================*/
create table Cipher
(
   id                   bigint not null auto_increment,
   siteId               varchar(100),
   username             varchar(100),
   password             varchar(100),
   cookie               varchar(5000),
   header               varchar(5000),
   createTime           datetime,
   createUser           varchar(30),
   updateTime           datetime,
   lastUpdateUser       varchar(30),
   primary key (id)
);

/*==============================================================*/
/* Table: Follow                                                */
/*==============================================================*/
create table Follow
(
   id                   bigint not null auto_increment,
   wid                  bigint,
   cwid                 bigint,
   status               int,
   createTime           datetime,
   primary key (id)
);

insert into Follow(wid,cwid,status,createTime) 
values (1,1,0,now());

/*==============================================================*/
/* Table: Job                                                   */
/*==============================================================*/
create table Job
(
   id                   bigint not null auto_increment,
   jobType              varchar(100),
   configure            varchar(4000),
   wrapperId            bigint,
   createTime           datetime,
   createUser           varchar(30),
   updateTime           datetime,
   lastUpdateUser       varchar(30),
   primary key (id)
);

insert into Job(jobType, configure, wrapperId, createTime, createUser)
values ('websiteschema.schedule.job.JobCheckTask', '', 0, now(), 'system');
insert into Job(jobType, configure, wrapperId, createTime, createUser)
values ('websiteschema.schedule.job.JobAMQPQueueV1', 'XPATH=html/body/div[@class=\'area clearfix\']/div[@class=\'colLM\']/ul[@class=\'newsList dotted\']/li/span[@class=\'article\']/a\nCLS=0', 3, now(), 'system');

/*==============================================================*/
/* Table: Keyword                                               */
/*==============================================================*/
create table Keyword
(
   id                   bigint not null auto_increment,
   keywords             varchar(300),
   referrer             varchar(300),
   status               int,
   createTime           datetime,
   createUser           varchar(30),
   updateTime           datetime,
   lastUpdateUser       varchar(30),
   primary key (id)
);

/*==============================================================*/
/* Table: Location                                               */
/*==============================================================*/
create table Location
(
   id                   int not null auto_increment,
   name                 varchar(300) not null,
   address              varchar(300) not null,
   createTime           datetime,
   createUser           varchar(30),
   updateTime           datetime,
   lastUpdateUser       varchar(30),
   primary key (id) 
);

insert into Location(name, address, createTime, createUser)
values ('Center','192.168.6.66',now(),'system');
/*=============================================================*/
/* Table: RelatedCategory                                      */
/*=============================================================*/
create table RelatedCategory
(
   id                   bigint not null auto_increment,
   objectId             bigint,
   objectType           int,
   cateId               bigint,
   createTime           datetime,
   createUser           varchar(30),
   primary key (id)
);

/*=============================================================*/
/* Table: Sample                                               */
/*=============================================================*/
create table Sample
(
   id                   bigint not null auto_increment,
   rowKey               varchar(333),
   url                  varchar(300),
   siteId               varchar(100),
   content              mediumtext,
   httpStatus           int,
   lastUpdateTime       datetime,
   createTime           datetime,
   primary key (id)
);

/*=============================================================*/
/* Table: Schedule                                             */
/*=============================================================*/
create table Schedule
(
   id                   bigint not null auto_increment,
   startURLId           bigint,
   jobId                bigint,
   locationId           int,
   schedule             varchar(1000),
   scheduleType         int,
   status               int,
   createTime           datetime,
   primary key (id)
);

insert into Schedule(startURLId, jobId, schedule, locationId ,scheduleType, status, createTime)
values (1,2,'*/10 * * * ?',1,0,0,now());

/*==============================================================*/
/* Table: Site                                                  */
/*==============================================================*/
create table Site
(
   id                   bigint not null auto_increment,
   siteId               varchar(100),
   siteDomain           varchar(100),
   siteName             varchar(100),
   siteType             varchar(30),
   parentId             bigint,
   url                  varchar(120),
   createTime           datetime,
   createUser           varchar(30),
   updateTime           datetime,
   lastUpdateUser       varchar(30),
   primary key (id)
);

insert into Site(siteId,siteDomain,siteName,siteType,parentId,url,createTime,createUser,updateTime,lastUpdateUser) 
values ('www_163_com_1','www.163.com','netease','portal','0','http://www.163.com/',now(),'system',now(),'system');

insert into Site(siteId,siteDomain,siteName,siteType,parentId,url,createTime,createUser,updateTime,lastUpdateUser) 
values ('localhost:8080_2','localhost','tomcat','document','0','http://localhost:8080/',now(),'system',now(),'system');

insert into Site(siteId,siteDomain,siteName,siteType,parentId,url,createTime,createUser,updateTime,lastUpdateUser) 
values ('www_baidu_com_3','www.baidu.com','baidu','search engine','0','http://www.baidu.com/',now(),'system',now(),'system');

insert into Site(siteId,siteDomain,siteName,siteType,parentId,url,createTime,createUser,updateTime,lastUpdateUser) 
values ('baike_baidu_com_4','baike.baidu.com','baidu baike','wiki','3','http://baike.baidu.com/',now(),'system',now(),'system');

insert into Site(siteId,siteDomain,siteName,siteType,parentId,url,createTime,createUser,updateTime,lastUpdateUser) 
values ('www_sina_com_cn_5','www.sina.com.cn','sina','portal','0','http://www.sina.com.cn/',now(),'system',now(),'system');

insert into Site(siteId,siteDomain,siteName,siteType,parentId,url,createTime,createUser,updateTime,lastUpdateUser) 
values ('www_sohu_com_6','www.sohu.com','sohu','portal','0','http://www.sohu.com/',now(),'system',now(),'system');

insert into Site(siteId,siteDomain,siteName,siteType,parentId,url,createTime,createUser,updateTime,lastUpdateUser) 
values ('www_weibo_com_7','www.weibo.com','sina weibo','weibo','5','http://www.weibo.com/',now(),'system',now(),'system');

insert into Site(siteId,siteDomain,siteName,siteType,parentId,url,createTime,createUser,updateTime,lastUpdateUser) 
values ('guba_eastmoney_com_8','guba.eastmoney.com','guba','guba','0','http://guba.eastmoney.com/',now(),'system',now(),'system');

/*==============================================================*/
/* Table: StartURL                                              */
/*==============================================================*/
create table StartURL
(
   id                   bigint not null auto_increment,
   siteId               varchar(100),
   startURL             varchar(8192),
   jobname              varchar(100) not null,
   name                 varchar(100),
   status               int,
   createTime           datetime,
   createUser           varchar(30),
   updateTime           datetime,
   lastUpdateUser       varchar(30),
   primary key (id, jobname)
);

insert into StartURL(siteId, startURL, jobname, name, status, createTime, createUser)
values ('www_163_com_1','http://money.163.com/special/00252G50/macroNew.html','money_163_com_1','网易财经',1,now(),'system');

/*==============================================================*/
/* Table: SysConf                                               */
/*==============================================================*/
create table SysConf
(
   id                   bigint not null auto_increment,
   field                varchar(100),
   name                 varchar(100),
   value                varchar(100),
   description          varchar(1000),
   createTime           datetime,
   createUser           varchar(30),
   updateTime           datetime,
   lastUpdateUser       varchar(30),
   primary key (id)
);

insert into SysConf(field, name, value, description, createTime, createUser)
values ('URLQueue','ServerHost','localhost','Rabbitmq Server for URL Queue',now(),'system');
insert into SysConf(field, name, value, description, createTime, createUser)
values ('URLQueue','PriorQueueName','url_queue','URL Queue Name',now(),'system');
insert into SysConf(field, name, value, description, createTime, createUser)
values ('URLQueue','QueueName','url_queue_1','URL Queue Name',now(),'system');
insert into SysConf(field, name, value, description, createTime, createUser)
values ('default','VirtualDevices','["localhost:12207"]','Crawlers',now(),'system');

/*==============================================================*/
/* Table: UrlLink                                               */
/*==============================================================*/
create table UrlLink
(
   id                   bigint not null auto_increment,
   rowKey               varchar(333),
   content              mediumtext,
   status               int,
   linkType             int,
   url                  varchar(333),
   lastUpdateTime       datetime,
   createTime           datetime,
   parent               varchar(333),
   depth                int,
   httpStatus           int,
   jobname              varchar(100),
   primary key (id)
);

/*==============================================================*/
/* Table: UrlLog                                                */
/*==============================================================*/
create table UrlLog
(
   id                   bigint not null auto_increment,
   rowKey               varchar(333),
   createTime           bigint,
   jobname              varchar(100),
   primary key (id)
);

/*==============================================================*/
/* Table: User                                                  */
/*==============================================================*/
create table User
(
   ID                   bigint not null auto_increment,
   USER_ID              varchar(30),
   NAME                 varchar(30),
   PASSWD               char(32),
   EMAIL                varchar(30),
   ROLE                 varchar(100),
   primary key (ID)
);


insert into User(user_id,name,passwd,email,role) values ('admin','admin','21232f297a57a5a743894a0e4a801fc3','yingrui.f@gmail.com','ROLE_ADMIN, ROLE_USER, ROLE_CRAWLER');
insert into User(user_id,name,passwd,email,role) values ('system','system','21232f297a57a5a743894a0e4a801fc3','yingrui.f@gmail.com','ROLE_ADMIN, ROLE_USER, ROLE_CRAWLER');
insert into User(user_id,name,passwd,email,role) values ('yingrui','yingrui','21232f297a57a5a743894a0e4a801fc3','yingrui.f@gmail.com','ROLE_ADMIN, ROLE_USER, ROLE_CRAWLER');

/*==============================================================*/
/* Table: Websiteschema                                         */
/*==============================================================*/
create table Websiteschema
(
   id                   bigint not null auto_increment,
   rowKey               varchar(100),
   valid                varchar(5),
   dimension            mediumtext,
   xpathAttr            text,
   crawlerSettings      text,
   properties           mediumtext,
   status               int,
   createTime           datetime,
   lastUpdateTime       datetime,
   primary key (id),
   unique (rowKey)
);

/*==============================================================*/
/* Table: Weibo                                                 */
/*==============================================================*/
create table Weibo
(
   id                   bigint not null auto_increment,
   userId               varchar(30),
   siteId               varchar(100),
   passwd               varchar(30),
   status               int,
   createTime           datetime,
   createUser           varchar(30),
   updateTime           datetime,
   lastUpdateUser       varchar(30),
   primary key (id)
);

insert into Weibo(userId,siteId,passwd,status,createTime,createUser,updateTime,lastUpdateUser) 
values ('websiteschema@gmail.com','www_weibo_com_7','websiteschema','0',now(),'system',now(),'system');

/*==============================================================*/
/* Table: Wrapper                                               */
/*==============================================================*/
create table Wrapper
(
   id                   bigint not null auto_increment,
   application          text,
   name                 varchar(100) not null,
   wrapperType          varchar(30),
   visualConfig         text,
   checksum             char(32),
   createTime           datetime,
   createUser           varchar(30),
   updateTime           datetime,
   lastUpdateUser       varchar(30),
   primary key (id, name)
);

/*==============================================================*/
/* Table: ScheduleTask                                          */
/*==============================================================*/
create table ScheduleTask
(
   id                   bigint not null auto_increment,
   scheId               bigint,
   status               tinyint,
   taskType             tinyint,
   message              varchar(1000),
   createTime           datetime,
   updateTime           datetime,
   primary key (id)
);

/*==============================================================*/
/* Table: ScheduleTaskArchive                                   */
/*==============================================================*/
create table ScheduleTaskArchive
(
   id                   bigint not null,
   scheId               bigint,
   status               tinyint,
   taskType             tinyint,
   message              varchar(1000),
   createTime           datetime,
   updateTime           datetime,
   primary key (id)
);
