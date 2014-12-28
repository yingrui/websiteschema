StartFB=Start
InitEvent=COLD

[Bean]
SpringBeans=spring-beans.xml

[Start]
FBType=websiteschema.fb.E_RESTART
EO.COLD={"WebsiteschemaFactory":"EI"}

[WebsiteschemaFactory]
FBType=websiteschema.crawler.fb.FBWebsiteschema
EO.EO={"CRAWLER":"FETCH"}
EO.FAIL={"EXIT":"EI1"}
DO.OUT={"CRAWLER":"SCHEMA","EXTRACTOR":"SCHEMA"}
DI.SITE=${SITEID}

[CRAWLER]
FBType=websiteschema.crawler.fb.FBWebCrawler
EO.SUC={"EXTRACTOR":"EI"}
EO.FAL={"EXIT":"EI2"}
DO.DOC={"EXTRACTOR":"IN"}
DO.URL={"EXTRACTOR":"URL"}
DI.CRAWLER=${CRAWLER}
DI.URL=${URL}

[EXTRACTOR]
FBType=websiteschema.crawler.fb.FBDOMExtractor
EO.EO={"FIELD_FILTER":"EI"}
DI.CLS = ${CLS}
DO.OUT={"FIELD_FILTER":"DOC"}

[FIELD_FILTER]
FBType=websiteschema.crawler.fb.FBFieldFilter
EO.EO={"VALIDATE":"EI"}
DI.FILTER={"SOURCENAME":"websiteschema.cluster.analyzer.fields.SourceNameFilter"}
DO.DOC={"SAVE_CONTENT":"DOC","VALIDATE":"DOC","Convertor":"DOC"}

[VALIDATE]
FBType=websiteschema.crawler.fb.FBValidate
EO.YES={"SAVE_CONTENT":"SAVE"}
EO.NO={"EXIT":"EI3"}
DI.MUSTHAVE=["CONTENT","TITLE","DATE"]

[SAVE_CONTENT]
FBType=websiteschema.crawler.fb.FBURLStorage
EO.SAVE={"Convertor":"EI"}
DI.URL=${URL}

[Convertor]
FBType=com.apc.websiteschema.fb.DocToIdxFB
DI.MAP={"DATE":"PUBLISHDATE", "TITLE":"DRETITLE", "URL":"DREREFERENCE", "CONTENT":"DRECONTENT"}
DI.DEF={"DREDBNAME":"${DBNAME}","JOBNAME":"${JOBNAME}"}
DI.ENCODE=["URL"]
EO.EO={"SEND":"IDX"}
DO.IDX={"SEND":"IDX"}

[SEND]
FBType=com.apc.websiteschema.fb.DreAddDataFB
DI.SERVER=${DIH}
EO.EO={"EXIT":"EI4"}

[EXIT]
FBType=websiteschema.fb.common.merge.QuadMerge
EO.EO={"Start":"STOP"}