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
DO.OUT={"采集器":"SCHEMA"}
DI.SITE=${SITEID}

[采集器]
FBType=websiteschema.crawler.fb.FBWebCrawler
EO.SUC={"抽取器":"EI"}
EO.FAL={"退出":"EI2"}
DO.DOC={"抽取器":"IN"}
DO.URL={"抽取器":"URL"}
DI.USERAGENT=${USERAGENT}
DI.CRAWLER=${CRAWLER}
DI.URL=${URL}

[抽取器]
FBType=websiteschema.crawler.fb.FBLinksExtractor
EO.EO={"保存股吧列表":"EI"}
EO.EMPTY={"退出":"EI3"}
DI.XPATH = ${XPATH}
DO.OUT={"保存股吧列表":"LINKS"}

[保存股吧列表]
FBType=websiteschema.crawler.fb.bbs.FBForumList
EO.EO={"退出":"EI4"}
DI.SITEID=${SITEID}

[退出]
FBType=websiteschema.fb.common.merge.QuadMerge
EO.EO={"启动":"STOP"}