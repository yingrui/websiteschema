/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.browser;

import com.webrenderer.swing.event.BrowserEvent;
import com.webrenderer.swing.event.BrowserListener;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 *
 * @author ray
 */
public class MyBrowserListener implements BrowserListener {

    Logger l = Logger.getLogger(MyBrowserListener.class);
    private final static Pattern imagePat = Pattern.compile(".*\\.(bmp|png|jpeg|jpg|gif|icon|ico|swf)\\b.*");
    private boolean blockImg = true;

    MyBrowserListener(boolean blockImg) {
        this.blockImg = blockImg;
    }

    @Override
    public void onLinkChange(BrowserEvent be) {
        l.debug("onLinkChange");
    }

    @Override
    public void onFavicon(BrowserEvent be) {
        l.debug("onFavicon");
    }

    @Override
    public void onURLChange(BrowserEvent be) {
        l.debug("onURLChange");
    }

    @Override
    public void onTitleChange(BrowserEvent be) {
        l.debug("onTitleChange");
    }

    @Override
    public void onBeforeNavigate(BrowserEvent be) {
        l.debug("onBeforeNavigate");
    }

    @Override
    public void onNavigationCancelled(BrowserEvent be) {
        l.debug("onNavigationCancelled");
    }

    @Override
    public void onLoadIntercept(BrowserEvent be) {
        l.debug("onLoadIntercept");
        String headers = be.getHeaders();
        l.debug(headers);
//        be.blockLoad();
    }

    public static void main(String[] args) {
        String url = "http://img.t.sinajs.cn/t4/style/images/common/layer_arrow.png?id=1323328404357";
        System.out.println(imagePat.matcher(url).matches());
        url = "http://rm.api.weibo.com/remind/unread_count.json?target=api&_pid=10002&count=9&source=3818214747&callback=STK_1332323474623109";
        System.out.println(imagePat.matcher(url).matches());
    }
}
