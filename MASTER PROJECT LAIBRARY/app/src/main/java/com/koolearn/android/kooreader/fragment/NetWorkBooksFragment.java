package com.koolearn.android.kooreader.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.koolearn.android.kooreader.RecyclerItemClickListener;
import com.koolearn.android.kooreader.book.Book;
import com.koolearn.android.kooreader.book.BookDetailActivity;
import com.koolearn.klibrary.ui.android.R;

import java.util.List;

public class NetWorkBooksFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private NetBookAdapter mNetBookAdapter;
    private ProgressBar mProgressBar;
    private FloatingActionButton mFabSearch;
    private SwipeRefreshLayout mRefreshLayout;

    private static final int ANIM_DURATION_FAB = 400;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mRefreshLayout.setRefreshing(false);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_network_books, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        mRefreshLayout.setColorSchemeResources(R.color.progressBara, R.color.progressBarb);
        mRefreshLayout.setProgressBackgroundColor(R.color.progressBarBg);
        mRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), mRecyclerView, onItemClickListener));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mNetBookAdapter = new NetBookAdapter(getActivity());
        mRecyclerView.setAdapter(mNetBookAdapter);

        setUpFAB(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFabSearch.setTranslationY(2 * getResources().getDimensionPixelOffset(R.dimen.btn_fab_size));
        doSearch(1, 30);
    }

    private void doSearch(int page, int pageSize) {
        mProgressBar.setVisibility(View.VISIBLE);
        mNetBookAdapter.clearItems();
        Book.searchBooks(page, pageSize, new Book.IBookResponse<List<Book>>() {
            @Override
            public void onData(List<Book> books) {
                startFABAnimation();
                mProgressBar.setVisibility(View.GONE);
                mNetBookAdapter.updateItems(books, true);
            }
        });
    }

    private void setUpFAB(View view) {
        mFabSearch = (FloatingActionButton) view.findViewById(R.id.fab_net_search);
        mFabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText inputServer = new EditText(getActivity());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("搜索").setIcon(android.R.drawable.ic_search_category_default).setView(inputServer).setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        String input = inputServer.getText().toString();
                        if (!TextUtils.isEmpty(input)) {
                            doSearch(1, 30);
                        }
                    }
                });
                builder.show();
            }
        });
    }

    private void startFABAnimation() {
        mFabSearch.animate()
                .translationY(0)
                .setInterpolator(new OvershootInterpolator(1.f))
                .setStartDelay(500)
                .setDuration(ANIM_DURATION_FAB)
                .start();
    }

    private RecyclerItemClickListener.OnItemClickListener onItemClickListener = new RecyclerItemClickListener.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Book book = mNetBookAdapter.getBook(position);
            Intent intent = new Intent(getActivity(), BookDetailActivity.class);
            intent.putExtra("book", book);
            ActivityOptionsCompat options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                            view.findViewById(R.id.ivBook), getString(R.string.transition_book_img));
            ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
        }

        @Override
        public void onItemLongClick(View view, int position) {

        }
    };

    @Override
    public void onRefresh() {
        handler.sendEmptyMessageDelayed(1, 2000);
    }
}