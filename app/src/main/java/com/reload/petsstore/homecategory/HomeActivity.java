package com.reload.petsstore.homecategory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.reload.petsstore.R;
import com.reload.petsstore.aboutus.AboutFragment;
import com.reload.petsstore.common.SessionMangment;
import com.reload.petsstore.databinding.ActivityHomeBinding;
import com.reload.petsstore.fav.FavFragment;
import com.reload.petsstore.homecategory.homeFragment.HomeFragment;
import com.reload.petsstore.settings.settingsScreen.SettingsFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    public static int navItemIndex = 0;
    SessionMangment mSessionMangment;
    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_home);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        mSessionMangment = new SessionMangment(this);
        initViews();
        loadFragment(new HomeFragment(), "Home", 0);
    }

    private void initViews() {
        binding.opneDrawer.setOnClickListener(this);
        binding.homeItem.setOnClickListener(this);
        binding.favItem.setOnClickListener(this);
        binding.historyItem.setOnClickListener(this);
        binding.settingsItem.setOnClickListener(this);
        binding.aboutItem.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.opne_drawer:
                binding.drawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.home_item:
                loadFragment(new HomeFragment(), "Home", 0);
                break;
            case R.id.fav_item:
                loadFragment(new FavFragment(), "Favourites", 1);
                break;
            case R.id.history_item:

                break;
            case R.id.settings_item:
                loadFragment(new SettingsFragment(), "Settings", 3);
                break;
            case R.id.about_item:
                loadFragment(new AboutFragment(), "About us", 4);
                break;

        }


    }

    void loadFragment(Fragment fragment, String pageTitle, int index) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.page_container, fragment).commit();
        binding.pageTitle.setText(pageTitle);
        binding.drawerLayout.closeDrawers();
        navItemIndex = index;

    }


    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            binding.drawerLayout.closeDrawer(Gravity.LEFT);
            return;
        }
        if (true) {
            if (navItemIndex != 0) {
                navItemIndex = 0;
                loadFragment(new HomeFragment(), "Home", 0);
                return;
            }
        }
        super.onBackPressed();
    }


    @Override
    protected void onResume() {
        super.onResume();
        binding.userName.setText(mSessionMangment.getUserDetails().get(SessionMangment.KEY_FNAME) + " " + mSessionMangment.getUserDetails().get(SessionMangment.KEY_LNAME));
        Glide.with(HomeActivity.this)
                .load(mSessionMangment.getUserDetails().get(SessionMangment.KEY_IMAGE))
                .placeholder(getResources().getDrawable(R.drawable.avatar))
                .into(binding.userImg);


    }

}
