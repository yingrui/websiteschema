namespace=fb.crawler

StartFB=启动
InitEvent=COLD

[Bean]
SpringBeans=spring-beans.xml

[启动]
FBType=websiteschema.fb.E_RESTART
EO.COLD={"抽取":"EI"}

[抽取]
FBType=websiteschema.crawler.fb.FBDatabaseExtractor
DI.JDBC_URL=jdbc:mysql://10.8.0.50:3306/weibo
DI.JDBC_DRIVER=com.mysql.jdbc.Driver
DI.USR=cpp
DI.PW=cpp
DI.PK=id
DI.SQL=select id, AuthorName AUTHOR, AuthorUrl AUTHOR_URL, WeiboText DRECONTENT, DATE_FORMAT(FROM_UNIXTIME(PublishTime/1000), '%Y-%m-%d %H:%i:%S') PUBLISHDATE, ForwardCount FORWARD_COUNT, CommentCount COMMENT_COUNT, Website SOURCEINFO, KeyMd5 DREREFERENCE from Results_sina where Flag=1 limit 0, 1000
DI.UPDATE_SQL=update Results_sina set Flag=0
EO.EMPTY={"退出":"EI1"}
EO.EO={"打标签COM_ORG":"BAT"}
DO.DOCS={"打标签COM_ORG":"DOCS"}

[打标签COM_ORG]
FBType=com.apc.websiteschema.fb.FBStockEntity
EO.BAT_OUT={"转换数据":"BAT"}
DI.TAG = COM_ORG
DI.TARGET = ["DRECONTENT"]
DO.DOCS={"转换数据":"DOCS"}


[转换数据]
FBType=com.apc.websiteschema.fb.DocToIdxFB
DI.DEF={"DREDBNAME":"WEIBO"}
EO.BAT_OUT={"发送":"EI"}
DO.CONTENT={"发送":"IN"}

[发送]
FBType=com.apc.websiteschema.fb.DreAddDataFB
DI.SERVER=["10.8.0.55:3401"]
EO.EO={"退出":"EI2"}

[退出]
FBType=websiteschema.fb.common.merge.QuadMerge
EO.EO={"启动":"STOP"}
