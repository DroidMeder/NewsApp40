package kg.geekteck.newsapp40.adaptors;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import kg.geekteck.newsapp40.databinding.Item1Binding;
import kg.geekteck.newsapp40.databinding.ItemBinding;
import kg.geekteck.newsapp40.models.News;

public class NotificationsAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<News> list;
    ItemBinding binding2;
    Item1Binding binding3;

    public NotificationsAdaptor() {
        list = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            binding2 = ItemBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new VH1(binding2);
        } else {
            binding3 = Item1Binding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new VH2(binding3);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 0) {
            ((VH1) holder).bind1(list.get(position));
        } else {
            ((VH2) holder).bind2(list.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(News n) {
        list.add(0, n);
        notifyItemInserted(0);
    }
    @SuppressLint("NotifyDataSetChanged")
    public void addItems(List<News> newsList){
        System.out.println(newsList.size());
        Comparator<News> comparator = Comparator.comparing(News::getCreatedAt);
        newsList.sort(comparator);
        list= newsList;
        System.out.println(list.size());
        notifyDataSetChanged();
    }

    protected static class VH1 extends RecyclerView.ViewHolder {
        ItemBinding binding2;

        public VH1(@NonNull ItemBinding itemView) {
            super(itemView.getRoot());
            binding2 = itemView;
        }

        @SuppressLint("SetTextI18n")
        public void bind1(News news) {
            System.out.println("bind1 -- "+news);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss, dd MMM yyyy", Locale.ROOT);
            String str = String.valueOf(simpleDateFormat.format(news.getCreatedAt()));
            binding2.tvTitle.setText(news.getTitle()+"              "+str);
            System.out.println(binding2.tvTitle.getText());
        }
    }

    protected static class VH2 extends RecyclerView.ViewHolder {
        Item1Binding binding3;

        public VH2(@NonNull Item1Binding itemView) {
            super(itemView.getRoot());
            binding3 = itemView;
        }

        public void bind2(News news) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss, dd MMM yyyy", Locale.ROOT);
            String str = String.valueOf(simpleDateFormat.format(news.getCreatedAt()));
            binding3.tvTitle1.setText(String.format("%s          %s", news.getTitle(), str));
            System.out.println(binding3.tvTitle1.getText());
        }
    }
}
