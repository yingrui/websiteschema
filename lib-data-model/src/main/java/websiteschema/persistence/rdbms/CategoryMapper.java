/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.rdbms;

import java.util.List;
import java.util.Map;
import websiteschema.model.domain.Category;

/**
 *
 * @author ray
 */
public interface CategoryMapper {

    public long getTotalResults(Map map);

    public List<Category> getCategories(Map map);

    public List<Category> getChildCategories(long parentId);

    public List<Category> getAllCategories();

    public Category getById(long id);

    public void setHasLeaf(long id);

    public void insert(Category category);

    public void update(Category category);

    public void delete(Category category);

    public void deleteById(long id);
}
