/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NewsParamPanel.java
 *
 * Created on Dec 8, 2011, 11:16:17 PM
 */
package websiteschema.analyzer.browser.left;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import websiteschema.analyzer.context.BrowserContext;
import websiteschema.cluster.Clusterer;
import websiteschema.cluster.DocVectorConvertor;
import websiteschema.cluster.DocumentConvertor;
import websiteschema.cluster.analyzer.AnalysisResult;
import websiteschema.cluster.analyzer.fields.ContentExtractor;
import websiteschema.cluster.analyzer.fields.DateAnalyzer;
import websiteschema.cluster.analyzer.fields.StandardExtractor;
import websiteschema.cluster.analyzer.fields.TitleAnalyzer;
import websiteschema.cluster.analyzer.fields.XPathExtractor;
import websiteschema.crawler.Crawler;
import websiteschema.model.domain.Websiteschema;
import websiteschema.model.domain.cluster.Cluster;
import websiteschema.model.domain.cluster.ClusterModel;
import websiteschema.model.domain.cluster.DocVector;
import websiteschema.model.domain.cluster.Sample;
import websiteschema.model.domain.cralwer.CrawlerSettings;
import websiteschema.persistence.Mapper;
import websiteschema.utils.CollectionUtil;

/**
 *
 * @author ray
 */
public class NewsParamPanel extends javax.swing.JPanel implements ISiteAnalyzer {

    BrowserContext context;
    Websiteschema schema;
    IConfigureHandler confHandler;
    TitleAnalyzer titleAnalyzer = new TitleAnalyzer();
    DateAnalyzer dateAnalyzer = new DateAnalyzer();
    StandardExtractor sourceExtractor = new StandardExtractor();
    StandardExtractor authorExtractor = new StandardExtractor();
    XPathExtractor commentsExtractor = new XPathExtractor();
    XPathExtractor clicksExtractor = new XPathExtractor();
    XPathExtractor transmitExtractor = new XPathExtractor();
    XPathExtractor summaryExtractor = new XPathExtractor();
    XPathExtractor chnlExtractor = new XPathExtractor();
    ContentExtractor contentExtractor = new ContentExtractor();
    AnalysisResult ar = new AnalysisResult();

    /** Creates new form NewsParamPanel */
    public NewsParamPanel() {
        initComponents();
        titleAnalyzer.setFieldName("TITLE");
        dateAnalyzer.setFieldName("DATE");
        sourceExtractor.setFieldName("SOURCENAME");
        authorExtractor.setFieldName("AUTHOR");
        commentsExtractor.setFieldName("COMMENT_COUNT");
        clicksExtractor.setFieldName("CLICK_COUNT");
        transmitExtractor.setFieldName("TRANSMIT_COUNT");
        summaryExtractor.setFieldName("SUMMARY");
        chnlExtractor.setFieldName("CHANNEL");
        contentExtractor.setFieldName("CONTENT");
    }

    @Override
    public void setSiteId(String siteId) {
        this.siteIdLabel.setText(siteId);
        this.schema = (Websiteschema) BrowserContext.getSpringContext().
                getBean("websiteschemaMapper", Mapper.class).
                get(siteId);
    }

    private void analysis() {
        this.testButton.setEnabled(false);
        this.saveButton.setEnabled(false);
        try {
            classify();
            initProp();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            this.testButton.setEnabled(true);
            this.saveButton.setEnabled(true);
        }
    }

    private String getClusterName() {
        String clusterName = clusterField.getText();
        if (null != clusterName && !"".equals(clusterName)) {
            return clusterName;
        }
        return AnalysisResult.DefaultClusterName;
    }

