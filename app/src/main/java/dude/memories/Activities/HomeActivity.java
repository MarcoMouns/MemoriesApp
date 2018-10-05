package dude.memories.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import dude.memories.Fragments.HomeFragment;
import dude.memories.R;
import es.dmoral.toasty.Toasty;

public class HomeActivity extends AppCompatActivity {
    long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        HomeFragment fragment = new HomeFragment();
        showFragment(fragment);
    }

    public void showFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.HomeActivity, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {

            fm.popBackStack();
        } else {
            if (time + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
                finish();
            } else {
                Toasty.info(this, "Press Back Again.", Toast.LENGTH_SHORT, true).show();
            }
            time = System.currentTimeMillis();
        }
    }
}
