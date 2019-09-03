package com.gmail.dymitr.kuzmin;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.SQLException;

public class GramophoneAllegroScraper implements JScraper {
    @Override
    public void scraping() throws SQLException, IOException {
        String URL = "https://allegro.pl/kategoria/antyki-zabytki-techniki-47910?string=gramofon&bmatch=baseline-var-nbn-n-eyesa-bp-col-1-1-0619&order=p";
        JDBCMaria jdbcGramophon = new JDBCMaria("Gramophones");
        for (int pageNumber = 1; ; pageNumber++) {
            if (pageNumber > 1) {
                URL = URL.substring(0, URL.length() - 4);
                URL += "&p=" + pageNumber;
            }
            Connection.Response response = Jsoup.connect(URL).followRedirects(true).execute();
            if (pageNumber == 1 || response.url().toString().equals(URL)) {

                Document document = Jsoup.connect(URL).get();
                Elements links = document.select("a[href]");
                for (Element link : links) {
                    String linkTemp = link.attr("href");
                    String textTemp = link.text();
                    if (!textTemp.isEmpty() && !linkTemp.contains("search") && linkTemp.contains("oferta")
                            && !linkTemp.contains("igla") && !linkTemp.contains("emission_unit") && !linkTemp.contains("instrukcja") &&
                    !linkTemp.contains("wzmacniacz") && !linkTemp.contains("adapter") && !linkTemp.contains("plyt")) {
                        String decription = getDescription(linkTemp);
                        System.out.println("link : " + link.attr("href"));
                        System.out.println("(" + pageNumber + ") [Allegro] " + getPrice(decription) + " | " + getMark(linkTemp));
                        jdbcGramophon.addToDB(new Gramophone(linkTemp, (int) getPrice(decription), getMark(linkTemp)));
                    }
                }
            }
            else{
                jdbcGramophon.disconnect();
                break;
            }
        }
    }

    @Override
    public boolean isActual(String URL) {
        return false;
    }

    private String getMark(String linkTemp) throws IOException {
        Document doc = Jsoup.connect(linkTemp).get();
        String title = doc.title().substring(0, doc.title().length() - 23);
        title = title.replaceAll("'", "");
        return title;
    }


    private float getPrice(String description){
        try {
            return Float.valueOf(description.substring(26, description.indexOf("zł")).replaceAll(" ", "").replaceAll(",", "."));
        }catch (java.lang.NumberFormatException | java.lang.StringIndexOutOfBoundsException e){
            return 0.0f;
        }
    }

    private String getDescription(String URL) throws IOException {
        Document document = Jsoup.connect(URL).get();
        return document.select("meta[name=description]").get(0)
                .attr("content");
    }
}
