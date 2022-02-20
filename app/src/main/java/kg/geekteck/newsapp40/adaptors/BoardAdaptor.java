package kg.geekteck.newsapp40.adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import kg.geekteck.newsapp40.R;
import kg.geekteck.newsapp40.databinding.ItemBoardBinding;
import kg.geekteck.newsapp40.interfaces.Click;

public class BoardAdaptor extends RecyclerView.Adapter<BoardAdaptor.ViewHodler> {
    private ItemBoardBinding binding;
    private final Click onClickClick;


    public BoardAdaptor(Click onClickClick) {
        this.onClickClick = onClickClick;
    }
    private final int[] images = new int[]{R.drawable.venera,R.drawable.neptun,R.drawable.upiter};
    private final String[] titles = new String[]{"Венера", "Нуптун", "Юпитер"};
    private final String[] descriptions = new String[]{"Вторая планета",
            "Восьмая планета",
            "Пятая планета"};

    @NonNull
    @Override
    public BoardAdaptor.ViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding=ItemBoardBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new ViewHodler(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardAdaptor.ViewHodler holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public class ViewHodler extends RecyclerView.ViewHolder{
        private final ItemBoardBinding binding;

        public ViewHodler(@NonNull ItemBoardBinding itemView) {
            super(itemView.getRoot());
            binding=itemView;
        }

        public void bind(int position) {
            binding.imageView2.setImageResource(images[position]);
            binding.textTitle.setText(titles[position]);
            binding.textDesc.setText(descriptions[position]);
            if (position != getItemCount()-1){
                binding.button.setVisibility(View.GONE);
            }else {
                binding.button.setVisibility(View.VISIBLE);
                binding.button.setOnClickListener(v -> onClickClick.click());
            }
        }
    }
}
