/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.element.xpath;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ray
 */
public class XPathParser {

    List<Node> list = new ArrayList<Node>();

    public XPathParser() {
    }

    public XPathParser(String xpath) {
        parse(xpath);
    }

    public List<Node> getList() {
        return list;
    }

    public void setNamespace(String ns) {
        for(Node node : list) {
            node.setNamespace(ns);
        }
    }

    final public void parse(String xpath) {
        int pos = 0;
        String n = findNode(xpath, pos);

        while (n != null) {
            Node node = new Node(n);
            list.add(node);
            pos += n.length();
            n = findNode(xpath, pos);
        }
    }

    private String findNode(String xpath, int pos) {
        if (pos >= xpath.length()) {
            return null;
        }
        int start = pos;
        int end = start;

        //忽略../、//、/
        while (ignore(xpath, end) && end < xpath.length()) {
            end++;
        }

        end = xpath.indexOf("/", end);

        if (end > 0) {
            return xpath.substring(start, end);
        } else {
            return xpath.substring(start);
        }
    }

    private boolean ignore(String xpath, int pos) {
        if (xpath.charAt(pos) == '.' || xpath.charAt(pos) == '/') {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Node n : list) {
            sb.append(n.toString());
        }
        return sb.toString();
    }

    public static void main(String args[]) {
        String xpath = "//*";
        XPathParser parser = new XPathParser(xpath);
        System.out.println(parser.getList());
        System.out.println(parser);
    }
}
