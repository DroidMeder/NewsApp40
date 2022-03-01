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

import java.io.ByteArrayOutputStream;

import kg.geekteck.newsapp40.R;
import kg.geekteck.newsapp40.databinding.FragmentProfileBinding;
import kg.geekteck.newsapp40.ui.models.Prefs;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private Prefs prefs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                        Uri selectedImage = data.getData();
                        binding.imageView.setImageURI(selectedImage);
                        prefs.putValues(setProfileImage(),
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
    public void onPause() {
        super.onPause();
        System.out.println("onPause");
        prefs.putValues(setProfileImage(),
                binding.editTextTextPersonName.getText().toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy");
        prefs.putValues(setProfileImage(),
                binding.editTextTextPersonName.getText().toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("onResume");
        String s1 = prefs.getImage(setProfileImage());
        byte[] imageAsBytes = Base64.decode(s1.getBytes(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes,0, imageAsBytes.length);
        binding.imageView.setImageBitmap(bitmap);

        String a = prefs.getValue();
        binding.editTextTextPersonName.setText(a);
    }

}