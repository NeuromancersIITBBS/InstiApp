package neuromancers.iitbbs.iitbhubaneswar;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

public class InstiAppUtil {

    public InstiAppUtil() {
    }

    public void onClick(View view) {
        String url = null;
        switch(view.getId()){
            /*
            swicth case fall-through cases
             */
            case R.id.nav_header_name:;
            case R.id.nav_header_link:;
            default:
                url = "http://www.iitbbs.ac.in";
        }

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        view.getContext().startActivity(browserIntent);
    }
}
