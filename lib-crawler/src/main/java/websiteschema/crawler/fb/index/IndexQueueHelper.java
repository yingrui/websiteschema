/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb.index;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import websiteschema.cluster.analyzer.Doc;
import websiteschema.element.DocumentUtil;
import websiteschema.model.domain.UrlLink;
import websiteschema.persistence.Mapper;
import websiteschema.utils.FileUtil;
import websiteschema.utils.MD5;

/**
 *
 * @author ray
 */
public class IndexQueueHelper {

    String path;
    String tempDir;
    File dir = null;
    Mapper<UrlLink> urlLinkMapper;
    Logger l = Logger.getLogger(IndexQueueHelper.class);

    IndexQueueHelper(String path, String tempDir) {
        this.path = path;
        this.tempDir = tempDir;
        File d = new File(tempDir);
        if (!d.exists()) {
            d.mkdir();
        }

        dir = new File(d.getAbsoluteFile() + File.separator + path);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    public String getPath() {
        return path;
    }

    public String getTempDir() {
        return tempDir;
    }

    public List<String> listMessages() {
        List<String> ret = new ArrayList<String>();
        for (File f : dir.listFiles()) {
            if (f.isFile() && !f.isHidden()) {
                ret.add(f.getAbsolutePath());
            }
        }
        return ret;
    }

    public void setUrlLinkMapper(Mapper<UrlLink> urlLinkMapper) {
        this.urlLinkMapper = urlLinkMapper;
    }

    public String compositeIndexFile(List<String> keys) {
        Document index = DocumentUtil.createEmptyDocument();
        Element root = index.createElement("ROOT");
        index.appendChild(root);
        for (String filename : keys) {
            String rowKey = FileUtil.read(filename);
            UrlLink urlLink = urlLinkMapper.get(rowKey.trim());
            if (null != urlLink) {
                try {
                    if (null != urlLink.getContent()) {
                        Document document = DocumentUtil.convertTo(urlLink.getContent());
                        Doc doc = new Doc(document);

                        Element eleDoc = index.createElement("DOCUMENT");
                        root.appendChild(eleDoc);
                        appendCustomDoc(doc, eleDoc, index);
                    }
                } catch (Exception ex) {
                    l.error(ex.getMessage(), ex);
                }
            }
        }
        return DocumentUtil.getXMLString(index);
    }

    private void appendCustomDoc(Doc doc, Element eleDoc, Document index) {
        doc.toW3CDocument(index, eleDoc, null);
    }

    public void saveMessageInFileSystem(String urlKey) {
        String md5 = null;
        try {
            md5 = MD5.getMD5(urlKey.getBytes("UTF-8"));
        } catch (Exception ex) {
            l.error(ex.getMessage(), ex);
        }
        File f = new File(dir.getAbsoluteFile() + File.separator + md5);
        if (!f.exists()) {
            FileUtil.save(f, urlKey);
        }
    }

    public void removeMessageInFileSystem(List<String> keys) {
        for (String filename : keys) {
            File f = new File(filename);
            if (f.exists()) {
                f.delete();
            }
        }
    }
}
