package red.moccasins.nonogramgame;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import red.moccasins.nonogramgame.nono.NonoView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NonoView mNonogramTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
// Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);


        Toolbar toolbar = (Toolbar) findViewById(R.id.topToolbar);
        setSupportActionBar(toolbar);

        initToolbars();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Здесь выводится сообщение об ошибках", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mNonogramTable = (NonoView) findViewById(R.id.nonogram);

        initializeNonogram();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initializeNonogram() {
        InputStream is = getResources().openRawResource(R.raw.task);
        String task = null;
        try {
            task = convertStreamToString(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mNonogramTable.InitNewNonogram(task);
    }

    private String  convertStreamToString(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = is.read();
        while( i != -1)
        {
            baos.write(i);
            i = is.read();
        }
        return  baos.toString();
    }

    private void initToolbars() {
        Toolbar toolbarBottomLeft = (Toolbar) findViewById(R.id.bottomToolbarLeft);
        Toolbar toolbarBottomCenter = (Toolbar) findViewById(R.id.bottomToolbarCenter);
        Toolbar toolbarBottomRight = (Toolbar) findViewById(R.id.bottomToolbarRight);

        Toolbar.OnMenuItemClickListener listener = new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_fill:
                        mNonogramTable.setInputMode(1);
                        break;
                    case R.id.action_cross:
                        mNonogramTable.setInputMode(2);
                        break;
                    case R.id.action_space:
                        mNonogramTable.setInputMode(0);
                        break;
                    case R.id.action_undo:
                        mNonogramTable.Undo();
                        break;
                    case R.id.action_redo:
                        mNonogramTable.Redo();
                        break;
                    default:
                        break;
                }
                return true;
            }
        };

        toolbarBottomLeft.setOnMenuItemClickListener(listener);
        toolbarBottomCenter.setOnMenuItemClickListener(listener);
        toolbarBottomRight.setOnMenuItemClickListener(listener);

        // Inflate a menu to be displayed in the toolbar
        toolbarBottomLeft.inflateMenu(R.menu.options_menu_left);
        toolbarBottomCenter.inflateMenu(R.menu.options_menu_center);
        toolbarBottomRight.inflateMenu(R.menu.options_menu_right);
    }

}
