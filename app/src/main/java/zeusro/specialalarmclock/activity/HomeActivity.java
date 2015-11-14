package zeusro.specialalarmclock.activity;

import zeusro.specialalarmclock.R;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toast toast = Toast.makeText(getApplicationContext(), R.string.Thank,Toast.LENGTH_SHORT);
        //显示toast信息
        toast.show();

    }
}
