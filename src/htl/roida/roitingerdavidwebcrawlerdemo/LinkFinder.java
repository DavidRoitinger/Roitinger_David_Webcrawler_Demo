package htl.roida.roitingerdavidwebcrawlerdemo;

import org.htmlparser.Parser;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkFinder implements Runnable {

    private String url;
    private ILinkHandler linkHandler;
    
    private Statistic statistic = Statistic.getInstance();
    
    private static final long t0 = System.nanoTime();

    public LinkFinder(String url, ILinkHandler handler) {
        this.url = url;
        this.linkHandler = handler;
    }

    @Override
    public void run() {
        getSimpleLinks(url);
    }

    private void getSimpleLinks(String url) {

        if(!linkHandler.visited(url)){
            linkHandler.addVisited(url);
            Parser parser = null;
            try {
                //System.out.println(url);
                parser = new Parser(url);

                NodeList list = parser.parse(null);

                Pattern trimmer = Pattern.compile("(?<=\")http.*?(?=\")");
                Matcher m = trimmer.matcher(list.asString().replace("\n", ""));

                while(m.find()){
                    String link = m.group();
                    if(!linkHandler.visited(link)){
                        linkHandler.queueLink(link);
                    }

                }






                String[] words = Jsoup.parse(list.toHtml()).body().text().replace("\n", " ").replaceAll("[^a-zA-Zöäü\\s]","").split(" ");

                for (int i = 0; i < words.length; i++) {
                    statistic.putWordInMap(words[i]);
                }
                
                //System.out.println(linkHandler.size());
                //System.out.println(t0);
                //System.out.println(statistic.toString());


            } catch (Exception e) {
            }

        }



        // ToDo: Implement
        // 1. if url not already visited, visit url with linkHandler k
        // 2. get url and Parse Website k
        // 3. extract all URLs and add url to list of urls which should be visited
        //    only if link is not empty and url has not been visited before k?
        // 4. If size of link handler equals 500 -> print time elapsed for statistics

    }
}
