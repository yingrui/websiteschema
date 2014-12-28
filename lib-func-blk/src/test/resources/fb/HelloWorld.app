namespace=fb.test

StartFB=Start
InitEvent=COLD

[Bean]
SpringBeans=spring.xml

[Start]
FBType=websiteschema.fb.E_RESTART
EO.COLD={"Timer":"INIT"}

[Timer]
FBType=websiteschema.fb.Timer
EO.EO={"Console":"PRINT","Threshold":"COMP"}
DO.TIMES={"Threshold":"L"}
DI.INTERVAL=1000

[Console]
FBType=websiteschema.fb.STDOUT
DI.STR=Hello world!

[Threshold]
FBType=websiteschema.fb.Compare
DI.R=1
EO.EQ={"Start":"STOP"}