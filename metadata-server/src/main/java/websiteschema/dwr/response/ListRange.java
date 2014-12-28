/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.dwr.response;
import java.io.Serializable;
/**
 *
 * @author mupeng
 */
public class ListRange implements Serializable {
	private static final long serialVersionUID = 19L;

	private Object[] data;
	private Long totalSize;

	public Object[] getData() {
		return data;
	}

	public void setData(Object[] data) {
		this.data = data;
	}

	public Long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(Long totalSize) {
		this.totalSize = totalSize;
	}
}
