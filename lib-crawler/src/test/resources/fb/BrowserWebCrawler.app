namespace=fb.crawler

StartFB=Start
InitEvent=COLD

[Bean]
SpringBeans=spring-beans.xml


[Start]
FBType=websiteschema.fb.E_RESTART
EO.COLD={"WebsiteschemaFactory":"EI"}

[WebsiteschemaFactory]
FBType=websiteschema.crawler.fb.FBWebsiteschema
#启动抓取
EO.EO={"CRAWLER":"FETCH"}
EO.FAIL={"EXIT":"EI1"}
#将Websiteschema传到Crawler
DO.OUT={"CRAWLER":"SCHEMA"}
DI.SITE=travel_163_com_3657

[CRAWLER]
FBType=websiteschema.crawler.fb.FBWebCrawler
EO.SUC={"EXTRACTOR":"EI"}
EO.FAL={"EXIT":"EI2"}
DO.DOC={"EXTRACTOR":"IN"}
DO.URL={"EXTRACTOR":"URL"}
DI.CRAWLER=websiteschema.crawler.browser.BrowserWebCrawler
DI.URL=http://travel.163.com/special/00063K6K/fallow.html

[EXTRACTOR]
FBType=websiteschema.crawler.fb.FBLinksExtractor
EO.EO={"SAVE_LINKS":"ADD"}
EO.FATAL={"EXIT":"EI4"}
EO.EMPTY={"EXIT":"EI3"}
DI.XPATH =html/body/div[@class='area ui_bg1']/div[@class='colL']/div[@class='content']/ul[@class='list_f14d']/li
DO.OUT={"SAVE_LINKS":"LINKS"}

[SAVE_LINKS]
FBType=websiteschema.crawler.fb.FBURLStorage
EO.ADD={"ADD_NEW_TASK":"ADD"}
DI.JOBNAME=travel_163_com_3657
DI.PARENT=http://travel.163.com/special/00063K6K/fallow.html
DI.DEPTH=1
DO.ADDED={"Console":"STR","ADD_NEW_TASK":"LINKS"}

[ADD_NEW_TASK]
FBType=websiteschema.crawler.fb.FBURLQueue
EO.EO={"Console":"PRINT"}
DI.HOST=localhost
DI.PORT=5672
DI.QUEUE=url_queue
DI.SITEID=www_163_com_1
DI.JOBNAME=travel_163_com_3657
DI.SID=1
DI.WID=2
DI.JID=2
DI.CFG=CLS=0
DI.DEPTH=1

[EXIT]
FBType=websiteschema.fb.common.merge.QuadMerge
EO.EO={"Start":"STOP"}

[Convertor]
FBType=websiteschema.crawler.fb.FBXMLToString
EO.EO={"Console":"PRINT","Start":"STOP"}
DO.OUT={"Console":"STR"}

[Console]
FBType=websiteschema.fb.STDOUT
DI.STR=hello world
