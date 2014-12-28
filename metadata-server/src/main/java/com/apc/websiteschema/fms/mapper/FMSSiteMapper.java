/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apc.websiteschema.fms.mapper;

import com.apc.websiteschema.fms.FMSSite;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mgd
 */
public interface FMSSiteMapper {

    public long getTotalResults(Map params);

    public List<FMSSite> getFMSSites(Map params);

    public FMSSite getById(long id);
}