    private void initProp() throws IOException {
        Map<String, String> prop = this.confHandler.getProperties(); //获取当前的配置
        //初始化参数
        ar.init(prop);
        this.contentExtractor.setBasicAnalysisResult(ar.getBasicAnalysisResult());
        this.contentExtractor.setXPathAttr(schema.getXpathAttr());
        //设置各个字段
        setTitle(ar.getFieldAnalysisResult(getClusterName(), titleAnalyzer.getFieldName()));
        setDate(ar.getFieldAnalysisResult(getClusterName(), dateAnalyzer.getFieldName()));
        setContent(ar.getFieldExtractorConfig(getClusterName(), contentExtractor.getFieldName()));
        setStandardExtractor(sourceExtractor,
                ar.getFieldExtractorConfig(getClusterName(), sourceExtractor.getFieldName()),
                sourceResultField, sourceXPathField, sourcePrefixField, sourceSuffixField);
        setStandardExtractor(authorExtractor,
                ar.getFieldExtractorConfig(getClusterName(), authorExtractor.getFieldName()),
                authorResultField, authorXPathField, authorPrefixField, authorSuffixField);
        setXPathExtractor(commentsExtractor,
                ar.getFieldExtractorConfig(getClusterName(), commentsExtractor.getFieldName()),
                commentResultField, commentXPathField);
        setXPathExtractor(clicksExtractor,
                ar.getFieldExtractorConfig(getClusterName(), clicksExtractor.getFieldName()),
                clickResultField, clickXPathField);
        setXPathExtractor(transmitExtractor,
                ar.getFieldExtractorConfig(getClusterName(), transmitExtractor.getFieldName()),
                transmitResultField, transmitXPathField);
        setXPathExtractor(summaryExtractor,
                ar.getFieldExtractorConfig(getClusterName(), summaryExtractor.getFieldName()),
                summaryResultField, summaryXPathField);
        setXPathExtractor(chnlExtractor,
                ar.getFieldExtractorConfig(getClusterName(), chnlExtractor.getFieldName()),
                channelResultField, channelXPathField);
        //
    }

    private void setStandardExtractor(StandardExtractor extractor, List<Map<String, String>> params,
            JTextField valueField, JTextField xpathField, JTextField prefixField, JTextField suffixField) {
        if (null != params && null != extractor) {
            for (Map<String, String> map : params) {
                Collection<String> set = extractAndDisplayStandardExtractor(extractor, map, valueField, xpathField, prefixField, suffixField);
                if (null != set && !set.isEmpty()) {
                    break;
                }
            }
        }
    }

    /**
     * 
     * @param map
     * @return
     */
    private Collection<String> extractAndDisplayStandardExtractor(StandardExtractor extractor, Map<String, String> map,
            JTextField valueField, JTextField xpathField, JTextField prefixField, JTextField suffixField) {
        extractor.init(map);
        Collection<String> set = extractor.extract(getDocument(), getDocumentSource());
        if (null != set && !set.isEmpty()) {
            valueField.setText(CollectionUtil.toString(set));
            xpathField.setText(map.get(StandardExtractor.xpathKey));
            prefixField.setText(map.get(StandardExtractor.prefixKey));
            suffixField.setText(map.get(StandardExtractor.suffixKey));
        }
        return set;
    }

    private void setXPathExtractor(XPathExtractor extractor, List<Map<String, String>> params,
            JTextField valueField, JTextField xpathField) {
        if (null != params && null != extractor) {
            for (Map<String, String> map : params) {
                Collection<String> set = extractAndDisplayXPathExtractor(extractor, map, valueField, xpathField);
                if (null != set && !set.isEmpty()) {
                    break;
                }
            }
        }
    }

    /**
     * 根据XPATH抽取内容，如果不为空则在界面上显示结果
     * @param map
     * @return
     */
    private Collection<String> extractAndDisplayXPathExtractor(XPathExtractor extractor, Map<String, String> map,
            JTextField valueField, JTextField xpathField) {
        extractor.init(map);
        Collection<String> set = extractor.extract(getDocument(), getDocumentSource());
        if (null != set && !set.isEmpty()) {
            valueField.setText(CollectionUtil.toString(set));
            xpathField.setText(map.get(XPathExtractor.xpathKey));
        }
        return set;
    }

