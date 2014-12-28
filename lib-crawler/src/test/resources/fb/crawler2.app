namespace=fb.crawler

StartFB=启动
InitEvent=COLD

[Bean]
SpringBeans=spring-beans.xml

[启动]
FBType=websiteschema.fb.E_RESTART
EO.COLD={"Websiteschema工厂":"EI"}

[初始化]
FBType=websiteschema.fb.common.split.DualSplit
EO.EO1={"Websiteschema工厂":"EI"}
EO.EO2={"ClusterModel工厂":"EI"}

[Websiteschema工厂]
FBType=websiteschema.crawler.fb.FBWebsiteschema
EO.EO={"ClusterModel工厂":"EI"}
EO.FAIL={"终止1":"EI1"}
#将Websiteschema传到Crawler
DO.OUT={"采集":"SCHEMA","抽取内容":"SCHEMA","分类":"SCHEMA"}
DI.SITE=${SITEID}

[ClusterModel工厂]
FBType=websiteschema.crawler.fb.FBClusterModel
EO.EO={"采集":"FETCH"}
EO.FAIL={"终止1":"EI2"}
DO.CM={"分类":"CM"}
DI.SITE=${SITEID}
DI.CACHE=true
DI.TIMEOUT=60000
DI.LOCAL=cache

[采集]
FBType=websiteschema.crawler.fb.FBWebCrawler
EO.SUC={"分类":"EI"}
EO.FAL={"终止1":"EI3"}
DO.DOCS={"分类":"DOCS","抽取链接":"DOCS","抽取内容":"DOCS"}
DO.URL={"抽取链接":"URL"}
DI.CRAWLER=${CRAWLER}
DI.URL=${URL}

[分类]
FBType=websiteschema.crawler.fb.FBClassifier
EO.DOC={"抽取内容":"EI"}
EO.LINK={"汇总链接":"EI1"}
EO.INV={"汇总链接":"EI2"}
DO.CLS={"抽取内容":"CLS"}
#DI.CM来自:ClusterModel工厂:DO.CM

[汇总链接]
FBType=websiteschema.fb.common.merge.DualMerge
EO.EO={"抽取链接":"EI"}

#--------------------------
#  抽取链接
#--------------------------

[抽取链接]
FBType=websiteschema.crawler.fb.FBLinksExtractor
EO.EO={"保存链接":"ADD"}
EO.EMPTY={"终止1":"EI4"}
DI.XPATH = ${XPATH}
DO.OUT={"保存链接":"LINKS"}

[保存链接]
FBType=websiteschema.crawler.fb.FBURLStorage
EO.ADD={"添加新任务":"ADD"}
DI.JOBNAME=money_163_com_1
DI.PARENT=${URL}
DI.DEPTH=1
DO.ADDED={"打印输出":"STR","添加新任务":"LINKS"}

[添加新任务]
FBType=websiteschema.crawler.fb.FBURLQueue
EO.EO={"打印输出":"PRINT"}
EO.FATAL={"终止2":"EI4"}
DI.HOST=localhost
DI.PORT=5672
DI.QUEUE=url_queue
DI.SITEID=${SITEID}
DI.JOBNAME=${JOBNAME}
DI.SID=${STARTURLID}
DI.WID=${WRAPPERID}
DI.JID=${JOBID}
DI.CFG=
DI.DEPTH=1

#--------------------------
#  抽取内容
#--------------------------

[抽取内容]
FBType=websiteschema.crawler.fb.FBDOMExtractor
EO.EO={"过滤抽取结果":"EI"}
#DI.CLS = 30
DI.URL = ${URL}
DO.OUT={"过滤抽取结果":"DOC"}

[过滤抽取结果]
FBType=websiteschema.crawler.fb.FBFieldFilter
EO.EO={"检验抽取结果":"EI"}
DI.FILTER={"SOURCENAME":"websiteschema.cluster.analyzer.fields.SourceNameFilter"}
DO.DOC={"保存抽取结果":"DOC","检验抽取结果":"DOC"}

[检验抽取结果]
FBType=websiteschema.crawler.fb.FBValidate
EO.YES={"保存抽取结果":"SAVE"}
EO.NO={"终止2":"EI2"}
DI.MUSTHAVE=["CONTENT","TITLE","DATE"]

[保存抽取结果]
FBType=websiteschema.crawler.fb.FBURLStorage
EO.SAVE={"XML2Str":"TRAN"}
DI.URL=${URL}
DO.DOC={"XML2Str":"DOC"}

#[转换为IDX]
#FBType=com.apc.websiteschema.fb.DocToIdxFB
#EO.EO={"XML2Str":"TRAN"}
#DI.MAP={"TITLE":"DRETITLE","CONTENT":"DRECONTENT","URL":"DREREFERENCE"}
#DI.DEF={"DREDBNAME":"NEWS"}
#DI.ENCODE=["DREREFERENCE"]
#DO.IDX={"XML2Str":"DOC"}
#
#[发送IDX]
#FBType=com.apc.websiteschema.fb.DreAddDataFB
#EO.EO={"终止2":"EI4"}
#DI.SERVER=${DIH}

[终止1]
FBType=websiteschema.fb.common.merge.QuadMerge
EO.EO={"终止2":"EI1"}

[终止2]
FBType=websiteschema.fb.common.merge.QuadMerge
EO.EO={"启动":"STOP"}

[XML2Str]
FBType=websiteschema.crawler.fb.FBXMLToString
EO.EO={"打印输出":"PRINT"}
DO.OUT={"打印输出":"STR"}

[打印输出]
FBType=websiteschema.fb.STDOUT
EO.EO={"终止2":"EI3"}
DI.STR=hello world