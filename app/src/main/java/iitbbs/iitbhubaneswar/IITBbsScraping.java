package iitbbs.iitbhubaneswar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Looper;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tom_roush.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Semaphore;

public class IITBbsScraping extends AsyncTask<Void, Void, Integer> {
    private String fileName = null;
    private String fileExtension = "pdf";
    private ProgressBar progressBar = null;
    private boolean forceUpdate = false;
    private String link = null;

    private Context context = null;
    private File file = null;

    private final Semaphore semaphore = new Semaphore(0);

    public IITBbsScraping(String fileName, ProgressBar progressBar, boolean forceUpdate) {
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
        if (result == 0) {
            file = getFile(fileName + "." + fileExtension);
            startFileViewActivity(file, "application/" + fileExtension);
        }
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        if (Looper.myLooper() == null) { // check already Looper is associated or not.
            Looper.prepare(); // No Looper is defined So define a new one
        }

        file = getFile(fileName + "." + fileExtension);
        if (file.exists() && !forceUpdate) {
            return 0;
        }

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(fileName + "_link");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                link = (String) dataSnapshot.getValue();
                semaphore.release();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return downloadFile(link);
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
        } catch (Exception e) {
            Toast.makeText(context, "Error downloading the file - try again later", Toast.LENGTH_SHORT).show();
        }
        return -1;
    }
}