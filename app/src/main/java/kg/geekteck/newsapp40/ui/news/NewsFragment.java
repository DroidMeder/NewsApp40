package kg.geekteck.newsapp40.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.transition.TransitionInflater;
import com.google.firebase.firestore.FirebaseFirestore;
import kg.geekteck.newsapp40.App;
import kg.geekteck.newsapp40.R;
import kg.geekteck.newsapp40.databinding.FragmentNewsBinding;
import kg.geekteck.newsapp40.models.News;

public class NewsFragment extends Fragment {
    private FragmentNewsBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflate = TransitionInflater.from(requireContext());
        setEnterTransition(inflate.inflateTransition(R.transition.fade_out));
        setExitTransition(inflate.inflateTransition(R.transition.fade_in));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnSave.setOnClickListener(v -> NewsFragment.this.save());
    }


    private void save() {
        String text = binding.etTitle.getText().toString();
        Bundle bundle = new Bundle();
        News news = new News(text, System.currentTimeMillis(), "Description");
        saveToFirestore(news);
        bundle.putSerializable("news", news);
        getParentFragmentManager().setFragmentResult("rk_news", bundle);
        App.dataBase.newsDao().insertNews(news);
    }

    private void saveToFirestore(News news) {
        binding.progressCircular.setVisibility(View.VISIBLE);
        FirebaseFirestore.getInstance()
                .collection("news")
                .add(news)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        binding.progressCircular.setVisibility(View.GONE);
                        Toast.makeText(requireActivity(), "Успешно", Toast.LENGTH_SHORT).show();
                        close();
                    } else {
                        Toast.makeText(requireActivity(), "Ошибка", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void close() {
        NavController navController = Navigation.findNavController(requireActivity(),
                R.id.nav_host_fragment_activity_main);
        navController.navigateUp();
    }
}