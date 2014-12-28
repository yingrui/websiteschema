namespace=fb.crawler

StartFB=启动
InitEvent=COLD

[Bean]
SpringBeans=spring-beans.xml

[启动]
FBType=websiteschema.fb.E_RESTART
EO.COLD={"Websiteschema工厂类":"EI"}

[Websiteschema工厂类]
FBType=websiteschema.crawler.fb.FBWebsiteschema
EO.EO={"采集器":"FETCH"}
EO.FAIL={"退出":"EI1"}
DO.OUT={"采集器":"SCHEMA","抽取器":"SCHEMA"}
DI.SITE=${SITEID}

[采集器]
FBType=websiteschema.crawler.fb.FBWebCrawler
EO.SUC={"抽取器":"EI"}
EO.FAL={"退出":"EI2"}
DO.DOC={"抽取器":"DOC"}
DO.URL={"抽取器":"URL"}
DI.USERAGENT=${USERAGENT}
DI.CRAWLER=${CRAWLER}
DI.URL=${URL}

[抽取器]
FBType=websiteschema.crawler.fb.FBUnitExtractor
EO.EO={"发送Unit":"ADD"}
DI.UPATH=${UPATH}
DI.PTS=${PTS}
DI.MUST=["href","text"]
DO.OUT={"发送Unit":"UNITS","对象转换":"IN"}

[发送Unit]
FBType=websiteschema.crawler.fb.index.FBUnitIndexer
EO.EO={"对象转换":"EI"}
DI.HOST=${QUEUE_SERVER}
DI.PORT=5672
DI.QUEUE=index_queue_unit_guba

[对象转换]
FBType=websiteschema.crawler.fb.FBBeanWrapper
EO.EO={"保存链接":"ADD"}
DI.TYPE=websiteschema.cluster.analyzer.Link
DO.OUT={"保存链接":"LINKS"}

[保存链接]
FBType=websiteschema.crawler.fb.FBURLStorage
EO.ADD={"添加任务":"ADD"}
DI.JOBNAME=${JOBNAME}
DI.PARENT=${URL}
DI.DEPTH=1
DO.ADDED={"打印":"STR","添加任务":"LINKS"}

[添加任务]
FBType=websiteschema.crawler.fb.FBURLQueue
EO.EO={"打印":"PRINT"}
DI.HOST=${QUEUE_SERVER}
DI.PORT=5672
DI.QUEUE=${QUEUE_NAME}
DI.SITEID=${SITEID}
DI.JOBNAME=${JOBNAME}
DI.SID=${STARTURLID}
DI.WID=8
DI.JID=${JOBID}
DI.SCHEID=${SCHEID}
DI.CFG=CLS=custom
DI.DEPTH=1

[退出]
FBType=websiteschema.fb.common.merge.QuadMerge
EO.EO={"启动":"STOP"}

[打印]
FBType=websiteschema.fb.STDOUT
EO.EO={"退出":"EI4"}
DI.STR=hello world