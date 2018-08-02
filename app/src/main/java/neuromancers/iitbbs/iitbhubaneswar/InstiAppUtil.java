package neuromancers.iitbbs.iitbhubaneswar;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import java.util.logging.Logger;

public class InstiAppUtil {

    public InstiAppUtil() {
    }

    public void onClick(View view) {
        String url = null;
        switch (view.getId()) {
            /*
            swicth case fall-through cases
             */
            case R.id.nav_header_name:
            case R.id.nav_header_link:
                url = "http://www.iitbbs.ac.in";
                break;
            case R.id.transport_query_web:
                url = "http://www.iitbbs.ac.in/transportation.php";
                break;
            case R.id.calendar_query_web:
                url = "http://www.iitbbs.ac.in/academic-calender.php";
                break;
            case R.id.regulations_query_web:
                url = "http://www.iitbbs.ac.in/iit-regulation.php";
                break;
            case R.id.timetable_query_web:
                url = "http://www.iitbbs.ac.in/time-table.php";
                break;
            default:
                url = "http://www.iitbbs.ac.in";
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        view.getContext().startActivity(browserIntent);
    }
}
