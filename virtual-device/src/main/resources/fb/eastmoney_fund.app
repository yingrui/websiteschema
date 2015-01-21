StartFB=Start
InitEvent=COLD

[Bean]
SpringBeans=spring-beans.xml

[Start]
FBType=websiteschema.fb.E_RESTART
EO.COLD={"CRAWLER":"FETCH"}

[CRAWLER]
FBType=websiteschema.crawler.fb.FBWebCrawler
EO.SUC={"EXTRACTOR":"EXTRACT"}
EO.FAL={"EXIT":"EI2"}
DO.DOC={"EXTRACTOR":"IN"}
DO.URL={"EXTRACTOR":"URL"}
DI.CRAWLER=websiteschema.crawler.browser.BrowserWebCrawler
DI.URL=http://fund.eastmoney.com/data/fundranking.html#pn2000;

[EXTRACTOR]
FBType=websiteschema.crawler.fb.FBTableExtractor
EO.EO={"SAVE_CONTENT":"SAVE"}
DI.XPATH=//table[@id='dbtable']
DO.OUT={"SAVE_CONTENT":"TABLE"}

[SAVE_CONTENT]
FBType=websiteschema.crawler.fb.FBTableStorage
EO.EO={"EXIT":"EI4"}
DI.DATA_SOURCE=dataSource
DI.TABLE_NAME=Fund
DI.COLUMNS={"基金代码": "CODE", "基金简称":"SHORT_NAME", "日期":"DATE", "单位净值": "NET_ASSET_VAL", "累计净值": "NET_CUMULATIVE_VAL", "日增长率": "DAY", "近1周": "WEEK", "近1月": "MONTH", "近3月": "THREE_MONTH", "近6月":"SIX_MONGTH", "近1年": "YEAR", "近2年": "TWO_YEAR", "近3年": "THREE_YEAR", "今年来":"THIS_YEAR", "成立来": "SINCE_CREATED", "自定义": "CUSTOMIZE","手续费":"FEE"}

[EXIT]
FBType=websiteschema.fb.common.merge.QuadMerge
EO.EO={"Start":"STOP"}