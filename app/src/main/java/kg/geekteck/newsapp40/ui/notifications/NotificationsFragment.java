package kg.geekteck.newsapp40.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.transition.TransitionInflater;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kg.geekteck.newsapp40.App;
import kg.geekteck.newsapp40.R;
import kg.geekteck.newsapp40.adaptors.NotificationsAdaptor;
import kg.geekteck.newsapp40.databinding.FragmentNotificationsBinding;
import kg.geekteck.newsapp40.models.News;

public class NotificationsFragment extends Fragment {
    private FragmentNotificationsBinding binding;
    private NotificationsAdaptor notificationsAdaptor;
    List<News> newsList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflate = TransitionInflater.from(requireContext());
        setEnterTransition(inflate.inflateTransition(R.transition.auto_transition));
        setExitTransition(inflate.inflateTransition(R.transition.slide_left));
        notificationsAdaptor = new NotificationsAdaptor();
        newsList=new ArrayList<>();

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getFromFirestore();
        binding.fabNot.setOnClickListener(v1 -> {
            NavController controller = Navigation.findNavController(NotificationsFragment
                    .this.requireActivity(), R.id.nav_host_fragment_activity_main);
            controller.navigate(R.id.newsFragment);
        });
        System.out.println(" binding.recNot.setAdapter(notificationsAdaptor);");
        binding.recNot.setAdapter(notificationsAdaptor);
        getFromFirestore();
        System.out.println("getFromFirestore();");
        System.out.println(newsList.isEmpty());
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
            deleteFirestore();
            getFromFirestore();
            return true;
        }
        return false;
    }

    private void deleteFirestore() {
        FirebaseFirestore.getInstance()
                .collection("news")
                .document().delete();
    }

    private void getFromFirestore() {
        FirebaseFirestore.getInstance()
                .collection("news")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            newsList=new ArrayList<>();
                            System.out.println("task ----" + task);
                            for (QueryDocumentSnapshot query: task.getResult()
                            ) {
                                String title = Objects.requireNonNull(query.getData().get("title")).toString();
                                String desc = Objects.requireNonNull(query.getData().get("description")).toString();
                                long date = Long.parseLong(Objects.requireNonNull(query.getData().get("createdAt")).toString());
                                newsList.add(new News(title,date,desc));
                                System.out.println("query ----"+query.getId() + " => " + query.getData());
                            }
                            binding.progressCircular.setVisibility(View.GONE);
                            binding.frame.setVisibility(View.GONE);
                        } else {
                            System.out.println("not successful mfg");
                        }
                        System.out.println("hght435345");
                        System.out.println(newsList.isEmpty());
                        notificationsAdaptor.addItems(newsList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("fault  "+e);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}