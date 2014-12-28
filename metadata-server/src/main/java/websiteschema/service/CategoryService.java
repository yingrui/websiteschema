/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.service;

import java.util.List;
import websiteschema.model.domain.Category;
import websiteschema.persistence.rdbms.CategoryMapper;
import java.util.Map;
import java.util.Date;
import websiteschema.dwr.response.ListRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static websiteschema.persistence.rdbms.utils.ParameterUtil.*;

/**
 *
 * @author ray
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public ListRange getResults(Map map) {
        ListRange listRange = new ListRange();
        Map params = buildParamWithInt(map, "start", "limit");
        listRange.setData(categoryMapper.getCategories(params).toArray());
        listRange.setTotalSize(categoryMapper.getTotalResults(params));
        return listRange;
    }

    public Category getById(long id) {
        return categoryMapper.getById(id);
    }

    @Transactional
    public void insert(Category cate) {
        cate.setCreateTime(new Date());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        cate.setCreateUser(userDetails.getUsername());
        cate.setCreateTime(new Date());
        long parentId = cate.getParentId();
        if (parentId > 0) {
            categoryMapper.setHasLeaf(parentId);
        }
        categoryMapper.insert(cate);
    }

    @Transactional
    public void update(Category cate) {
        cate.setUpdateTime(new Date());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        cate.setLastUpdateUser(userDetails.getUsername());
        cate.setUpdateTime(new Date());
        categoryMapper.update(cate);
    }

    @Transactional
    public void deleteRecord(Category cate) {
        deleteDescendant(cate.getId());
        categoryMapper.delete(cate);
    }

    @Transactional
    public void deleteById(long id) {
        if (id > 0) {
            deleteDescendant(id);
            categoryMapper.deleteById(id);
        }
    }

    /**
     * 删除此站点的后代，但不包括此站点
     * @param site
     */
    private void deleteDescendant(long id) {
        List<Category> children = categoryMapper.getChildCategories(id);
        if (null != children) {
            for (Category child : children) {
                deleteDescendant(child.getId());
                categoryMapper.delete(child);
            }
        }

    }
}