    /**
     * 在界面上显示标题
     * @param params
     */
    private void setTitle(List<Map<String, String>> params) {
        if (null != params) {
            for (Map<String, String> map : params) {
                Collection<String> set = extractAndDisplayTitle(map);
                if (null != set && !set.isEmpty()) {
                    break;
                }
            }
        }
    }

    /**
     * 在界面上显示标题
     * @param params
     */
    private void setContent(List<Map<String, String>> params) {
        if (null != params) {
            for (Map<String, String> map : params) {
                Collection<String> set = extractAndDisplayContent(map);
                if (null != set && !set.isEmpty()) {
                    break;
                }
            }
        }
    }

    private Collection<String> extractAndDisplayContent(Map<String, String> map) {
        contentExtractor.init(map);
        Collection<String> set = contentExtractor.extract(getDocument(), getDocumentSource());
        if (null != set && !set.isEmpty()) {
            this.contentTextArea.setText(set.iterator().next());
            this.contentTextArea.setCaretPosition(0);
            this.contentStartField.setText(map.get(ContentExtractor.prefixKey));
            this.contentEndField.setText(map.get(ContentExtractor.suffixKey));
            this.contentXPathField.setText(map.get(ContentExtractor.xpathKey));
            this.contentStartXPathField.setText(map.get(ContentExtractor.startXPathKey));
            this.contentEndXPathField.setText(map.get(ContentExtractor.endXPathKey));
            this.validCheckBox.setSelected(contentExtractor.isIncludeValidNodeOnly());
            this.invalidCheckBox.setSelected(contentExtractor.isExcludeInvalidNode());
            this.keepHTMLTagCheckBox.setSelected(contentExtractor.isKeepHTMLTag());
        }
        return set;
    }

    private Map<String, String> getContentConfig() {
        Map<String, String> conf = new HashMap<String, String>();
        put(ContentExtractor.xpathKey, contentXPathField.getText(), conf);
        put(ContentExtractor.prefixKey, contentStartField.getText(), conf);
        put(ContentExtractor.suffixKey, contentEndField.getText(), conf);
        put(ContentExtractor.startXPathKey, contentStartXPathField.getText(), conf);
        put(ContentExtractor.endXPathKey, contentEndXPathField.getText(), conf);
        put(ContentExtractor.includeValidKey, String.valueOf(validCheckBox.isSelected()), conf);
        put(ContentExtractor.excludeInvalidKey, String.valueOf(invalidCheckBox.isSelected()), conf);
        put(ContentExtractor.keepHTMLTagKey, String.valueOf(keepHTMLTagCheckBox.isSelected()), conf);
        return conf;
    }

    /**
     * 抽取时间，如果不为空则在界面上显示结果
     * @param map
     * @return
     */
    private Collection<String> extractAndDisplayTitle(Map<String, String> map) {
        titleAnalyzer.init(map);
        Collection<String> set = titleAnalyzer.extract(getDocument(), getDocumentSource());
        if (null != set && !set.isEmpty()) {
            this.titleResultField.setText(set.iterator().next());
            this.titleXPathField.setText(map.get(TitleAnalyzer.xpathKey));
            this.titlePrefixField.setText(map.get(TitleAnalyzer.prefixKey));
            this.titleSuffixField.setText(map.get(TitleAnalyzer.suffixKey));
        }
        return set;
    }

    /**
     * 读取界面上日期的配置参数
     * @return
     */
    private Map<String, String> getTitleConfig() {
        Map<String, String> conf = new HashMap<String, String>();
        put(TitleAnalyzer.xpathKey, titleXPathField.getText(), conf);
        put(TitleAnalyzer.prefixKey, titlePrefixField.getText(), conf);
        put(TitleAnalyzer.suffixKey, titleSuffixField.getText(), conf);
        return conf;
    }

