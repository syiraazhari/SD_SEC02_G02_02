package com.koolearn.android.kooreader.book;

import android.graphics.Bitmap;
import android.os.Bundle;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.koolearn.android.kooreader.events.AddBookEvent;
import com.koolearn.android.kooreader.events.OpenBookEvent;
import com.koolearn.android.kooreader.fragment.DetailFragment;
import com.koolearn.android.kooreader.fragment.TOCDetailFragment;
import com.koolearn.android.kooreader.view.DownloadProcessButton;
import com.koolearn.klibrary.ui.android.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BookDetailActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private Book mBook;
    private DownloadProcessButton mBtnDownload;
    private Toolbar mToolbar;

    private static AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
//    private CoordinatorLayout mCooLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appbar_detail);
//        mCooLayout = (CoordinatorLayout) findViewById(R.id.cl);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mBtnDownload = (DownloadProcessButton) findViewById(R.id.btn_download);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mBook = (Book) getIntent().getSerializableExtra("book");
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(mBook.getTitle());

        ImageView ivImage = (ImageView) findViewById(R.id.ivImage);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.book_cover)
                .showImageOnFail(R.drawable.book_cover)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(mBook.getImage(), ivImage, options);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("内容简介"));
        tabLayout.addTab(tabLayout.newTab().setText("作者简介"));
        tabLayout.addTab(tabLayout.newTab().setText("目录"));
        tabLayout.setupWithViewPager(mViewPager);

        final String filePath = getExternalCacheDirPath() + "/" + mBook.getTitle() + ".epub";
        File fileDir = new File(filePath);
        if (fileDir.exists()) {
            mBtnDownload.setProgress(100);
        }

        mBtnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBtnDownload.getProgress() == 100) {
                    startOpenBookByPath(filePath);
                } else {
                    mBtnDownload.setProgress(1);
                    mBtnDownload.setEnabled(false);
                    downloadBook(mBook);
                }
            }
        });
    }

    private void setupViewPager(ViewPager mViewPager) {
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(DetailFragment.newInstance(mBook.getSummary()), "内容简介");
        adapter.addFragment(DetailFragment.newInstance(mBook.getAuthor_intro()), "作者简介");
        adapter.addFragment(TOCDetailFragment.newInstance(mBook.getCatalog()), "目录");
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0, true);
    }

    static class MyPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    private void downloadBook(Book book) {
        String filePath = getExternalCacheDirPath() + "/" + book.getTitle() + ".epub";
        File fileDir = new File(filePath);
        // http://45.78.20.53:8080/read.epub
        client.get("http://file.bmob.cn/" + mBook.getUrl(), null, new FileAsyncHttpResponseHandler(fileDir) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                Toast.makeText(BookDetailActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                mBtnDownload.setProgress(100);
                mBtnDownload.setEnabled(true);
                addBookShelf(file.getPath());
//                startOpenBookByPath(file.getPath());
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                mBtnDownload.setProgress((int) ((bytesWritten * 1.0 / totalSize) * 100));
            }
        });
    }

    /**
     * 放入书架
     *
     * @param bookPath
     */
    private void addBookShelf(String bookPath) {
        EventBus.getDefault().post(new AddBookEvent(bookPath));
    }

    /**
     * 通过已经下载好的路径打开书
     *
     * @param bookPath
     */
    private void startOpenBookByPath(String bookPath) {
        EventBus.getDefault().post(new OpenBookEvent(bookPath));
    }

    private String getExternalCacheDirPath() {
        File d = getExternalCacheDir();
        if (d != null) {
            d.mkdirs();
            if (d.exists() && d.isDirectory()) {
                return d.getPath();
            }
        }
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}