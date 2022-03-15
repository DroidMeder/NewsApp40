package kg.geekteck.newsapp40.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.transition.TransitionInflater;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import kg.geekteck.newsapp40.App;
import kg.geekteck.newsapp40.R;
import kg.geekteck.newsapp40.adaptors.HomeAdaptor;
import kg.geekteck.newsapp40.databinding.FragmentHomeBinding;
import kg.geekteck.newsapp40.models.News;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HomeAdaptor homeAdaptor;
    private List<News> newsList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflate = TransitionInflater.from(requireContext());
        setExitTransition(inflate.inflateTransition(R.transition.fade));
        setEnterTransition(inflate.inflateTransition(R.transition.explode));
        homeAdaptor = new HomeAdaptor();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.fab.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(HomeFragment.this.requireActivity(),
                    R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.newsFragment);
        });

        binding.rec.setAdapter(homeAdaptor);
        newsList = App.dataBase.newsDao().getAllNews();
        homeAdaptor.addItems(newsList);

        getParentFragmentManager().setFragmentResultListener("rk_news", getViewLifecycleOwner(),
                (requestKey, result) -> {
                    /*News n = (News) result.getSerializable("news");
                        homeAdaptor.addItem(n);*/
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss, dd MMM yyyy", Locale.ROOT);
                    //String str = String.valueOf(simpleDateFormat.format(n.getCreatedAt()));
                    //Log.e("Home", "Title: " +n.getTitle()+ "; CreatedAt: "+str);
                });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.action_bar_menu_fragment, menu);
        menu.removeItem(R.id.item_clean_cash);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_clean_cash_frag) {
            App.dataBase.newsDao().delete(newsList);
            homeAdaptor.addItems(newsList);
            binding.rec.setAdapter(homeAdaptor);
            return true;
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}