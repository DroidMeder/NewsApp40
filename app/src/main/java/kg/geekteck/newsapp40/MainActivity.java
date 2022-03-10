package kg.geekteck.newsapp40;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Objects;

import kg.geekteck.newsapp40.databinding.ActivityMainBinding;
import kg.geekteck.newsapp40.ui.models.Prefs;
import kg.geekteck.newsapp40.ui.profile.ProfileFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    public static Prefs prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = new Prefs(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard,
                R.id.navigation_notifications, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        //Prefs prefs = new Prefs(this);
        if (!prefs.isBoardShown()) {
            navController.navigate(R.id.boardFragment);
        }
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController1, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                if (navDestination.getId() == R.id.boardFragment) {
                    binding.navView.setVisibility(View.GONE);
                    Objects.requireNonNull(MainActivity.this.getSupportActionBar()).hide();
                } else {
                    binding.navView.setVisibility(View.VISIBLE);
                    Objects.requireNonNull(MainActivity.this.getSupportActionBar()).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_clean_cash:
                prefs.clean();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}