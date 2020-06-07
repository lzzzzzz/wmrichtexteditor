package com.widemouth.wmrichtexteditor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.widemouth.library.wmview.WMEditText;
import com.widemouth.library.wmview.WMTextEditor;


public class ShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("阅览界面");
        }
        String html = getIntent().getStringExtra(MainActivity.HTML);
        WMTextEditor showEditText = findViewById(R.id.showTextEditor);
        showEditText.setEditorType(WMTextEditor.TYPE_NON_EDITABLE);
        showEditText.fromHtml(html);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
