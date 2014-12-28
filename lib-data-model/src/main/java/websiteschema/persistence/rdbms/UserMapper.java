/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.rdbms;

import java.util.List;
import java.util.Map;
import websiteschema.model.domain.User;

/**
 *
 * @author ray
 */
public interface UserMapper {

    public long getTotalResults();

    public List<User> getUsers(Map params);

    public User getUserById(long id);

    public User getUserByUserId(String userId);

    public User getUserByName(String name);

    public void update(User user);

    public void insert(User user);

    public void delete(User user);

    public void deleteByUserId(String userId);
}
