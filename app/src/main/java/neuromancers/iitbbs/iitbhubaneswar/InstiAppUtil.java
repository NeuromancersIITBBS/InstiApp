package neuromancers.iitbbs.iitbhubaneswar;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

public class InstiAppUtil {

    Logger logger = Logger.getLogger("InstiAppUtil Logger");

    public InstiAppUtil() {
    }

    public void onClick(View view) {
        String url = null;
        switch (view.getId()) {
            /*
            swicth case fall-through cases
             */
            case R.id.nav_header_name:
                ;
            case R.id.nav_header_link:
                ;
            default:
                url = "http://www.iitbbs.ac.in";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                view.getContext().startActivity(browserIntent);
        }
    }

    public void scraper(View view, String url) {
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            Toast.makeText(view.getContext(), "Invalid URL For Scraping", Toast.LENGTH_SHORT).show();
            logger.warning("Invalid URL For Scraping");
        }

        if (document != null) {
            Elements anchorTags = document.getElementsByTag("a");
            for (Element anchorTag : anchorTags) {
                String href = anchorTag.attr("href");
                if (href.endsWith(".pdf") || href.endsWith(".xlsx") || href.endsWith(".xls")) {
                    String file = "http://www.iitbbs.ac.in" + href.substring(2);

                    try {
                        URL link = new URL(file);
                        URLConnection urlConnection = link.openConnection();

                        if (href.endsWith(".pdf")) {
                            PDDocument pdDocument = PDDocument.load(urlConnection.getInputStream());
                            pdDocument.save(view.getContext().getFilesDir() + href);
                            pdDocument.close();
                        } else {

                        }
                    } catch (MalformedURLException e) {
                        Toast.makeText(view.getContext(), "Malformed URL While Scraping", Toast.LENGTH_SHORT).show();
                        logger.warning("Malformed URL While Scraping");
                    } catch (IOException e) {
                        Toast.makeText(view.getContext(), "Error Connecting To URL While Scraping", Toast.LENGTH_SHORT).show();
                        logger.warning("Error Connecting To URL While Scraping");
                    }

                }
            }
        }
    }
}
