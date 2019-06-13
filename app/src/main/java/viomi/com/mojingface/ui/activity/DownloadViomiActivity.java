package viomi.com.mojingface.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import viomi.com.mojingface.R;
import viomi.com.mojingface.base.BaseActivity;

public class DownloadViomiActivity extends BaseActivity {

    private ImageView back_icon;
    private TextView title_view;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_download_viomi);
        back_icon = findViewById(R.id.back_icon);
        title_view = findViewById(R.id.title_view);
        title_view.setText("下载云米商城");
    }

    @Override
    protected void initListener() {
        back_icon.setOnClickListener(v ->  onBackPressed());
    }

    @Override
    protected void init() {

    }
}
