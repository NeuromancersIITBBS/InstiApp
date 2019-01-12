package neuromancers.iitbbs.iitbhubaneswar;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.app.Activity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class Erp extends Fragment {
    private String webaddr = "http://www.iitbbs.ac.in/erp_portal.php";

    public WebView mwebview;
    ProgressBar bar;
    private FrameLayout framelayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.erp, container, false);
        framelayout = (FrameLayout)v.findViewById( R.id.framelayout );
        bar = (ProgressBar)v.findViewById(R.id.progressBar);
        bar.setMax(100);
        getActivity().setTitle("ERP");
        WebView webview = (WebView) v.findViewById(R.id.erp_webview);
        webview.setWebViewClient(new MyWebViewClient());
        webview.setWebChromeClient(new WebChromeClient(){
            public void onProgressChanged(WebView view, int progress){
                framelayout.setVisibility( View.VISIBLE );
                bar.setProgress( progress );

                if(progress==100)
                    framelayout.setVisibility( View.GONE );
                super.onProgressChanged( view, progress);
            }
        } );
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webview.setVerticalScrollBarEnabled( false );
        webview.loadUrl( webaddr );
        bar.setProgress( 0 );
        return v;
    }
    public  class MyWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            view.loadUrl( url );
            framelayout.setVisibility(View.VISIBLE );
            return true;
        }
    }

}

