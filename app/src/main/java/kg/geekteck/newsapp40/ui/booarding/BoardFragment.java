package kg.geekteck.newsapp40.ui.booarding;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.transition.TransitionInflater;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayoutMediator;

import kg.geekteck.newsapp40.MainActivity;
import kg.geekteck.newsapp40.R;
import kg.geekteck.newsapp40.adaptors.BoardAdaptor;
import kg.geekteck.newsapp40.databinding.FragmentBoardBinding;
import kg.geekteck.newsapp40.interfaces.Click;

public class BoardFragment extends Fragment implements Click {
    private FragmentBoardBinding binding;
    private BoardAdaptor boardAdaptor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflate = TransitionInflater.from(requireContext());
        setEnterTransition(inflate.inflateTransition(R.transition.slide_up));
        setExitTransition(inflate.inflateTransition(R.transition.slide_down));
        boardAdaptor=new BoardAdaptor(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBoardBinding.inflate(inflater, container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.viewPager2.setAdapter(boardAdaptor);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        requireActivity().finish();
                    }
                });

        new TabLayoutMediator(binding.tabLay, binding.viewPager2, (tab, position) -> {
            if (tab.isSelected()) {
                tab.setIcon(R.drawable.ic_circle_selkected);
            }else {
                tab.setIcon(R.drawable.ic_circle_black);
            }
        }).attach();
        pager();
        binding.tvSkip.setOnClickListener(v -> click());
    }

    private void pager() {
        binding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == boardAdaptor.getItemCount()-1){
                    binding.tvSkip.setVisibility(View.INVISIBLE);
                    binding.tvSkip.setClickable(false);
                }else {
                    binding.tvSkip.setVisibility(View.VISIBLE);
                    binding.tvSkip.setClickable(true);
                }
            }
        });
    }

    @Override
    public void click() {
        //Prefs prefs = new Prefs(requireContext());
        MainActivity.prefs.saveBoardState();
        NavController navController = Navigation.findNavController(BoardFragment.this.requireActivity(),
                R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.navigation_home);
    }
}