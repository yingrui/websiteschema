/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.utils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author ray
 */
public class UrlLinkUtil {

    private final static UrlLinkUtil ins = new UrlLinkUtil();

    public static UrlLinkUtil getInstance() {
        return ins;
    }

    public URI getURI(String pageUrl, String href) {
        try {
            if (null != href && null != pageUrl) {
                if (href.startsWith("http://") || href.startsWith("ftp://")) {
                    return new URI(href.trim());
                } else if (href.indexOf("://") > 0) {
                    // UnsupportProtocol.
                    return new URI(href.trim());
                }
                URI uri = new URI(pageUrl);
                return uri.resolve(href.trim());
            }
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public URL getURL(String pageUrl, String href) {
        try {
            if (null != href && null != pageUrl) {
                if (href.startsWith("http://") || href.startsWith("ftp://")) {
                    return new URL(href.trim());
                } else if (href.indexOf("://") > 0) {
                    // UnsupportProtocol.
                    return new URL(href.trim());
                }
                URI uri = new URI(pageUrl);
                return uri.resolve(href.trim()).toURL();
            }
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private boolean shouldEscape(Character ch) {
        if (ch.equals(' ')) {
            return true;
        } else if (ch > 127) {
            return true;
        }
        return false;
    }

    public URI getURI(String pageUrl, String href, String charset, Map<String, String> charsetMap, String def) {
        return getURI(pageUrl, escapeIfNecessary(href, charset, charsetMap, def));
    }

    public URL getURL(String pageUrl, String href, String charset, Map<String, String> charsetMap, String def) {
        return getURL(pageUrl, escapeIfNecessary(href, charset, charsetMap, def));
    }

    private String escapeIfNecessary(String href, String charset, Map<String, String> charsetMap, String def) {
        StringBuilder sb = new StringBuilder();
        for (Character ch : href.toCharArray()) {
            if (shouldEscape(ch)) {
                try {
                    sb.append(URLEncoder.encode(String.valueOf(ch), charset));
                } catch (UnsupportedEncodingException ex) {
                    charset = parseCharset(charset, charsetMap, def);
                    try {
                        sb.append(URLEncoder.encode(String.valueOf(ch), charset));
                    } catch (Exception e) {
                    }
                }
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    private String parseCharset(String charset, Map<String, String> charsetMap, String def) {
        charset = charset.toLowerCase();
        if (charset.contains("gb")) {
            charset = "gbk";
        } else if (charsetMap.containsKey(charset)) {
            charset = charsetMap.get(charset);
        } else {
            charset = def;
        }

        return charset;
    }

    public String convertUrlToRowKey(URL url) {
        String ret = null;

        if (null != url) {
            String schema = url.getProtocol();
            String host = (new StringBuilder(url.getHost())).reverse().toString();
            String query = url.getQuery();
            String path = url.getPath();
            if (null != query) {
                ret = schema + "://" + host + path + "?" + query;
            } else {
                ret = schema + "://" + host + path;
            }
        }

        return ret;
    }

    public String convertUrlToRowKey(URL url, String siteId) {
        String ret = null;

        if (null != url) {
            String schema = url.getProtocol();
            String host = url.getHost();
            String query = url.getQuery();
            String path = url.getPath();
            String date = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm");

            host = (new StringBuilder(host)).reverse().toString();

            if (null != query) {
                ret = siteId + "+" + date + "+" + schema + "://" + host + path + "?" + query;
            } else {
                ret = siteId + "+" + date + "+" + schema + "://" + host + path;
            }
        }

        return ret;
    }

    public String convertUriToRowKey(URI uri, String siteId) {
        String ret = null;

        if (null != uri) {
            String schema = uri.getScheme();
            String host = uri.getHost();
            String query = uri.getQuery();
            String path = uri.getPath();
            String date = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm");

            host = (new StringBuilder(host)).reverse().toString();

            if (null != query) {
                ret = siteId + "+" + date + "+" + schema + "://" + host + path + "?" + query;
            } else {
                ret = siteId + "+" + date + "+" + schema + "://" + host + path;
            }
        }

        return ret;
    }

    /**
     * 只要有一个正则表达式match上了，就返回true。
     * 如果没有指定mustHave，则返回true。
     * 如果包含或匹配任一dontHave，则返回false。
     * @param url
     * @param mustHave
     * @return
     */
    public boolean matchOnePattern(String url, String[] mustHave, String[] dontHave) {
        boolean ret = false;
        if (null != mustHave && mustHave.length > 0) {
            for (String pat : mustHave) {
                if (!"".equals(pat)) {
                    if (url.matches(pat)) {
                        ret = true;
                        break;
                    }
                }
            }
        } else {
            ret = true;
        }

        if (ret) {
            if (null != dontHave && dontHave.length > 0) {
                for (String pat : dontHave) {
                    if (!"".equals(pat)) {
                        if (url.contains(pat) || url.matches(pat)) {
                            ret = false;
                            break;
                        }
                    }
                }
            }
        }

        return ret;
    }

    /**
     * 必须包含或匹配全部mustHave中指定的字符串或正则表达式。
     * 一定不能包含或匹配任一dontHave中指定的字符串或正则表达式。
     * @param url
     * @param mustHave
     * @param dontHave
     * @return
     */
    public boolean match(String url, String[] mustHave, String[] dontHave) {
        if (null != url) {
            // 必须同时包含设置中的参数。
            if (null != mustHave) {
                for (String must : mustHave) {
                    if (!"".equals(must)) {
                        if (!url.contains(must) && !url.matches(must)) {
                            return false;
                        }
                    }
                }
            }
            // 不能包含任意一个
            if (null != dontHave) {
                for (String dont : dontHave) {
                    if (!"".equals(dont)) {
                        if (url.contains(dont) || url.matches(dont)) {
                            return false;
                        }
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 简单匹配（正则match，会因元字符等问题抛出PatternSyntaxException）
     * @param url
     * @param mustHave
     * @param dontHave
     * @return
     */
    public boolean match_simple(String url, String[] mustHave, String[] dontHave) {
        if (null != url) {
            // 必须同时包含设置中的参数。
            if (null != mustHave) {
                for (String must : mustHave) {
                    if (!"".equals(must)) {
                        if (!url.contains(must)) {
                            return false;
                        }
                    }
                }
            }
            // 不能包含任意一个
            if (null != dontHave) {
                for (String dont : dontHave) {
                    if (!"".equals(dont)) {
                        if (url.contains(dont)) {
                            return false;
                        }
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 正则匹配（正则match，会因元字符等问题抛出PatternSyntaxException）
     * @param url
     * @param mustHave
     * @param dontHave
     * @return
     */
    public boolean match_reg(String url, String[] mustHave, String[] dontHave) {
        if (null != url) {
            // 必须同时包含设置中的参数。
            if (null != mustHave) {
                for (String must : mustHave) {
                    if (!"".equals(must)) {
                        if (!url.matches(must)) {
                            return false;
                        }
                    }
                }
            }
            // 不能包含任意一个
            if (null != dontHave) {
                for (String dont : dontHave) {
                    if (!"".equals(dont)) {
                        if (url.matches(dont)) {
                            return false;
                        }
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }
//    public static void main(String args[]) throws URISyntaxException {
//        System.out.println(UrlLinkUtil.getInstance().getURL("http://utility.baidu.com/traf/click.php?id=215&url=http://www.baidu.com", "test"));
//        System.out.println(UrlLinkUtil.getInstance().getURL("http://utility.baidu.com/traf/click.php?id=215&url=http://www.baidu.com", ""));
//
//        URI uri = new URI("http://utility.baidu.com/traf/click.php?id=215&url=http://www.baidu.com");
//        System.out.println("schema: " + uri.getScheme());
//        System.out.println("schema-specific-part: " + uri.getSchemeSpecificPart());
//        System.out.println("fragment: " + uri.getFragment());
//        System.out.println("host: " + uri.getHost());
//        System.out.println("path: " + uri.getPath());
//        System.out.println("query: " + uri.getQuery());
//    }
}
