package com.gmail.dymitr.kuzmin;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.SQLException;

public class GramophoneOLXScraper implements JScraper {

    @Override
    public void scraping() throws SQLException, IOException {
        String URL = "https://www.olx.pl/elektronika/q-gramofon/?search%5Border%5D=filter_float_price%3Aasc";
        JDBCMaria jdbcGramophon = new JDBCMaria("Gramophones");
        for (int pageNumber = 1; ;pageNumber++) {
            URL += "&page=" + pageNumber;
            Connection.Response response = Jsoup.connect(URL).followRedirects(true).execute();
            if(pageNumber == 1 || response.url().toString().equals(URL)) {
                Document document = Jsoup.connect(URL).get();
                Elements links = document.select("a[href]");

                for (Element link : links) {
                    String linkTemp = link.attr("href");
                    String textTemp = link.text();
                    if (!textTemp.isEmpty() && !linkTemp.contains("search") && linkTemp.contains("oferta") && linkTemp.contains("gramofon")
                            && !linkTemp.contains("gramofonowy") && !linkTemp.contains("gramofonu") && !linkTemp.contains("do-gramofon")
                            && !linkTemp.contains("sprzet") && !linkTemp.contains("adapter")) {
                        String decription = getDescription(linkTemp);
                        System.out.println("link : " + link.attr("href"));
                        System.out.println("(" + pageNumber + ") [OLX] " + getPrice(decription) + " | " + getTitle(linkTemp));
                        jdbcGramophon.addToDB(new Gramophone(linkTemp, getPrice(decription), getTitle(linkTemp)));
                    }
                }
            }else{
                jdbcGramophon.disconnect();
                break;
            }
        }
    }

    @Override
    public boolean isActual(String URL) {
        return false;
    }

    private int getPrice(String description){
        try {
            description = description.replaceAll(" ", "");
            String price = description.substring(0, description.indexOf("z"));
            return Integer.valueOf(price);
        }catch (java.lang.NumberFormatException | StringIndexOutOfBoundsException e){
            return 0;
        }
    }

    private String getTitle(String URL) throws IOException {
        Document document = Jsoup.connect(URL).get();
        return document.select("meta[property=og:title]").get(0)
                .attr("content");
    }

    private String getDescription(String URL) throws IOException {
        Document document = Jsoup.connect(URL).get();
        return document.select("meta[name=description]").get(0)
                .attr("content");
    }
}
