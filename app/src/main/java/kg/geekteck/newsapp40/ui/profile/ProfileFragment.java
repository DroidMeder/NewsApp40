package kg.geekteck.newsapp40.ui.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.transition.TransitionInflater;

import com.bumptech.glide.Glide;

import kg.geekteck.newsapp40.MainActivity;
import kg.geekteck.newsapp40.R;
import kg.geekteck.newsapp40.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private Uri uri;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflate = TransitionInflater.from(requireContext());
        setEnterTransition(inflate.inflateTransition(R.transition.explode));
        setExitTransition(inflate.inflateTransition(R.transition.fade_in));
        System.out.println("onCreate");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        return binding.getRoot();
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
        if (id==R.id.item_clean_cash_frag){
            MainActivity.prefs.clean();
            binding.imageView.setImageURI(Uri.parse(MainActivity.prefs.getImage()));
            binding.editTextTextPersonName.setText(MainActivity.prefs.getValue());
            return true;
        }
        return false;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.imageView.setOnClickListener(v -> openYourActivity());
        initEdidtext();
        binding.editTextTextPersonName.setText(MainActivity.prefs.getValue());
    }

    private void initEdidtext() {
        binding.editTextTextPersonName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                MainActivity.prefs.putText(binding.editTextTextPersonName.getText().toString());
            }
        });
    }

    @SuppressLint("IntentReset")
    public void openYourActivity() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        launchSomeActivity.launch(intent);
    }

    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        uri = data.getData();
                        binding.imageView.setImageURI(uri);
                        MainActivity.prefs.putImage(String.valueOf(uri));
                    }
                }
            });

    @Override
    public void onStart() {
        super.onStart();
        if (MainActivity.prefs.getImage() != null) {
            uri = Uri.parse(MainActivity.prefs.getImage());
            Glide.with(requireContext()).load(uri).circleCrop().into(binding.imageView);
        }
    }
}