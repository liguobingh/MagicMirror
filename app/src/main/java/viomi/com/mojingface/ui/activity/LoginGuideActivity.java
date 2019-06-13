package viomi.com.mojingface.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import viomi.com.mojingface.R;
import viomi.com.mojingface.base.BaseActivity;


public class LoginGuideActivity extends BaseActivity {

    private ImageView back_icon;
    private TextView title_view;
    private TextView how_to_down;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login_guide);
        back_icon = findViewById(R.id.back_icon);
        title_view = findViewById(R.id.title_view);
        how_to_down = findViewById(R.id.how_to_down);
        title_view.setText("登录指引");
    }

    @Override
    protected void initListener() {
        back_icon.setOnClickListener(v -> onBackPressed());

        how_to_down.setOnClickListener(v -> startActivity(new Intent(LoginGuideActivity.this, DownloadViomiActivity.class)));
    }

    @Override
    protected void init() {

    }
}
