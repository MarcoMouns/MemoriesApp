package dude.memories.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import dude.memories.Models.MemoryModel;
import dude.memories.R;

public class MemoriesListAdapter extends RecyclerView.Adapter<MemoriesListAdapter.ViewHolder> {
    Context context;
    List<MemoryModel> memoryModelList;

    public MemoriesListAdapter(Context context, List<MemoryModel> memoryModelList) {
        this.context = context;
        this.memoryModelList = memoryModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_memory, viewGroup, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        MemoryModel memoryModel = memoryModelList.get(i);
        viewHolder.title.setText(memoryModel.getTitle());
        viewHolder.description.setText(memoryModel.getDescription());
        if (memoryModel.getPicture() != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(memoryModel.getPicture(), 0, memoryModel.getPicture().length);
            viewHolder.image.setImageBitmap(bmp);
        }

    }

    @Override
    public int getItemCount() {
        return memoryModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;
        public TextView description;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            image = itemView.findViewById(R.id.item_image);
            description = itemView.findViewById(R.id.item_description);

        }
    }
}
