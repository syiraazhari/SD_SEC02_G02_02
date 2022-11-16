package com.koolearn.android.kooreader.fragment;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ProgressBar;

import com.koolearn.android.kooreader.KooReader;
import com.koolearn.android.kooreader.MainActivity;
import com.koolearn.android.kooreader.RecyclerItemClickListener;
import com.koolearn.android.kooreader.animation.FlipInLeftYAnimator;
import com.koolearn.android.kooreader.library.LibraryActivity;
import com.koolearn.android.kooreader.libraryService.BookCollectionShadow;
import com.koolearn.android.util.OrientationUtil;
import com.koolearn.klibrary.ui.android.R;
import com.koolearn.kooreader.book.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * ******************************************
 * 作    者 ：  杨越
 * 版    本 ：  1.0
 * 创建日期 ：  2016/3/29 ${time}
 * 描    述 ：
 * 修订历史 ：
 * ******************************************
 */
public class LocalBooksFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout refreshLayout;
    private FloatingActionButton mFabSearch;
    private ProgressBar mProgressBar;

    private List<Book> bookshelf = new ArrayList<>();
    private LocalBookAdapter mLocalBookAdapter;
    private final BookCollectionShadow bookCollectionShadow = new BookCollectionShadow();

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            getBooks();
            refreshLayout.setRefreshing(false);
        }
    };
    private View view;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_local_books, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.progressBara, R.color.progressBarb);
        refreshLayout.setProgressBackgroundColor(R.color.progressBarBg);
        setUpFAB(view);

        mRecyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), mRecyclerView, onItemClickListener));
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setItemAnimator(new FlipInLeftYAnimator());

        mLocalBookAdapter = new LocalBookAdapter(getActivity());
        mRecyclerView.setAdapter(mLocalBookAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getBooks();
    }

    private void getBooks() {
        bookCollectionShadow.bindToService(getActivity(), new Runnable() {
            public void run() {
                bookshelf.clear();
                bookshelf = bookCollectionShadow.recentlyOpenedBooks(15);
                mLocalBookAdapter.clearItems();
                mLocalBookAdapter.updateItems(bookshelf, true);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFabSearch.setTranslationY(2 * getResources().getDimensionPixelOffset(R.dimen.btn_fab_size));
        startFABAnimation();
    }

    private void setUpFAB(View view) {
        mFabSearch = (FloatingActionButton) view.findViewById(R.id.fab_search);
        mFabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*int hasWriteStoragePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
                if (hasWriteStoragePermission == PackageManager.PERMISSION_GRANTED) {
                    OrientationUtil.startActivity(getActivity(), new Intent(getActivity(), LibraryActivity.class));
                    getActivity().overridePendingTransition(R.anim.tran_fade_in, R.anim.tran_fade_out);
                }else{
                    //没有权限，向用户请求权限
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                }*/

                if (SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        OrientationUtil.startActivity(getActivity(), new Intent(getActivity(), LibraryActivity.class));
                        getActivity().overridePendingTransition(R.anim.tran_fade_in, R.anim.tran_fade_out);
                    } else { //request for the permission
                        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                        Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{READ_EXTERNAL_STORAGE}, 100);
                }

            }
        });
    }

    private void startFABAnimation() {
        mFabSearch.animate()
                .translationY(0)
                .setInterpolator(new OvershootInterpolator(1.f))
                .setStartDelay(500)
                .setDuration(400)
                .start();
    }

    @Override
    public void onRefresh() {
        handler.sendEmptyMessageDelayed(1, 2000);
    }

    private RecyclerItemClickListener.OnItemClickListener onItemClickListener = new RecyclerItemClickListener.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            KooReader.openBookActivity(getActivity(), mLocalBookAdapter.getBook(position), null);
            getActivity().overridePendingTransition(R.anim.tran_fade_in, R.anim.tran_fade_out);
        }

        @Override
        public void onItemLongClick(final View view, final int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setIcon(R.drawable.ic_error_outline_black).setTitle("Delete Book?").setNeutralButton("Later", null).setNegativeButton("Cancel", null);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    bookCollectionShadow.removeBook(mLocalBookAdapter.getBook(position), true);
                    mLocalBookAdapter.removeItems(position);
                    Snackbar.make(mFabSearch, "Delete Successfully", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
            builder.show();
        }
    };

    @Override
    public void onDestroy() {
        bookCollectionShadow.unbind();
        super.onDestroy();
    }
}