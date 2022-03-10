package kg.geekteck.newsapp40.ui.dashboard;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.transition.TransitionInflater;


import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import kg.geekteck.newsapp40.R;
import kg.geekteck.newsapp40.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {
    private FragmentDashboardBinding binding;
    private boolean isNotEmptyUsername=false, isNotEmptyPassword = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflate = TransitionInflater.from(requireContext());
        setEnterTransition(inflate.inflateTransition(R.transition.fade));
        setExitTransition(inflate.inflateTransition(R.transition.slide_right));

    }

    @Nullable
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        changeListener();
        click();
    }

    private void click() {
        binding.btnYoyo.setOnClickListener(v -> {
            if (isNotEmptyUsername){
                changeListener();
                binding.tilUsername.setError(null);
                if (isNotEmptyPassword){
                    binding.tilPassword.setError(null);
                    System.out.println("GOOOOd");
                }else {
                    binding.tilPassword.setError("Длина пароля не должен быть меньше 6 символов!!!");
                    YoYo.with(Techniques.Bounce)
                            .duration(700)
                            .repeat(3)
                            .playOn(binding.tilPassword);
                }
            }else {
                binding.tilUsername.setError("Имя не может быть пустым!!!");
                YoYo.with(Techniques.Shake)
                        .duration(700)
                        .repeat(2)
                        .playOn(binding.tilUsername);
            }
        });
    }

    private void changeListener() {
        binding.tiedUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isNotEmptyUsername = !s.toString().isEmpty();
                binding.tilUsername.setError(null);
            }
        });
        binding.tiedPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty() && s.toString().length() > 6) {
                    isNotEmptyPassword = true;
                    binding.tilPassword.setError(null);
                }else isNotEmptyPassword=false;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}