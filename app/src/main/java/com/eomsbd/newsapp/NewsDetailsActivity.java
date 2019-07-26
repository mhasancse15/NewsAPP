package com.eomsbd.newsapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.eomsbd.newsapp.utils.Utils;

public class NewsDetailsActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener{
    private ImageView imageView;
    private TextView appbarTitle,appbarSubtitle,date,time,title;
    private boolean isHideTolbarView = false;
    private FrameLayout frameLayout;
    private LinearLayout linearLayout;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private String mUrl,mImg,mTitle,mDate,mSource,mAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        toolbar=findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final CollapsingToolbarLayout collapsingToolbarLayout=findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");
        getSupportActionBar().setTitle("");

        appBarLayout=findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(this);

        frameLayout=findViewById(R.id.date_behavior);
        linearLayout=findViewById(R.id.title_appbar);
        imageView=findViewById(R.id.backdrop);
        appbarTitle=findViewById(R.id.title_on_appbar);
        appbarSubtitle=findViewById(R.id.subtitle_on_appbar);
        date=findViewById(R.id.date);
        time=findViewById(R.id.time);
        title=findViewById(R.id.title);

        Intent intent=getIntent();
        mUrl=intent.getStringExtra("url");

        mImg=intent.getStringExtra("img");
        mTitle=intent.getStringExtra("title");
        mDate=intent.getStringExtra("date");
        mSource=intent.getStringExtra("source");
        mAuthor=intent.getStringExtra("author");

        RequestOptions requestOptions=new RequestOptions();
        requestOptions.error(Utils.getRandomDrawbleColor());
        Glide.with(this)
                .load(mImg)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);

        appbarTitle.setText(mSource);
        appbarSubtitle.setText(mUrl);
        date.setText(mDate);
        title.setText(mTitle);

        String author =null;
        if(mAuthor !=null || mAuthor !=""){
            mAuthor=" \u2022 "+mAuthor;
        }else {
            author="";
        }
        time.setText(mSource+author+" \u2022 "+Utils.DateFormat(mDate));
        initWebView(mUrl);






    }
    private void initWebView(String url){
        WebView webView=findViewById(R.id.webView);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
       webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        int maxScroll=appBarLayout.getTotalScrollRange();
        float percentage=(float)Math.abs(verticalOffset)/(float)maxScroll;

        if(percentage ==1f && isHideTolbarView){
            frameLayout.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            isHideTolbarView=!isHideTolbarView;
        }else if(percentage <1f && isHideTolbarView){
            frameLayout.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
            isHideTolbarView=!isHideTolbarView;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.view_web){
            Intent intent=new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(mUrl));
            startActivity(intent);
            return true;
        }
        else if(id==R.id.share){
            try{
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plan");
                intent.putExtra(Intent.EXTRA_SUBJECT,mSource);
                String body=mTitle+"\n"+mUrl+"\n"+"Share From the news App"+"\n";
                intent.putExtra(Intent.EXTRA_TEXT,body);
                startActivity(Intent.createChooser(intent,"Share With :"));
            }catch (Exception e){
                Toast.makeText(this, "Sorry,\n Cannot be share", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
