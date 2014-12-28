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
DO.DOC={"抽取器":"IN"}
DO.URL={"抽取器":"URL"}
DO.STATUS={"保存结果":"STATUS"}
DI.USERAGENT=${USERAGENT}
DI.CRAWLER=${CRAWLER}
DI.URL=${URL}

[抽取器]
FBType=websiteschema.crawler.fb.FBDOMExtractor
EO.EO={"打标签COM_ORG":"EI"}
DI.CLS = ${CLS}
DO.OUT={"打标签COM_ORG":"DOC"}

[打标签COM_ORG]
FBType=com.apc.websiteschema.fb.FBStockEntity
EO.EO={"打标签_来源":"EI"}
DI.TAG = COM_ORG
DI.TARGET = ["TITLE","THREAD/CONTENT"]
DO.DOC={"打标签_来源":"DOC"}

[打标签_来源]
FBType=websiteschema.crawler.fb.index.FBTagging
EO.EO={"打标签COM_BBS":"EI"}
DI.SITE = ${SITEID}
DI.TAG_SITE = SOURCEINFO
DO.DOC={"打标签COM_BBS":"DOC"}

[打标签COM_BBS]
FBType=com.apc.websiteschema.fb.FBStockEntity
EO.EO={"增加日期字段":"EI"}
DI.TAG = COM_BBS
DI.TARGET = ["CHANNEL"]
DO.DOC={"增加日期字段":"DOC"}

[增加日期字段]
FBType=websiteschema.crawler.fb.field.FBTimeToDate
EO.EO={"保存结果":"SAVE"}
DI.DATE_TAG = STATDATE
DI.TIME_TAG = THREADS/DATE
DO.DOC={"保存结果":"DOC"}

[保存结果]
FBType=websiteschema.crawler.fb.FBURLStorage
DI.MAP={"DATE":"PUBLISHDATE", "TITLE":"DRETITLE", "URL":"DREREFERENCE", "CONTENT":"DRECONTENT"}
DI.DEF={"DREDBNAME":"BBS","JOBNAME":"${JOBNAME}"}
DI.ENCODE=["URL"]
DI.URL=${URL}
EO.SAVE={"发送":"ADD"}
DO.KEY={"发送":"KEY"}

[发送]
FBType=websiteschema.crawler.fb.index.FBIndexQueue
DI.HOST=${QUEUE_SERVER}
DI.PORT=5672
DI.QUEUE=index_queue_guba
EO.EO={"退出":"EI3"}

[退出]
FBType=websiteschema.fb.common.merge.QuadMerge
EO.EO={"启动":"STOP"}

