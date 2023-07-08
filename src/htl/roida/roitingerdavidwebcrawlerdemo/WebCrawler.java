package htl.roida.roitingerdavidwebcrawlerdemo;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class WebCrawler implements ILinkHandler {

    private final int TARGET_SIZE = 3000;
    private final int SEARCH_TIME = 100;

    private final Collection<String> visitedLinks = Collections.synchronizedSet(new HashSet<String>());
    //    private final Collection<String> visitedLinks = Collections.synchronizedList(new ArrayList<String>());
    private String url;
    private ExecutorService execService;

    public WebCrawler(String startingURL, int maxThreads) {
        this.url = startingURL;
        execService = Executors.newFixedThreadPool(maxThreads);
    }

    @Override
    public void queueLink(String link) throws Exception {
        if(size()>=TARGET_SIZE){
            execService.shutdownNow();
            System.out.println(Thread.currentThread());
            throw new Exception();
        }
        startNewThread(link);
    }

    @Override
    public int size() {
        return visitedLinks.size();
    }

    @Override
    public void addVisited(String s) {
        visitedLinks.add(s);
    }

    @Override
    public boolean visited(String s) {
        return visitedLinks.contains(s);
    }

    private void startNewThread(String link) throws Exception {
        execService.submit(new LinkFinder(link, this));
    }

    private void startCrawling() throws Exception {
        startNewThread(this.url);
        execService.awaitTermination(SEARCH_TIME, TimeUnit.SECONDS);
        System.out.println(Statistic.getInstance().toString());
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        new WebCrawler("http://www.orf.at", 64).startCrawling();
    }
}