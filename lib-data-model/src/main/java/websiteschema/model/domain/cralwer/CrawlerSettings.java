/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.model.domain.cralwer;

/**
 *
 * @author ray
 */
public class CrawlerSettings {

    String[] dontHave;
    String[] mustHave;
    String[] acceptedType;
    String encoding;
    String crawlerType;
    int maxDepth = 0;
    int minDate = -1;
    int maxDate = 3;

    public String[] getAcceptedType() {
        return acceptedType;
    }

    public void setAcceptedType(String[] acceptedType) {
        this.acceptedType = acceptedType;
    }

    public String[] getDontHave() {
        return dontHave;
    }

    public void setDontHave(String[] dontHave) {
        this.dontHave = dontHave;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public int getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(int maxDate) {
        this.maxDate = maxDate;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public int getMinDate() {
        return minDate;
    }

    public void setMinDate(int minDate) {
        this.minDate = minDate;
    }

    public String[] getMustHave() {
        return mustHave;
    }

    public void setMustHave(String[] mustHave) {
        this.mustHave = mustHave;
    }

    public String getCrawlerType() {
        return crawlerType;
    }

    public void setCrawlerType(String crawlerType) {
        this.crawlerType = crawlerType;
    }
}
