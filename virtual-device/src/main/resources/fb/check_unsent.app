namespace=fb.crawler

StartFB=启动
InitEvent=COLD

[Bean]
SpringBeans=spring-beans.xml

[启动]
FBType=websiteschema.fb.E_RESTART
EO.COLD={"检查并发送数据":"CHK"}

[检查并发送数据]
FBType=com.apc.websiteschema.fb.DreAddDataFB
EO.EO={"退出":"EI1"}

[退出]
FBType=websiteschema.fb.common.merge.QuadMerge
EO.EO={"启动":"STOP"}
