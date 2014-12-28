/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.model.domain;

/**
 *
 * @author ray
 */
public interface TreeNode {

    public String getName();

    public long getId();

    public String getDescription();

    public boolean isLeafNode();

    public long getParentId();

}
