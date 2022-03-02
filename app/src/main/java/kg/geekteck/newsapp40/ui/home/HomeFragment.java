package kg.geekteck.newsapp40.ui.home;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import kg.geekteck.newsapp40.App;
import kg.geekteck.newsapp40.ui.models.News;
import kg.geekteck.newsapp40.R;
import kg.geekteck.newsapp40.adaptors.HomeAdaptor;
import kg.geekteck.newsapp40.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HomeAdaptor homeAdaptor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeAdaptor = new HomeAdaptor();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
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
        List<News> newsList = App.dataBase.newsDao().getAllNews();
        homeAdaptor.addItems(newsList);

        getParentFragmentManager().setFragmentResultListener("rk_news", getViewLifecycleOwner(), (requestKey, result) -> {
            /*News n = (News) result.getSerializable("news");
            homeAdaptor.addItem(n);*/
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss, dd MMM yyyy", Locale.ROOT);
            //String str = String.valueOf(simpleDateFormat.format(n.getCreatedAt()));
            //Log.e("Home", "Title: " +n.getTitle()+ "; CreatedAt: "+str);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}