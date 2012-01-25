package com.googlecode.jstdmavenplugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.IncorrectnessListener;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Main {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static void main(String[] args) throws Exception {
        new Main();
    }

    public Main() throws Exception {

        String url = "http://localhost:42442/capture";
        boolean debug = true;

        String browserVersion = "FIREFOX_3_6";
        //TODO - this class has numerous reasons to change: configuring a web client and running specs. extract at least one class
        BrowserVersion htmlUnitBrowserVersion = (BrowserVersion) BrowserVersion.class.getField(browserVersion).get(
                BrowserVersion.class);
        WebClient webClient = new WebClient(htmlUnitBrowserVersion);
        webClient.setJavaScriptEnabled(true);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        if (!debug) {
            quietIncorrectnessListener(webClient);
        }
        HtmlPage page = webClient.getPage(url);

        waitForRunnerToFinish(page, 420000, true);

        webClient.closeAllWindows();
    }

    private void waitForRunnerToFinish(HtmlPage page, int timeout, boolean debug) throws InterruptedException {
        page.getWebClient().waitForBackgroundJavaScript(5000);
        for (int i = 0; i < timeout; i++) {
            if (executionFinished(page)) {
                return;
            } else {
                synchronized (page) {
                    page.wait(1000);
                }
            }
        }
        if (!executionFinished(page)) {
            handleTimeout(timeout, debug);
        }
    }

    private void handleTimeout(int timeout, boolean debug) {
        log.warn("Attempted to wait for your specs to finish processing over the course of " + timeout
                + " seconds, but it still appears to be running.");
        if (debug) {
            log.warn("Debug mode: will attempt to parse the incomplete spec runner results");
        } else {
            throw new IllegalStateException(
                    "Timeout occurred. Aborting execution of specs. (Try configuring 'debug' to 'true' for more details.)");
        }
    }

    private Boolean executionFinished(HtmlPage page) {
        //return false;
        ScriptResult result = page.executeJavaScript("reporter.finished");
        return (Boolean) result.getJavaScriptResult();
    }

    private void quietIncorrectnessListener(WebClient webClient) {
        //Disables stuff like this "com.gargoylesoftware.htmlunit.IncorrectnessListenerImpl notify WARNING: Obsolete content type encountered: 'text/javascript'."
        webClient.setIncorrectnessListener(new IncorrectnessListener() {
            @Override
            public void notify(String arg0, Object arg1) {
            }
        });
    }

}
