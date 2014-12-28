/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apc.websiteschema.fms.mapper;

import com.apc.websiteschema.fms.FMSJob;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ray
 */
public interface FMSJobMapper {

    public long getTotalResults(Map params);

    public List<FMSJob> getFMSJobs(Map params);

    public FMSJob getById(long id);
}