    /**
     * 在界面上显示时间
     * @param params
     */
    private void setDate(List<Map<String, String>> params) {
        if (null != params) {
            for (Map<String, String> map : params) {
                Collection<String> set = extractAndDisplayDate(map);
                if (null != set && !set.isEmpty()) {
                    break;
                }
            }
        }
    }

    /**
     * 抽取时间，如果不为空则在界面上显示结果
     * @param map
     * @return
     */
    private Collection<String> extractAndDisplayDate(Map<String, String> map) {
        dateAnalyzer.init(map);
        Collection<String> set = dateAnalyzer.extract(getDocument(), getDocumentSource());
        if (null != set && !set.isEmpty()) {
            String xpath = map.get(DateAnalyzer.xpathKey);
            String type = map.get(DateAnalyzer.typeKey);
            this.dateResultField.setText(set.iterator().next());
            this.dateXPathField.setText(map.get(DateAnalyzer.xpathKey));
            this.datePatternField.setText(map.get(DateAnalyzer.patternKey));
            this.dateFormatField.setText(map.get(DateAnalyzer.formatKey));
            if (null != type) {
                this.dateTypeCombo.setSelectedItem(type);
            } else {
                if (null != xpath) {
                    this.dateTypeCombo.setSelectedItem(DateAnalyzer.Type_XPath);
                }
            }
        }
        return set;
    }

    /**
     * 读取界面上日期的配置参数
     * @return
     */
    private Map<String, String> getDateConfig() {
        Map<String, String> conf = new HashMap<String, String>();
        put(DateAnalyzer.xpathKey, dateXPathField.getText(), conf);
        put(DateAnalyzer.patternKey, datePatternField.getText(), conf);
        put(DateAnalyzer.formatKey, dateFormatField.getText(), conf);
        put(DateAnalyzer.typeKey, dateTypeCombo.getSelectedItem().toString(), conf);
        return conf;
    }

    /**
     * 读取界面上日期的配置参数
     * @return
     */
    private Map<String, String> getStandardExtractorConfig(JTextField xpath, JTextField prefix, JTextField suffix) {
        Map<String, String> conf = new HashMap<String, String>();
        put(StandardExtractor.xpathKey, xpath.getText(), conf);
        put(StandardExtractor.prefixKey, prefix.getText(), conf);
        put(StandardExtractor.suffixKey, suffix.getText(), conf);
        return conf;
    }

    /**
     * 读取界面上的配置参数
     * @return
     */
    private Map<String, String> getXPathExtractorConfig(JTextField xpath) {
        Map<String, String> conf = new HashMap<String, String>();
        put(StandardExtractor.xpathKey, xpath.getText(), conf);
        return conf;
    }

    private void put(String key, String value, Map<String, String> map) {
        if (null != map) {
            if (null != key && !"".equals(key)) {
                if (null != value && !"".equals(value)) {
                    map.put(key, value);
                }
            }
        }
    }

