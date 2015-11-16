package net.egordmitriev.loaderviewdemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.egordmitriev.loaderview.LoaderView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LoaderView loaderView = (LoaderView) findViewById(R.id.loaderview);

        this.findViewById(R.id.start_load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loaderView.setState(LoaderView.STATE_LOADING);
            }
        });

        this.findViewById(R.id.start_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loaderView.setState(LoaderView.STATE_ERROR);
            }
        });

        this.findViewById(R.id.start_success).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loaderView.setState(LoaderView.STATE_IDLE);
            }
        });

        final Handler handler = new Handler();
        loaderView.setListener(new LoaderView.LoaderViewCallback() {
            @Override
            public void onRetryClick() {
                loaderView.setState(LoaderView.STATE_LOADING);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loaderView.setState(LoaderView.STATE_IDLE);
                    }
                }, 2000);
            }
        });
    }
}
