/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import websiteschema.element.DocumentUtil;
import websiteschema.fb.annotation.*;
import websiteschema.fb.core.FunctionBlock;

import java.util.*;

@EI(name = {"EXTRACT:EXT"})
@EO(name = {"EO", "FATAL", "EMPTY"})
@Description(desc = "抽取DOM树中指定表格的内容")
public class FBTableExtractor extends FunctionBlock {

    @DI(name = "IN")
    public Document document;
    @DI(name = "XPATH")
    public String tableXPath = null;
    @DI(name = "HEADER_XPATH")
    public String headerXPath = null;
    @DI(name = "CONTENT_XPATH")
    public String contentXPath = null;
    @DO(name = "OUT", relativeEvents = {"EO"})
    public List<Map<String, String>> table = new ArrayList<>();

    private List<String> headers = new ArrayList<>();

    @Algorithm(name = "EXT")
    public void extract() {
        InitiateXPath();
        try {
            extractTable();
            if (null != table && !table.isEmpty()) {
                this.triggerEvent("EO");
            } else {
                this.triggerEvent("EMPTY");
            }
        } catch (Exception ex) {
            l.error(this.getName() + " error when extract contents: " + ex.getMessage(), ex);
            this.triggerEvent("FATAL");
        }
    }

    private void InitiateXPath() {
        if (tableXPath == null) return;

        if (headerXPath == null) {
            headerXPath = tableXPath.trim() + "/thead/tr/th";
        }

        if (contentXPath == null) {
            contentXPath = tableXPath.trim() + "/tbody/tr";
        }
    }

    private void extractTable() {
        extractHeaders();
        extractContent();
    }

    private void extractHeaders() {
        l.debug("extract table header");
        if (null != document && null != headerXPath) {
            List<Node> nodes = DocumentUtil.getByXPath(document, headerXPath.trim());
            for(Node th : nodes) {
                String head = getTextContent(th);
                headers.add(head);
            }
        }
    }

    private void extractContent() {
        l.debug("extract table content");
        if (null != document && null != contentXPath) {
            List<Node> nodes = DocumentUtil.getByXPath(document, contentXPath.trim());
            for(Node tr : nodes) {
                table.add(extractRow(tr));
            }
        }
    }

    private Map<String, String> extractRow(Node rowElement) {
        NodeList children = rowElement.getChildNodes();
        Map<String, String> row = new LinkedHashMap<>();
        for(int i = 0; i < children.getLength(); i++) {
            Node item = children.item(i);
            String text = getTextContent(item);
            row.put(headers.get(i), text);
        }
        return row;
    }

    private String getTextContent(Node item) {
        return item.getTextContent().replaceAll("[\\s\\r\\n]", "");
    }
}

