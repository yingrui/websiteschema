namespace=fb.crawler

StartFB=启动
InitEvent=COLD

[Bean]
SpringBeans=spring-beans.xml

[启动]
FBType=websiteschema.fb.E_RESTART
EO.COLD={"发送数据":"SEND"}

[发送数据]
FBType=websiteschema.crawler.fb.index.FBIndexQueue
DI.HOST=${QUEUE_SERVER}
DI.PORT=5672
DI.QUEUE=index_queue_guba
DI.INDEX_HOST=localhost
DI.INDEX_PORT=9601
EO.EO={"退出":"EI1"}

[退出]
FBType=websiteschema.fb.common.merge.QuadMerge
EO.EO={"启动":"STOP"}
