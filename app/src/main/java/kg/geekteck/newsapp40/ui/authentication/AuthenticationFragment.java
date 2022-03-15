package kg.geekteck.newsapp40.ui.authentication;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.transition.TransitionInflater;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import kg.geekteck.newsapp40.R;
import kg.geekteck.newsapp40.databinding.FragmentAuthenticationBinding;

public class AuthenticationFragment extends Fragment { //} implements GoogleApiClient.ConnectionCallbacks,
    //OtpReceivedInterface, GoogleApiClient.OnConnectionFailedListener {
    private FragmentAuthenticationBinding binding;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;
    private String verificationId;
    EditText inputMobileNumber, inputOtp;
    //private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=create();

    /*GoogleApiClient mGoogleApiClient;
    SmsBroadcastReceiver mSmsBroadcastReceiver;
    private int RESOLVE_HINT = 2;
    Button btnGetOtp, btnVerifyOtp;
    ConstraintLayout layoutInput, layoutVerify;*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transition();
        //mSmsBroadcastReceiver = new SmsBroadcastReceiver();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireContext());
        mAuth = FirebaseAuth.getInstance();
        // Force reCAPTCHA flow
        //FirebaseAuth.getInstance().getFirebaseAuthSettings().forceRecaptchaFlowForTesting(true);

//        //TO DO SOMETHING
        //create();
    }

    private void transition() {
        TransitionInflater inflate = TransitionInflater.from(requireContext());
        setEnterTransition(inflate.inflateTransition(R.transition.fade));
        setExitTransition(inflate.inflateTransition(R.transition.slide_right));
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAuthenticationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        analytic();
        buttonNumber();


        //.mSms();
        quit();
    }

    private void quit() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        requireActivity().finish();
                    }
                });
    }

    /*private void mSms() {
        mSmsBroadcastReceiver.setOnOtpListeners(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        requireContext().registerReceiver(mSmsBroadcastReceiver, intentFilter);
    }*/

    private void buttonNumber() {
        System.out.println("buttonNumber()");
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = Objects.requireNonNull(binding.tiedNumber.getText()).toString().trim();
                System.out.println("data   ---= = = " + data);
                if (TextUtils.isEmpty(data)) {
                    binding.tilNumber.setError("Введите номер правильно");
                    return;
                } else {
                    binding.tilNumber.setVisibility(View.INVISIBLE);
                    binding.tilNumber.setError(null);
                    binding.tilCode.setVisibility(View.VISIBLE);
                }
                binding.btnSend.setText("Отправить");
                register("+" + data);
                checkingSMSCode("");


                //.startSMSListener();
            }
        });
    }

    private void startSMSListener() {
        SmsRetrieverClient mClient = SmsRetriever.getClient(requireContext());
        Task<Void> mTask = mClient.startSmsRetriever();
        mTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                showSnackbar("SMS Retriever starts --- " + unused);
            }
        });
        mTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showSnackbar("Error ---" + e);
            }
        });
    }

    public void checkingSMSCode(String otp) {
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mOtp = Objects.requireNonNull(binding.tiedCode.getText()).toString().trim();
                System.out.println("Code   ----- " + otp);
                if (mOtp.isEmpty() || mOtp.length() < 6) {
                    binding.tilCode.setError("Введите правильный код");
                    binding.tiedCode.requestFocus();
                    return;
                } else {
                    binding.tilCode.setError(null);
                }
                verifyCode(mOtp);
                /*PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
                // Turn off phone auth app verification.
                FirebaseAuth.getInstance().getFirebaseAuthSettings()
                        .setAppVerificationDisabledForTesting(true);
                signInWithPhoneAuthCredential(credential);*/
            }
        });
    }

    private void verifyCode(String mOtp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, mOtp);
        // Turn off phone auth app verification.
        /*FirebaseAuth.getInstance().getFirebaseAuthSettings()
                .setAppVerificationDisabledForTesting(true);*/
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //FirebaseUser user = task.getResult().getUser();
                            showSnackbar("signInWithCredential:success");

                            NavController navController = Navigation.findNavController(AuthenticationFragment.this.requireActivity(),
                                    R.id.nav_host_fragment_activity_main);
                            navController.navigate(R.id.boardFragment);
                        } else {
                            showSnackbar("signInWithCredential:failure" + task.getException());
                        }
                    }
                });
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            showSnackbar("onVerificationCompleted:" + phoneAuthCredential);
            String code = phoneAuthCredential.getSmsCode();
            binding.tiedCode.setText(code);
            verifyCode(code);
            //signInWithPhoneAuthCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                showSnackbar("onVerificationFailed " + e);
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                showSnackbar("onVerificationFailed " + e);
            }
            showSnackbar("onVerificationFailed " + e);
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken
                forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            System.out.println("onCodeSent:" + s);
            showSnackbar("onCodeSent:" + s);
        }
    };

    private void showSnackbar(String s) {
        Snackbar snackbar = Snackbar.make(requireView(), s, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public void register(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                requireActivity(),//(Activity) TaskExecutors.MAIN_THREAD,
                mCallbacks
        );
        /* PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(requireActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.getInstance();
        PhoneAuthProvider.verifyPhoneNumber(options);*/
    }

    private void analytic() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "name");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}
/*

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onOtpReceived(String otp) {
        showSnackbar("Otp Received --- "+otp);
        checkingSMSCode(otp);
    }

    @Override
    public void onOtpTimeout() {
        showSnackbar( "Time out, please resend");
    }
}*/
