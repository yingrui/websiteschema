/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.service;

import websiteschema.model.domain.Cipher;
import websiteschema.persistence.rdbms.CipherMapper;
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
public class CipherService {

    @Autowired
    private CipherMapper cipherMapper;

    public ListRange getResults(Map map) {
        ListRange listRange = new ListRange();
        Map params = buildParamWithInt(map, "start", "limit");
        listRange.setData(cipherMapper.getCiphers(params).toArray());
        listRange.setTotalSize(cipherMapper.getTotalResults(params));
        System.out.println(listRange.getTotalSize());
        return listRange;
    }

    public Cipher getById(long id) {
        return cipherMapper.getById(id);
    }

    @Transactional
    public void insert(Cipher cipher) {
        cipher.setCreateTime(new Date());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        cipher.setCreateUser(userDetails.getUsername());
        cipher.setUpdateTime(cipher.getCreateTime());
        cipher.setLastUpdateUser(cipher.getCreateUser());
        cipherMapper.insert(cipher);
    }

    @Transactional
    public void update(Cipher cipher) {
        cipher.setUpdateTime(new Date());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        cipher.setLastUpdateUser(userDetails.getUsername());
        cipherMapper.update(cipher);
    }

    @Transactional
    public void deleteRecord(Cipher cipher) {
        cipherMapper.delete(cipher);
    }
}
