package com.sellit.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.sellit.R;

/**
 * Created by IntelliJ IDEA.
 * User: vinodh
 * Date: 2/12/12
 * Time: 10:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class DashBoardActivity extends Activity{

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dashboard);
        //attach event handler to dash buttons
        DashboardClickListener dBClickListener = new DashboardClickListener();
        findViewById(R.id.linkedin).setOnClickListener(dBClickListener);
    }

    private class DashboardClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent i = null;
            switch (v.getId()) {
                case R.id.linkedin:
                    i = new Intent(DashBoardActivity.this, LinkedinActivity.class);
                    break;
            }
            if(i != null) {
                startActivity(i);
            }
        }
    }

}
