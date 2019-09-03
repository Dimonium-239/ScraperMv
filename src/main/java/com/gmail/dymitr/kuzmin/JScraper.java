package com.gmail.dymitr.kuzmin;

import java.io.IOException;
import java.sql.SQLException;

/**
 * The interface for making simple scraper-robots for
 * internet shops or social networks.
 * WEB-site with which this scrapper working must have list
 * with
 *
 * @author dimonium_239
 */

public interface JScraper {

    /**
     * Get HTML page from link and get basic information from
     * from WEB-site by tags.
     *
     */
    void scraping() throws SQLException, IOException;

    /**
     * Check is link actual or not
     *
     * @param URL
     *          URL for checkin is it actual.
     * @return true is link is actual and false if link return error 404.
     */
    boolean isActual(String URL);
}
