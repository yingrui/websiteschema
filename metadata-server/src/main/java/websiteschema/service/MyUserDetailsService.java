/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.service;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import websiteschema.model.domain.User;
import websiteschema.persistence.rdbms.UserMapper;

/**
 *
 * @author ray
 */
@Service("myUserDetailsService")
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public UserDetails loadUserByUsername(String username)
            throws DataAccessException {
        User user = userMapper.getUserByUserId(username);
        if (null != user) {
            System.out.println(user.getUserId() + " " + user.getRole());

            Set<GrantedAuthority> auth = new HashSet<GrantedAuthority>();

            String roles[] = user.getRole().split(",");
            for (String r : roles) {
                auth.add(new GrantedAuthorityImpl(r.trim()));
            }

            return new org.springframework.security.core.userdetails.User(
                    user.getUserId(),
                    user.getPasswd(),
                    true,
                    true,
                    true,
                    true,
                    auth);
        } else {
            return new org.springframework.security.core.userdetails.User(
                    username,
                    "",
                    true,
                    true,
                    true,
                    true,
                    new HashSet<GrantedAuthority>());
        }
    }
}
