/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.model.domain.cluster;

import java.util.List;

/**
 *
 * @author ray
 */
public class DocVector {// 能够代表一个文档的数字化序列，便于做聚类分析

    Dimension[] dims;
    String name;//rowkey

    public void append(List<Dimension> array) {
        int pos = 0;
        Dimension[] tmp = null;
        if (null != dims) {
            pos = this.dims.length;
            tmp = new Dimension[pos + array.size()];
            System.arraycopy(this.dims, 0, tmp, 0, pos);
        } else {
            tmp = new Dimension[array.size()];
        }
        for (int i = 0; i < array.size(); i++) {
            tmp[pos + i] = array.get(i);
        }
        this.dims = tmp;
    }

    public Dimension[] getDims() {
        return dims;
    }

    public void setDims(Dimension[] dims) {
        this.dims = dims;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
