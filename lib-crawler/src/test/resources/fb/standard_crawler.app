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
DO.DOCS={"分类":"DOCS","抽取内容":"DOCS"}
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
EO.EO={"终止1":"EI4"}

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
DO.DOC={"保存抽取结果":"DOC","检验抽取结果":"DOC","转换数据为IDX格式":"DOC"}

[检验抽取结果]
FBType=websiteschema.crawler.fb.FBValidate
EO.YES={"保存抽取结果":"SAVE"}
EO.NO={"终止2":"EI2"}
DI.MUSTHAVE=["CONTENT","TITLE","DATE"]

[保存抽取结果]
FBType=websiteschema.crawler.fb.FBURLStorage
EO.SAVE={"转换数据为IDX格式":"EI"}
DI.URL=${URL}

[转换数据为IDX格式]
FBType=com.apc.websiteschema.fb.DocToIdxFB
DI.MAP={"DATE":"PUBLISHDATE", "TITLE":"DRETITLE", "URL":"DREREFERENCE", "CONTENT":"DRECONTENT"}
DI.DEF={"DREDBNAME":"${DBNAME}","JOBNAME":"${JOBNAME}"}
DI.ENCODE=["URL"]
EO.EO={"发送IDX":"IDX"}
DO.IDX={"发送IDX":"IDX"}

[发送IDX]
FBType=com.apc.websiteschema.fb.DreAddDataFB
DI.SERVER=${DIH}
EO.EO={"终止2":"EI4"}

[终止1]
FBType=websiteschema.fb.common.merge.QuadMerge
EO.EO={"终止2":"EI1"}

[终止2]
FBType=websiteschema.fb.common.merge.QuadMerge
EO.EO={"启动":"STOP"}
