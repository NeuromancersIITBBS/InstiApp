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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class IITBbsScraping extends AsyncTask<Void, Void, Integer> {
    private String link = null;
    private String fileName = null;
    private String fileExtension = "pdf";
    private ProgressBar progressBar = null;
    private boolean forceUpdate = false;

    private long timeout = 5;
    private Context context = null;
    private File file = null;

    public IITBbsScraping(String link, String fileName, ProgressBar progressBar, boolean forceUpdate) {
        this.link = link;
        this.fileName = fileName;
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
        } else if (link.endsWith(".pdf")) {
            return Integer.valueOf(downloadFile(link));
        } else {
            return -1;
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
        } catch (IOException e) {
            Toast.makeText(context, "Error Connecting To URL While Scraping", Toast.LENGTH_SHORT).show();
        }
        return -1;
    }
}