package kg.geekteck.newsapp40.ui.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;

import kg.geekteck.newsapp40.R;
import kg.geekteck.newsapp40.databinding.FragmentProfileBinding;
import kg.geekteck.newsapp40.ui.models.Prefs;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private Uri uri;
    private Prefs prefs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate");

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.imageView.setOnClickListener(v -> openYourActivity());
        prefs=new Prefs(requireContext());
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
                        prefs.putValues(String.valueOf(uri),
                                binding.editTextTextPersonName.getText().toString());
                    }
                }
            });

    public String setProfileImage(){
        binding.imageView.buildDrawingCache();
        Bitmap bitmap = binding.imageView.getDrawingCache();
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        byte[] image=stream.toByteArray();
        return Base64.encodeToString(image, 0);
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("onStart");
        if (prefs.getImage()!=null) {
            uri = Uri.parse(prefs.getImage());
            Glide.with(requireContext()).load(uri).circleCrop().into(binding.imageView);
        }
        String a = prefs.getValue();
        binding.editTextTextPersonName.setText(a);
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("onPause");
        prefs.putValues(String.valueOf(uri),
                binding.editTextTextPersonName.getText().toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy");
        prefs.putValues(String.valueOf(uri),
                binding.editTextTextPersonName.getText().toString());
    }
}