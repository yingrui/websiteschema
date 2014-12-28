/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.service;

import java.util.Map;
import websiteschema.dwr.response.ListRange;
import websiteschema.model.domain.User;
import websiteschema.persistence.rdbms.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import websiteschema.utils.MD5;
import static websiteschema.persistence.rdbms.utils.ParameterUtil.*;

/**
 *
 * @author ray
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public ListRange getResults(Map map) {
        ListRange listRange = new ListRange();
        Map params = buildParamWithInt(map, "start", "limit");
        listRange.setData(userMapper.getUsers(params).toArray());
        listRange.setTotalSize(userMapper.getTotalResults());
        return listRange;
    }

    public User getByUserId(String id) {
        return userMapper.getUserByUserId(id);
    }

    public User getById(long id) {
        return userMapper.getUserById(id);
    }

    @Transactional
    public void insert(User user) {
        String passwd = user.getPasswd();
        if (null == passwd || !"".equals(passwd)) {
            passwd = MD5.getMD5("123456".getBytes());
            user.setPasswd(passwd);
        }
        userMapper.insert(user);
    }

    @Transactional
    public void update(User user) {
        userMapper.update(user);
    }

    @Transactional
    public void deleteRecord(User user) {
        userMapper.delete(user);
    }
}
