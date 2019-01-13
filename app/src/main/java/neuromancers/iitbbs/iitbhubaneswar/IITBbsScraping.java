package neuromancers.iitbbs.iitbhubaneswar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tom_roush.pdfbox.pdmodel.PDDocument;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

public class IITBbsScraping extends AsyncTask<Void, Void, Integer> {

    Logger logger = Logger.getLogger("InstiAppUtil Logger");

    private String website = null;
    private String fileName = null;
    private String fileExtension = null;
    private ProgressBar progressBar = null;
    private boolean forceUpdate = false;

    private long timeout = 5;
    private Context context = null;
    private File file = null;

    public IITBbsScraping(String website, String fileName, String fileExtension, ProgressBar progressBar, boolean forceUpdate) {
        this.website = website;
        this.fileName = fileName;
        this.fileExtension = fileExtension;
        this.progressBar = progressBar;
        this.forceUpdate = forceUpdate;

        context = progressBar.getContext().getApplicationContext();
    }

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(Integer result) {
        if (result.intValue() == -1) {
            Toast.makeText(context, "Error downloading the file - try again later", Toast.LENGTH_SHORT).show();
        } else if (result.intValue() == 0) {
            file = getFile(fileName + "." + fileExtension);
            startFileViewActivity(file, "application/" + fileExtension);
        }
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        if(Looper.myLooper() == null) { // check already Looper is associated or not.
            Looper.prepare(); // No Looper is defined So define a new one
        }

        file = getFile(fileName + "." + fileExtension);
        if (file.exists() && !forceUpdate) {
            startFileViewActivity(file, "application/" + fileExtension);
            return Integer.valueOf(1);
        } else if (website.endsWith(".pdf")) {
            return Integer.valueOf(downloadFile(website));
        } else {
            return Integer.valueOf(scrape());
        }
    }

    private File getFile(String fileName) {
        File file = new File(context.getExternalFilesDir(null), fileName);
        return file;
    }

    private void startFileViewActivity(File file, String type) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), type);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public int scrape() {
        Document document = null;
        long startTime = System.nanoTime();
        long endTime = startTime;
        while ((endTime - startTime) / 1000000000 < timeout) {
            try {
                Connection connection = Jsoup.connect(website);
                document = connection.get();
                break;
            } catch (IOException e) {
                Toast.makeText(context, "Network Issues", Toast.LENGTH_SHORT).show();
                logger.warning(e.getMessage());
            }
            endTime = System.nanoTime();
        }

        if (document != null) {
            Elements anchorTags = document.getElementsByTag("a");
            for (Element anchorTag : anchorTags) {
                String href = anchorTag.attr("href");
                if (href.endsWith(".pdf")) {
                    String fileLink = "http://www.iitbbs.ac.in" + href.substring(2);

                    return downloadFile(fileLink);
                }
            }
        }

        return -1;
    }

    private int downloadFile(String fileLink) {
        try {
            URL link = new URL(fileLink);
            URLConnection urlConnection = link.openConnection();

            PDDocument pdDocument = PDDocument.load(urlConnection.getInputStream());
            FileOutputStream outputStream = new FileOutputStream(file);
            pdDocument.save(outputStream);
            pdDocument.close();
            return 0;
        } catch (MalformedURLException e) {
            Toast.makeText(context, "Malformed URL While Scraping", Toast.LENGTH_SHORT).show();
            logger.warning("Malformed URL While Scraping");
        } catch (IOException e) {
            Toast.makeText(context, "Error Connecting To URL While Scraping", Toast.LENGTH_SHORT).show();
            logger.warning("Error Connecting To URL While Scraping: " + e.getMessage());
        }
        return -1;
    }
}