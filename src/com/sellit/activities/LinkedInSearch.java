package com.sellit.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.sellit.R;

/**
 * Created by IntelliJ IDEA.
 * User: vinodh
 * Date: 2/12/12
 * Time: 3:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class LinkedInSearch extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linkedin_search);

        Button search = (Button) findViewById(R.id.btnSearch);
        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent intent = new Intent(LinkedInSearch.this, LinkedinActivity.class);
                Bundle b = new Bundle();

                EditText txt1 = (EditText) findViewById(R.id.firstName);
                EditText txt2 = (EditText) findViewById(R.id.lastName);

                b.putString("firstName", txt1.getText().toString());
                b.putString("lastName", txt2.getText().toString());

                //Add the set of extended data to the intent and start it
                intent.putExtras(b);
                startActivity(intent);
            }

        });
    }
}
