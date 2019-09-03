package com.gmail.dymitr.kuzmin;

import java.io.IOException;
import java.sql.SQLException;

public class main {

    public static void main(String[] args) throws SQLException, IOException {
        //new GramophoneOLXScraper().scraping();
        //new GramophoneAllegroScraper().scraping();
        new JDBCMaria("Gramophones").deleteTrash();
    }
}
