package neuromancers.iitbbs.iitbhubaneswar;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveTheme {
    SharedPreferences mysharedPreferences;

    public SaveTheme(Context context)
    {
        mysharedPreferences = context.getSharedPreferences("filename", Context.MODE_PRIVATE);
    }

    public void setNightModeState(Boolean state)
    {
        SharedPreferences.Editor editor = mysharedPreferences.edit();
        editor.putBoolean("NightMode",state);
        editor.commit();
    }

    public Boolean loadNightModeSate()
    {
        Boolean state = mysharedPreferences.getBoolean("NightMode", false);
        return state;
    }
}
