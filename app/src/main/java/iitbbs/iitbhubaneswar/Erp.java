package iitbbs.iitbhubaneswar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

public class Erp extends Fragment {
    private String webaddr = "http://www.iitbbs.ac.in/erp_portal.php";

    public WebView mwebview;
    ProgressBar bar;
    private FrameLayout framelayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(iitbbs.iitbhubaneswar.R.layout.erp, container, false);
        framelayout = (FrameLayout)v.findViewById( iitbbs.iitbhubaneswar.R.id.framelayout );
        bar = (ProgressBar)v.findViewById(iitbbs.iitbhubaneswar.R.id.progressBar);
        bar.setMax(100);
        getActivity().setTitle("ERP");
        WebView webview = (WebView) v.findViewById(iitbbs.iitbhubaneswar.R.id.erp_webview);
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
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
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

