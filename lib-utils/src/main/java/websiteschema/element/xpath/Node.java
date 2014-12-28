/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.element.xpath;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.lang.System.*;

/**
 *
 * @author ray
 */
public class Node {

    public static int TYPE_ELEMENT = 0;
    public static int TYPE_ATTR = 1;
    public static int TYPE_INSTRUCTION = 2;
    String path = "";
    String nodeName;
    int nodeType;
    String predicates;
    String namespace = "pre";
    boolean toLowerCase = false;
    final static Pattern patElement = Pattern.compile("((\\w+):)?([\\w\\*]+)(\\[(.+)\\])?");
    final static Pattern patAttr = Pattern.compile("@.+");
    final static Pattern patInstruction = Pattern.compile(".+\\)");

    public Node(String node) {
        String n = node;
        if (node.startsWith("//")) {
            path = "//";
            n = n.substring(2);
        } else if (node.startsWith("/")) {
            path = "/";
            n = n.substring(1);
        } else if (n.startsWith(".")) {
            int start = n.lastIndexOf("/");
            if (start > 0) {
                path = n.substring(0, start + 1);
                n = n.substring(start + 1);
            }
        }
        parseNode(n);
    }

    private void parseNode(String node) {
        Matcher m = patElement.matcher(node);
        if (m.matches()) {
            this.nodeType = TYPE_ELEMENT;
            String ns = m.group(2);
            if (null != ns) {
                this.namespace = ns;
            }
            String name = m.group(3);
            if (null != name) {
                this.nodeName = name;
            }
            String pred = m.group(5);
            if (null != pred) {
                this.predicates = pred;
            }
        } else {
            m = patAttr.matcher(node);
            if (m.matches()) {
                this.nodeType = TYPE_ATTR;
                this.nodeName = node;
            } else {
                m = patInstruction.matcher(node);
                if (m.matches()) {
                    this.nodeType = TYPE_INSTRUCTION;
                    this.nodeName = node;
                }
            }
        }
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getNodeName() {
        if (toLowerCase) {
            return nodeName.toLowerCase();
        } else {
            return nodeName;
        }
    }

    public int getNodeType() {
        return nodeType;
    }

    public String getPredicates() {
        return predicates;
    }

    public String getPath() {
        return path;
    }

    public boolean isToLowerCase() {
        return toLowerCase;
    }

    public void setToLowerCase(boolean toLowerCase) {
        this.toLowerCase = toLowerCase;
    }

    @Override
    public String toString() {
        if (getNodeType() == TYPE_ATTR) {
            return getPath() + getNodeName();
        } else if (getNodeType() == TYPE_ELEMENT) {
            StringBuilder ret = new StringBuilder();
            ret.append(getPath());
            if(!getNodeName().startsWith("*")) {
                ret.append(getNamespace()).append(":");
            }
            ret.append(getNodeName());
            if (null != getPredicates()) {
                ret.append("[").append(getPredicates()).append("]");
            }
            return ret.toString();
        } else if (getNodeType() == TYPE_INSTRUCTION) {
            return getPath() + getNodeName();
        }
        return getPath() + getNodeName();
    }

    public static void main(String args[]) {
        String nodeStr = "../bean:dateSource1[a() AND b='1sda' | c=\"asd]";
        Node n = new Node(nodeStr);
        out.println("Type: " + n.getNodeType());
        out.println("Path: " + n.path);
        out.println("谓词: " + n.predicates);
        out.println("XPath: " + n);
        nodeStr = "//@id";
        n = new Node(nodeStr);
        out.println("Type: " + n.getNodeType());
        out.println("Path: " + n.path);
        out.println("谓词: " + n.predicates);
        out.println("XPath: " + n);
        nodeStr = ".././text()";
        n = new Node(nodeStr);
        out.println("Type: " + n.getNodeType());
        out.println("Path: " + n.path);
        out.println("谓词: " + n.predicates);
        out.println("XPath: " + n);
        nodeStr = "//*";
        n = new Node(nodeStr);
        out.println("Type: " + n.getNodeType());
        out.println("Path: " + n.path);
        out.println("谓词: " + n.predicates);
        out.println("XPath: " + n);
    }
}
