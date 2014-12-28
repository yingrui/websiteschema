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
DI.SQL=select id, Author AUTHOR, Source SOURCENAME, Content DRECONTENT, PublishTime PUBLISHDATE, Hits CLICKED, CommentCount COMMENT_COUNT, SourceChannel CHANNEL, URL DREREFERENCE, Title DRETITLE, ChannelId JOBNAME, UNIX_TIMESTAMP(createTime) DREDATE from RESULTS_NEWS where Flag=1 limit 0, 2000
DI.UPDATE_SQL=update RESULTS_NEWS set Flag=0
EO.EMPTY={"退出":"EI1"}
EO.EO={"检验数据":"BAT"}
DO.DOCS={"检验数据":"DOCS"}

[检验数据]
FBType=websiteschema.crawler.fb.FBValidate
DI.MUSTHAVE=["DRETITLE","PUBLISHDATE"]
EO.BAT_OUT={"打标签":"BAT"}
DO.DOCS={"打标签":"DOCS"}

[打标签]
FBType=com.apc.websiteschema.fb.FBFmsChannel
EO.BAT_OUT={"转换数据":"BAT"}
DO.DOCS={"转换数据":"DOCS"}

[转换数据]
FBType=com.apc.websiteschema.fb.DocToIdxFB
DI.DEF={"DREDBNAME":"NEWS"}
DI.ENCODE=["DREREFERENCE"]
EO.BAT_OUT={"发送":"EI"}
DO.CONTENT={"发送":"IN"}

[发送]
FBType=com.apc.websiteschema.fb.DreAddDataFB
DI.SERVER=["10.8.0.55:3011"]
EO.EO={"退出":"EI2"}

[退出]
FBType=websiteschema.fb.common.merge.QuadMerge
EO.EO={"启动":"STOP"}