    private void classify() {
        long start = System.currentTimeMillis();
        Mapper<ClusterModel> cmMapper = BrowserContext.getSpringContext().getBean("clusterModelMapper", Mapper.class);
        ClusterModel cm = cmMapper.get(getSiteId());
        long end = System.currentTimeMillis();
        context.getConsole().log("Get ClusterModel elapse: " + (end - start));
        if (null != cm) {
            String clustererType = cm.getClustererType();
            if (null != clustererType) {
                try {
                    Class clazz = Class.forName(clustererType);
                    Constructor ctor[] = clazz.getDeclaredConstructors();
                    Class cx[] = ctor[0].getParameterTypes();
                    // 通过反射创建Clusterer。其构造函数需要siteId;
                    Clusterer clusterer = (Clusterer) clazz.getConstructor(cx).newInstance(new Object[]{getSiteId()});
                    clusterer.init(cm);
                    this.statusLabel.setText("转换文档");
                    start = System.currentTimeMillis();
                    Sample sample = getCurrentDocument();
                    end = System.currentTimeMillis();
                    context.getConsole().log("Convert Document to Sample elapse: " + (end - start));
                    if (null != sample) {
                        DocVector vect = new DocVectorConvertor().convert(sample, cm.getStatInfo());
                        this.statusLabel.setText("开始分类");
                        start = System.currentTimeMillis();
                        Cluster cluster = clusterer.classify(sample);
                        end = System.currentTimeMillis();
                        context.getConsole().log("Classify elapse: " + (end - start));
                        if (null != cluster) {
                            double sim = clusterer.membershipDegree(vect, cluster.getCentralPoint());
                            setCluster(cluster, sim);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void setCluster(Cluster cluster, double sim) {
//        this.belongToLabel.setText(cluster.getCustomName());
        this.clusterField.setText(cluster.getCustomName());
        this.simLabel.setText(String.valueOf(sim));
    }

    private Sample getCurrentDocument() {
        Sample sample = null;

        Node doc = getDocument(context.getBrowser().getURL());
        if (null != doc && doc.getNodeType() == Node.DOCUMENT_NODE) {
            DocumentConvertor dc = new DocumentConvertor();
            dc.setXpathAttr(schema.getXpathAttr());
            sample = new Sample();
            sample.setUrl(context.getBrowser().getURL());
            sample.setSiteId(getSiteId());
            sample.setContent(dc.convertDocument((Document) doc));
        }

        return sample;
    }

    private Document getDocument(String url) {
        Mapper<Websiteschema> mapper = BrowserContext.getSpringContext().getBean("websiteschemaMapper", Mapper.class);
        Websiteschema ws = mapper.get(getSiteId());
        if (null != ws) {
            CrawlerSettings settings = ws.getCrawlerSettings();
            if (null != settings) {
                String crawlerType = settings.getCrawlerType();
                if (null == crawlerType) {
                    crawlerType = "websiteschema.crawler.SimpleHttpCrawler";
                }
                try {
                    Class clazz = Class.forName(crawlerType);
                    Crawler crawler = (Crawler) clazz.newInstance();
                    Document[] docs = crawler.crawl(url);
                    return docs[0];
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return getDocument();
    }

    private Document getDocument() {
        Document doc = (Document) context.getBrowser().getW3CDocument();
        return doc;
    }

    private String getDocumentSource() {
        return context.getBrowser().getDocument().getDocumentSource();
    }

    @Override
    public String getSiteId() {
        return this.siteIdLabel.getText();
    }

    @Override
    public void setBrowserContext(BrowserContext context) {
        this.context = context;
    }

    @Override
    public void setConfigureHandler(IConfigureHandler confHandler) {
        this.confHandler = confHandler;
    }

    @Override
    public void start() {
        new Thread(new FooThread()).start();
    }

    class FooThread implements Runnable {

        @Override
        public void run() {
            init();
        }
    };

    public void init() {
        this.statusLabel.setText("正在分析页面");
        analysis();
        this.statusLabel.setText("分析完毕");
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        siteIdLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        statusLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        titleResultField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        titleXPathField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        dateResultField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        dateTypeCombo = new javax.swing.JComboBox();
        dateXPathField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        datePatternField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        sourceResultField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        sourceXPathField = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel10 = new javax.swing.JLabel();
        authorResultField = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        authorXPathField = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        sourcePrefixField = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        sourceSuffixField = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        authorPrefixField = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        authorSuffixField = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel18 = new javax.swing.JLabel();
        commentResultField = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        commentXPathField = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        clickResultField = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        clickXPathField = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        dateFormatField = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        transmitResultField = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        transmitXPathField = new javax.swing.JTextField();
        jSeparator4 = new javax.swing.JSeparator();
        saveButton = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel25 = new javax.swing.JLabel();
        simLabel = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        contentStartField = new javax.swing.JTextField();
        contentEndField = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        summaryXPathField = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        channelXPathField = new javax.swing.JTextField();
        testButton = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        titlePrefixField = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        titleSuffixField = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        summaryResultField = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        channelResultField = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        contentStartXPathField = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        contentEndXPathField = new javax.swing.JTextField();
        validCheckBox = new javax.swing.JCheckBox();
        invalidCheckBox = new javax.swing.JCheckBox();
        jSeparator7 = new javax.swing.JSeparator();
        keepHTMLTagCheckBox = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        contentTextArea = new javax.swing.JTextArea();
        clusterField = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        contentXPathField = new javax.swing.JTextField();

        jLabel1.setText("网站Id:");

        siteIdLabel.setText("${siteId_here}");

        jLabel2.setText("属于类:");

        statusLabel.setText(" ");

        jLabel3.setText("标题:  ");

        jLabel4.setText("标题XPath: ");

        jLabel5.setText("发布时间: ");

        jLabel6.setText("时间抽取来自: ");

        dateTypeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "XPath", "URL", "HTTP" }));

        jLabel7.setText("模式: ");

        jLabel8.setText("来源: ");

        jLabel9.setText("来源XPath: ");

        jLabel10.setText("作者: ");

        jLabel11.setText("作者XPath: ");

        jLabel12.setText("抽取规则: ");

        jLabel13.setText("抽取规则: ");

        jLabel14.setText("(Prefix)");

        jLabel15.setText("(Suffix)");

        jLabel16.setText("(Prefix)");

        jLabel17.setText("(Suffix)");

        jLabel18.setText("评论数:");

        jLabel19.setText("XPath:");

        jLabel20.setText("点击量:");

        jLabel21.setText("XPath:");

        jLabel22.setText("目标格式:");

        dateFormatField.setText("yyyy-MM-dd HH:mm:SS");

        jLabel23.setText("转发数:");

        jLabel24.setText("XPath:");

        saveButton.setText("保存");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        jLabel25.setText("相似度: ");

        simLabel.setText("${similarity}");

        jLabel27.setText("开始字符:");

        jLabel28.setText("结束字符:");

        jLabel29.setText("正文抽取:");

        jLabel30.setText("摘要XPath:");

        jLabel31.setText("栏目XPath:");

        testButton.setText("测试");
        testButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testButtonActionPerformed(evt);
            }
        });

        jLabel32.setText("标题起始位置:");

        jLabel33.setText("标题结束位置:");

        jLabel26.setText("摘要:");

        jLabel34.setText("栏目:");

        jLabel35.setText("开始标签:");

        jLabel36.setText("结束标签:");

        validCheckBox.setText("仅包含有效节点");

        invalidCheckBox.setText("不包含无效节点");

        keepHTMLTagCheckBox.setText("保留HTML标签");

        contentTextArea.setColumns(20);
        contentTextArea.setRows(5);
        jScrollPane1.setViewportView(contentTextArea);

        jLabel37.setText("内容XPath:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE)
                    .addComponent(jSeparator6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE)
                    .addComponent(jSeparator7, javax.swing.GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel32))
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(titleXPathField, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(titlePrefixField, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel33)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(titleSuffixField, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(titleResultField, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(clusterField, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(siteIdLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel25)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(simLabel))
                                    .addComponent(statusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)))))
                    .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(dateResultField, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(datePatternField, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dateFormatField, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(dateTypeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dateXPathField, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel12))
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sourcePrefixField, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sourceSuffixField, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(sourceResultField, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sourceXPathField, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel10))
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(authorResultField, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(authorXPathField, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(authorPrefixField, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(authorSuffixField, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE))))
                    .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE)
                    .addComponent(jSeparator4, javax.swing.GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel23)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(transmitResultField))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(clickResultField, 0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(commentResultField, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel24)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(transmitXPathField, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(commentXPathField, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(clickXPathField, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(channelXPathField, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(channelResultField, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel30)
                        .addGap(18, 18, 18)
                        .addComponent(summaryXPathField, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(summaryResultField, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel27)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(contentStartField, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel28)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(contentEndField, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(invalidCheckBox)
                                .addGap(18, 18, 18)
                                .addComponent(validCheckBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(keepHTMLTagCheckBox))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(saveButton)
                            .addComponent(testButton)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel37)
                            .addComponent(jLabel35))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(contentStartXPathField, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel36)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(contentEndXPathField, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE))
                            .addComponent(contentXPathField, javax.swing.GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(siteIdLabel)
                    .addComponent(statusLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(simLabel)
                    .addComponent(jLabel25)
                    .addComponent(clusterField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(titleResultField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(titleXPathField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(titlePrefixField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(titleSuffixField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(dateResultField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(dateFormatField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(datePatternField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(dateTypeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateXPathField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(sourceResultField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(sourceXPathField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel14)
                    .addComponent(sourcePrefixField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(sourceSuffixField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(authorXPathField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(authorResultField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(authorPrefixField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(authorSuffixField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(commentResultField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(commentXPathField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(clickResultField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21)
                    .addComponent(clickXPathField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(transmitResultField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24)
                    .addComponent(transmitXPathField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(contentEndField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28)
                    .addComponent(contentStartField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(validCheckBox)
                    .addComponent(invalidCheckBox)
                    .addComponent(keepHTMLTagCheckBox))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(contentXPathField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(jLabel35)
                    .addComponent(contentStartXPathField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contentEndXPathField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(jLabel26)
                    .addComponent(summaryResultField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(summaryXPathField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel31)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(channelXPathField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(channelResultField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel34)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(testButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveButton))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void testButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testButtonActionPerformed
        // TODO add your handling code here:
        new Thread(new Runnable() {

            @Override
            public void run() {
                test();
            }
        }).start();
    }//GEN-LAST:event_testButtonActionPerformed

    private void test() {
        this.statusLabel.setText("正在分析页面");
        testButton.setEnabled(false);
        try {
            extractAndDisplayTitle(getTitleConfig());
            extractAndDisplayDate(getDateConfig());
            extractAndDisplayStandardExtractor(
                    sourceExtractor,
                    getStandardExtractorConfig(sourceXPathField, sourcePrefixField, sourceSuffixField),
                    sourceResultField, sourceXPathField, sourcePrefixField, sourceSuffixField);
            extractAndDisplayStandardExtractor(
                    authorExtractor,
                    getStandardExtractorConfig(authorXPathField, authorPrefixField, authorSuffixField),
                    authorResultField, authorXPathField, authorPrefixField, authorSuffixField);
            extractAndDisplayXPathExtractor(commentsExtractor, getXPathExtractorConfig(commentXPathField), commentResultField, commentXPathField);
            extractAndDisplayXPathExtractor(clicksExtractor, getXPathExtractorConfig(clickXPathField), clickResultField, clickXPathField);
            extractAndDisplayXPathExtractor(transmitExtractor, getXPathExtractorConfig(transmitXPathField), transmitResultField, transmitXPathField);
            extractAndDisplayXPathExtractor(summaryExtractor, getXPathExtractorConfig(summaryXPathField), summaryResultField, summaryXPathField);
            extractAndDisplayXPathExtractor(chnlExtractor, getXPathExtractorConfig(channelXPathField), channelResultField, channelXPathField);
            extractAndDisplayContent(getContentConfig());
        } finally {
            this.statusLabel.setText("分析完毕");
            testButton.setEnabled(true);
        }
    }

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        // TODO add your handling code here:
        this.statusLabel.setText("正在保存设置");
        ar.setFieldAnalysisResult(getClusterName(), titleAnalyzer.getFieldName(), titleAnalyzer.getClass().getName(), getTitleConfig());
        ar.setFieldAnalysisResult(getClusterName(), dateAnalyzer.getFieldName(), dateAnalyzer.getClass().getName(), getDateConfig());
        ar.setFieldExtractorSetting(getClusterName(),
                sourceExtractor.getFieldName(),
                sourceExtractor.getClass().getName(),
                getStandardExtractorConfig(sourceXPathField, sourcePrefixField, sourceSuffixField));
        ar.setFieldExtractorSetting(getClusterName(),
                authorExtractor.getFieldName(),
                authorExtractor.getClass().getName(),
                getStandardExtractorConfig(authorXPathField, authorPrefixField, authorSuffixField));
        ar.setFieldExtractorSetting(getClusterName(),
                commentsExtractor.getFieldName(),
                commentsExtractor.getClass().getName(),
                getXPathExtractorConfig(commentXPathField));
        ar.setFieldExtractorSetting(getClusterName(),
                clicksExtractor.getFieldName(),
                clicksExtractor.getClass().getName(),
                getXPathExtractorConfig(clickXPathField));
        ar.setFieldExtractorSetting(getClusterName(),
                transmitExtractor.getFieldName(),
                transmitExtractor.getClass().getName(),
                getXPathExtractorConfig(transmitXPathField));
        ar.setFieldExtractorSetting(getClusterName(),
                summaryExtractor.getFieldName(),
                summaryExtractor.getClass().getName(),
                getXPathExtractorConfig(summaryXPathField));
        ar.setFieldExtractorSetting(getClusterName(),
                chnlExtractor.getFieldName(),
                chnlExtractor.getClass().getName(),
                getXPathExtractorConfig(channelXPathField));
        ar.setFieldExtractorSetting(getClusterName(),
                contentExtractor.getFieldName(),
                contentExtractor.getClass().getName(),
                getContentConfig());

        this.confHandler.setProperties(ar.getResult());
        this.confHandler.save();
        this.statusLabel.setText("保存完毕");
        JOptionPane.showMessageDialog(this, "Crawler设置保存成功！");
    }//GEN-LAST:event_saveButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField authorPrefixField;
    private javax.swing.JTextField authorResultField;
    private javax.swing.JTextField authorSuffixField;
    private javax.swing.JTextField authorXPathField;
    private javax.swing.JTextField channelResultField;
    private javax.swing.JTextField channelXPathField;
    private javax.swing.JTextField clickResultField;
    private javax.swing.JTextField clickXPathField;
    private javax.swing.JTextField clusterField;
    private javax.swing.JTextField commentResultField;
    private javax.swing.JTextField commentXPathField;
    private javax.swing.JTextField contentEndField;
    private javax.swing.JTextField contentEndXPathField;
    private javax.swing.JTextField contentStartField;
    private javax.swing.JTextField contentStartXPathField;
    private javax.swing.JTextArea contentTextArea;
    private javax.swing.JTextField contentXPathField;
    private javax.swing.JTextField dateFormatField;
    private javax.swing.JTextField datePatternField;
    private javax.swing.JTextField dateResultField;
    private javax.swing.JComboBox dateTypeCombo;
    private javax.swing.JTextField dateXPathField;
    private javax.swing.JCheckBox invalidCheckBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JCheckBox keepHTMLTagCheckBox;
    private javax.swing.JButton saveButton;
    private javax.swing.JLabel simLabel;
    private javax.swing.JLabel siteIdLabel;
    private javax.swing.JTextField sourcePrefixField;
    private javax.swing.JTextField sourceResultField;
    private javax.swing.JTextField sourceSuffixField;
    private javax.swing.JTextField sourceXPathField;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JTextField summaryResultField;
    private javax.swing.JTextField summaryXPathField;
    private javax.swing.JButton testButton;
    private javax.swing.JTextField titlePrefixField;
    private javax.swing.JTextField titleResultField;
    private javax.swing.JTextField titleSuffixField;
    private javax.swing.JTextField titleXPathField;
    private javax.swing.JTextField transmitResultField;
    private javax.swing.JTextField transmitXPathField;
    private javax.swing.JCheckBox validCheckBox;
    // End of variables declaration//GEN-END:variables
}
