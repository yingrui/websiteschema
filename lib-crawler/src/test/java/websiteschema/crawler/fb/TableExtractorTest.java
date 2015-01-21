/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author ray
 */
public class TableExtractorTest {

    @Test
    public void should_extract_html_table() throws Exception {
        FBWebCrawler crawler = new FBWebCrawler();
        crawler.crawlerType = "websiteschema.crawler.browser.BrowserWebCrawler";
        crawler.url = "http://fund.eastmoney.com/data/fundranking.html";
        crawler.fetch();

        System.out.println(crawler.do_url);

        FBTableExtractor extractor = new FBTableExtractor();
        extractor.tableXPath = "//table[@id='dbtable']";

        extractor.document = crawler.out;
        extractor.extract();
        Assert.assertFalse(extractor.table.isEmpty());
        Assert.assertEquals(50, extractor.table.size());
    }
}

