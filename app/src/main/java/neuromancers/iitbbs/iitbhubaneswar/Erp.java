package neuromancers.iitbbs.iitbhubaneswar;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Erp extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.erp, container, false);

        getActivity().setTitle("ERP");

        WebView webview = (WebView) v.findViewById(R.id.erp_webview);
        webview.loadUrl("http://www.iitbbs.ac.in/erp_portal.php");

        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webview.setWebViewClient(new WebViewClient());
        return v;
    }
}

