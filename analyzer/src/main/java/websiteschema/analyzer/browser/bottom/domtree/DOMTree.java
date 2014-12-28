/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.analyzer.browser.bottom.domtree;

import com.webrenderer.swing.dom.IElement;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author ray
 */
public class DOMTree extends JTree {

    public DOMTree(IElement graphNode) {
        super(new DOMTreeModel(new DOMTreeNode(graphNode)));
        getSelectionModel().setSelectionMode(
                TreeSelectionModel.SINGLE_TREE_SELECTION);
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        Icon personIcon = null;
        renderer.setLeafIcon(personIcon);
        renderer.setClosedIcon(personIcon);
        renderer.setOpenIcon(personIcon);
        setCellRenderer(renderer);
    }

    /**
     * Get the selected item in the tree, and call showAncestor with this
     * item on the model.
     */
    public void showAncestor(boolean b) {
        Object newRoot = null;
        TreePath path = getSelectionModel().getSelectionPath();
        if (path != null) {
            newRoot = path.getLastPathComponent();
        }
        ((DOMTreeModel) getModel()).showAncestor(b, newRoot);
    }
}
