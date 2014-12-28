namespace=fb.crawler

StartFB=Start
InitEvent=COLD

[Start]
FBType=websiteschema.fb.E_RESTART
EO.COLD={"WebsiteschemaFactory":"INIT"}

[WebsiteschemaFactory]
FBType=websiteschema.crawler.fb.FBWebsiteschema
EO.SUC={"CRAWLER":"FETCH"}
DO.OUT={"CRAWLER":"SCHEMA","EXTRACTOR":"SCHEMA"}
DI.SITE=localhost:8080_2

[CRAWLER]
FBType=websiteschema.crawler.fb.FBWebCrawler
EO.SUC={"EXTRACTOR":"EXTRACT"}
EO.FAL={"Start":"STOP"}
DO.DOC={"EXTRACTOR":"IN"}
DI.URL=http://localhost:8080/docs/

[EXTRACTOR]
FBType=websiteschema.crawler.fb.FBDOMExtractor
EO.EO={"Convertor":"TRAN"}
DO.OUT={"Convertor":"DOC"}

[Convertor]
FBType=websiteschema.crawler.fb.FBXMLToString
EO.EO={"Console":"PRINT","Start":"STOP"}
DO.OUT={"Console":"STR"}

[Console]
FBType=websiteschema.fb.STDOUT
DI.STR=hello world