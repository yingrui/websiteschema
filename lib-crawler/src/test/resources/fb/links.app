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
#DI.SITE=www_163_com_1
DI.SITE=www_qq_com_4845

[CRAWLER]
FBType=websiteschema.crawler.fb.FBWebCrawler
EO.SUC={"EXTRACTOR":"EI"}
EO.FAL={"EXIT":"EI2"}
DO.DOC={"EXTRACTOR":"IN"}
DO.URL={"EXTRACTOR":"URL"}
DI.CRAWLER=websiteschema.crawler.SimpleHttpCrawler
#DI.URL=http://money.163.com/special/00252G50/macroNew.html
DI.URL=http://tech.qq.com/household/heanews/heanewslist.htm

[EXTRACTOR]
FBType=websiteschema.crawler.fb.FBLinksExtractor
EO.EO={"SAVE_LINKS":"ADD"}
EO.EMPTY={"EXIT":"EI3"}
#DI.XPATH = html/body/div[@class='area clearfix']/div[@class='colLM']/ul[@class='newsList dotted']/li/span[@class='article']/a
#DI.XPATH = html/body/div[@id='techMain']/div[@id='list_BotBox']/div[@class='LeftBox' and @id='list_B_L']/div[@class='Con_Box' and @id='list_B_L_C']/div[@class='Con_Box Pos_re']/div[@id='listZone']/div[@id='listZone']/ul[@class='list']/li/a
DI.XPATH = html/body/div[@id='techMain']/div[@id='list_BotBox']/div[@class='LeftBox' and @id='list_B_L']/div[@class='Con_Box' and @id='list_B_L_C']/div[@class='Con_Box Pos_re']/div[@id='listZone']/ul[@class='list']/li/a
DO.OUT={"SAVE_LINKS":"LINKS"}

[SAVE_LINKS]
FBType=websiteschema.crawler.fb.FBURLStorage
EO.ADD={"ADD_NEW_TASK":"ADD"}
DI.JOBNAME=money_163_com_1
#DI.PARENT=http://money.163.com/special/00252G50/macroNew.html
DI.PARENT=http://tech.qq.com/household/heanews/heanewslist.htm
DI.DEPTH=1
DO.ADDED={"Console":"STR","ADD_NEW_TASK":"LINKS"}

[ADD_NEW_TASK]
FBType=websiteschema.crawler.fb.FBURLQueue
EO.EO={"Console":"PRINT"}
DI.HOST=localhost
DI.PORT=5672
DI.QUEUE=url_queue
DI.SITEID=www_163_com_1
DI.JOBNAME=money_163_com_1
DI.SID=1
DI.WID=2
DI.JID=2
DI.CFG=CLS=0
DI.DEPTH=1

[EXIT]
FBType=websiteschema.fb.common.merge.QuadMerge
EO.EO={"Start":"STOP"}

[Console]
FBType=websiteschema.fb.STDOUT
EO.EO={"EXIT":"EI4"}
DI.STR=hello world