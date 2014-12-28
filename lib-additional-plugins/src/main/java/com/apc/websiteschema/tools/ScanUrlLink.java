/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apc.websiteschema.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import websiteschema.cluster.analyzer.Doc;
import websiteschema.common.base.Function;
import websiteschema.element.DocumentUtil;
import websiteschema.model.domain.UrlLink;
import websiteschema.model.domain.UrlLog;
import websiteschema.persistence.Mapper;
import websiteschema.persistence.hbase.UrlLinkMapper;
import websiteschema.persistence.hbase.UrlLogMapper;
import websiteschema.utils.Escape;

/**
 *
 * @author ray
 */
public class ScanUrlLink {

    Map<String, String> map = new HashMap<String, String>();
    Map<String, String> def = new HashMap<String, String>();
    List<String> encodeFields = new ArrayList<String>();
    Logger l = Logger.getLogger(getClass());

    ScanUrlLink() {
        map.put("DATE", "PUBLISHDATE");
        map.put("TITLE", "DRETITLE");
        map.put("URL", "DREREFERENCE");
        map.put("CONTENT", "DRECONTENT");
        def.put("DREDBNAME", "BBS");
        def.put("JOBNAME", "guba_eastmoney_com_100");
        encodeFields.add("URL");
        List<String> server = new ArrayList<String>();
        server.add("localhost:9601");
    }

    public static void main(String args[]) {
        String jobname = "guba_eastmoney_com_100";
        String start = "2012";
        String end = "2012-03-13 18";
        try {
            jobname = args[0];
            start = args[1];
            end = args[2];
        } catch (Exception ex) {
            System.err.println("invalid input arguments.");
//            System.exit(0);
        }
        ScanUrlLink tool = new ScanUrlLink();

        tool.scanAndSend(jobname, start, end);
    }

    public void scanAndSend(String jobname, String startDate, String endDate) {
        final Mapper<UrlLog> mapper = new UrlLogMapper();
        final Mapper<UrlLink> linkMapper = new UrlLinkMapper();
        mapper.batchScan(jobname + "+" + startDate, jobname + "+" + endDate, 1000, new Function<List<UrlLog>>() {

            @Override
            public void invoke(List<UrlLog> args) {
                Document index = create();
                Element root = index.createElement("ROOT");
                index.appendChild(root);
                for (UrlLog arg : args) {
                    String rowKey = arg.getURLRowKey();
                    l.debug(rowKey);
                    UrlLink urlLink = linkMapper.get(rowKey);
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
            }
        });
    }

    private Document create() {
        try {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true); // never forget this!
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            Document doc = builder.newDocument();
            return doc;
        } catch (Exception ex) {
            return null;
        }
    }

    private void convert(Doc doc, Element eleDoc, Document index, Map<String, String> map, Map<String, String> def, List<String> encodeFields) {
        try {
            if (null != def) {
                for (String key : def.keySet()) {
                    doc.addField(key, def.get(key));
                }
            }
            if (null != encodeFields) {
                for (String field : encodeFields) {
                    escape(doc, field);
                }
            }
            doc.toW3CDocument(index, eleDoc, map);
        } catch (Exception ex) {
            l.error(ex.getMessage(), ex);
            throw new RuntimeException("Doc不能转换成IDX, " + ex.getMessage());
        }
    }

    private void escape(Doc doc, String field) {
        Collection<String> values = doc.getValues(field);
        if (null != values && !values.isEmpty()) {
            Collection<String> newValues = new ArrayList<String>();
            for (String value : values) {
                newValues.add(Escape.escape(value, "UTF-8"));
            }
            doc.remove(field);
            for (String value : newValues) {
                doc.addField(field, value);
            }
        }
    }

    public void appendCustomDoc(Doc doc, Element eleDoc, Document index) {
        convert(doc, eleDoc, index, map, def, encodeFields);
    }
}
