package com.koolearn.android.kooreader;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koolearn.android.kooreader.events.AddBookEvent;
import com.koolearn.android.kooreader.events.OpenBookEvent;
import com.koolearn.android.kooreader.fragment.BookMarksFragment;
import com.koolearn.android.kooreader.fragment.BookNoteFragment;
import com.koolearn.android.kooreader.fragment.LocalBooksFragment;
import com.koolearn.android.kooreader.fragment.NetWorkBooksFragment;
import com.koolearn.android.kooreader.libraryService.BookCollectionShadow;
import com.koolearn.klibrary.ui.android.R;
import com.koolearn.kooreader.Paths;
import com.ui.AdminStaff.AdminStaffFramgnet;
import com.ui.book.BookFragment;
import com.ui.book.DateDatabase;
import com.ui.home.AboutUsPage;
import com.ui.home.FeedbackActivity;
import com.ui.home.FeedbackForm;
import com.ui.home.HomeFragment;
import com.ui.profile.ProfileFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;



public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Timer timer = null;
    private TimerTask timeTask = null;
    private boolean isExit = false;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String bookingid;
    int currentyear, currentmonth, currentday, idyear, idmonth, idday, newyear, newmonth, newday;

    private final BookCollectionShadow myCollection = new BookCollectionShadow();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        bookingid = getIntent().getStringExtra("keyid");

        Calendar instance = Calendar.getInstance();
        currentmonth = instance.get(Calendar.MONTH)+1;
        currentyear = instance.get(Calendar.YEAR);
        currentday = instance.get(Calendar.DAY_OF_MONTH);

        databaseReference = firebaseDatabase.getReference("Date").child(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DateDatabase dateDatabase = snapshot.getValue(DateDatabase.class);
                String day = dateDatabase.getDay();
                String month = dateDatabase.getMonth();
                String year = dateDatabase.getYear();

                if(day !=null || month != null || year != null){
                    idday = Integer.parseInt(day);
                    idmonth = Integer.parseInt(month);
                    idyear = Integer.parseInt(year);
                }else {
                    idday = 0;
                    idmonth = 0;
                    idyear = 0;
                }

                String status = dateDatabase.getStatus();

                if(status.equals("1")){

                    newmonth = idmonth+1;
                    newyear = idyear;
                    newday = idday;

                    if(newmonth>12){
                        newmonth = (idmonth+1)%12;
                        newyear = idyear+1;
                    }

                    if(idmonth == 2){
                        if((idyear==2020) ||(idyear == 2024)|| (idyear == 2028)){
                            newday = idday%29;
                        }else{
                            newday = idday%28;
                        }
                    }

                    if ((currentday == newday) && (currentmonth == newmonth) && (currentyear == newyear)){

                        Intent notificationIntent = new Intent(MainActivity.this, MainActivity.class);
                        PendingIntent pi = PendingIntent.getActivity(MainActivity.this, 0, notificationIntent, 0);

                        NotificationCompat.Builder notification = new NotificationCompat.Builder(MainActivity.this)
                                .setContentTitle("Laibrary App")
                                .setContentText("Today is the deadline returning book. Remember to return your book by today.")
                                .setSmallIcon(R.drawable.favicon).setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.logo))
                                .setColor(getResources().getColor(R.color.wallet_holo_blue_light))
                                .setVibrate(new long[]{0, 300, 300, 300})
                                .setLights(Color.WHITE, 1000, 5000)
                                .setContentIntent(pi)
                                .setAutoCancel(true)
                                .setPriority(NotificationCompat.PRIORITY_HIGH);

                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        notificationManager.notify(0, notification.build());

                    }
                }else if(status.equals("0")){

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        init();
        EventBus.getDefault().register(this);



    }

    private void init() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);



        timer = new Timer();
        copyBooks();
        switchToLocalBook();
    }

    private void switchToLocalBook() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new LocalBooksFragment()).commit();
        mToolbar.setTitle(R.string.local_book);
    }

    private void switchNetWorkBook() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new NetWorkBooksFragment()).commit();
        mToolbar.setTitle(R.string.network_book);
    }

    private void switchToBookNote() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new BookMarksFragment()).commit();
        mToolbar.setTitle(R.string.book_note);
    }

    private void switchToBookMark() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new BookNoteFragment()).commit();
        mToolbar.setTitle(R.string.book_mark);
    }

    private void switchToUser(){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new ProfileFragment()).commit();
        mToolbar.setTitle(R.string.title_profile);
    }

    private void switchToHome(){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new HomeFragment()).commit();
        mToolbar.setTitle(R.string.title_profile);
    }

    private void switchToAdminStaff(){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new AdminStaffFramgnet()).commit();
        mToolbar.setTitle(R.string.title_profile);
    }

    private void switchToBooking(){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new BookFragment()).commit();
        mToolbar.setTitle(R.string.title_profile);
    }

    /*private void switchToAboutUs(){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new AboutUsPage()).commit();
        mToolbar.setTitle(R.string.about_us);
    }*/

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            doubleExit();
        }
    }

    /**
     * DoubleExit
     */
    private void doubleExit() {
        if (isExit) {
            finish();
        } else {
            isExit = true;
            Toast.makeText(this, "再按一次退出掌读", Toast.LENGTH_SHORT).show();
            timeTask = new TimerTask() {

                @Override
                public void run() {
                    isExit = false;
                }
            };
            timer.schedule(timeTask, 2000);
        }
    }

    /**
     * 字体拷贝
     */
    private void copyFonts(String fontName) {
        File destFile = new File(getFilesDir(), fontName);
        if (destFile.exists()) {
            return;
        }

        FileOutputStream out = null;
        InputStream in = null;

        try {
            in = getAssets().open(fontName);
            out = new FileOutputStream(destFile);
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copyBooks() {
        new Thread() {
            @Override
            public void run() {
                copyFonts("hksv.ttf");
                copyFonts("wryh.ttf");
//                copyEpub("harry.epub");
//                copyEpub("abeaver.epub");
//                copyEpub("silverchair.epub");

//                copyEpub("ExaminationCloze.doc");
//                copyEpub("function.doc");
//                copyEpubToSdCard("TheSilverChair.epub");
//                copyEpubToSdCard("ExaminationCloze.doc");
//                copyEpubToSdCard("function.doc");
            }
        }.start();
    }

    /**
     * epub拷贝
     */
    private void copyEpub(String epubName) {
        final String fileName = Paths.internalTempDirectoryValue(this) + "/" + epubName;
        File file = new File(fileName);
        if (file.exists()) {
            return;
        }

        FileOutputStream out = null;
        InputStream in = null;

        try {
            in = getAssets().open(epubName);
            out = new FileOutputStream(file);
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * epub拷贝
     */
    private void copyEpubToSdCard(String epubName) {
        File destFile = new File(Environment.getExternalStorageDirectory(), epubName); // 与Path路径中的设置一致,可以读到数据库中
        if (destFile.exists()) {
            return;
        }

        FileOutputStream out = null;
        InputStream in = null;

        try {
            in = getAssets().open(epubName);
            out = new FileOutputStream(destFile);

            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpProfileImage() {
        findViewById(R.id.profile_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    protected void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.navigation_local_book) {
            switchToLocalBook();

        } else if (id == R.id.navigation_book_note) {
            switchToBookNote();

        } else if (id == R.id.navigation_profile){
            switchToUser();

        }   else if (id == R.id.navigation_home){
            switchToHome();

        }   else if (id == R.id.navigation_book){
            switchToBooking();

        }   else if (id == R.id.navigation_staffAdmin){
            switchToAdminStaff();

        }   else if(id == R.id.menuAboutUs){
            startActivity(new Intent(this, AboutUsPage.class));

        }   else if(id == R.id.menuFeedback){
            startActivity(new Intent(this, FeedbackActivity.class));

        }
        mDrawerLayout.closeDrawers();
        return true;
    }

    @Subscribe
    public void onOpenBookEvent(final OpenBookEvent event) {
        myCollection.bindToService(this, new Runnable() {
            public void run() {
                com.koolearn.kooreader.book.Book book = myCollection.getBookByFile(event.bookPath);
                if (book != null) {
                    openBook(book);
                } else {
                    Toast.makeText(MainActivity.this, "打开失败,请重试", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openBook(com.koolearn.kooreader.book.Book data) {
        KooReader.openBookActivity(this, data, null);
        overridePendingTransition(R.anim.tran_fade_in, R.anim.tran_fade_out);
    }

    @Subscribe
    public void onAddBookEvent(final AddBookEvent event) {
        myCollection.bindToService(this, new Runnable() {
                    public void run() {
                        com.koolearn.kooreader.book.Book book = myCollection.getBookByFile(event.bookPath);
                        if (book != null) {
                            myCollection.saveBook(book); // 保存书籍
                            myCollection.addToRecentlyOpened(book); // 保存书籍至最近阅读的数据库
                            Toast.makeText(MainActivity.this, "已放入书架", Toast.LENGTH_SHORT).show();
//                            SuperActivityToast toast = new SuperActivityToast(MainActivity.this, SuperToast.Type.BUTTON);
//                            toast.setDuration(SuperToast.Duration.MEDIUM);
//                            toast.setTextSize(SuperToast.TextSize.SMALL);
//                            toast.setText("已放入书架");
//                            toast.setBackground(R.color.button_compelete);
//                            toast.show();
                        } else {
                            EventBus.getDefault().post(new AddBookEvent(event.bookPath));
                        }
                    }
                }

        );
    }






    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        myCollection.unbind();
        super.onDestroy();
    }
}