package name.isergius.freelance.protowpreader;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


/**
 * @author Sergey Kondratyev
 */

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolBar = (Toolbar) findViewById(R.id.activity_list_toolbar);
        setSupportActionBar(toolBar);
    }
}
