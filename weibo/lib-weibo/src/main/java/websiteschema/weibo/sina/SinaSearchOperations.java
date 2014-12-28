/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.weibo.sina;

import java.util.List;
import org.springframework.social.twitter.api.SavedSearch;
import org.springframework.social.twitter.api.SearchOperations;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Trends;
import weibo4j.Weibo;

/**
 *
 * @author ray
 */
public class SinaSearchOperations implements SearchOperations {

    Weibo weibo;

    public SinaSearchOperations(Weibo weibo) {
        this.weibo = weibo;
    }

    @Override
    public SearchResults search(String query) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SearchResults search(String query, int page, int pageSize) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SearchResults search(String query, int page, int pageSize, long sinceId, long maxId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<SavedSearch> getSavedSearches() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SavedSearch getSavedSearch(long searchId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void createSavedSearch(String query) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteSavedSearch(long searchId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Trends> getDailyTrends() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Trends> getDailyTrends(boolean excludeHashtags) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Trends> getDailyTrends(boolean excludeHashtags, String startDate) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Trends> getWeeklyTrends() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Trends> getWeeklyTrends(boolean excludeHashtags) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Trends> getWeeklyTrends(boolean excludeHashtags, String startDate) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Trends getLocalTrends(long whereOnEarthId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Trends getLocalTrends(long whereOnEarthId, boolean excludeHashtags) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Trends getCurrentTrends() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Trends getCurrentTrends(boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
