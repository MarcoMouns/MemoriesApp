package dude.memories.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dude.memories.Models.MemoryModel;
import dude.memories.R;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    Context mContext;

    public CustomInfoWindowAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popUp = inflater.inflate(R.layout.mapitem_memory, null);
        Gson gson = new Gson();
        String info = marker.getSnippet();
        MemoryModel memoryModel = gson.fromJson(info, new TypeToken<MemoryModel>() {
        }.getType());
        TextView title = popUp.findViewById(R.id.item_title);
        ImageView image = popUp.findViewById(R.id.item_image);
        TextView description = popUp.findViewById(R.id.item_description);
        title.setText(memoryModel.getTitle());
        description.setText(memoryModel.getDescription());
        if (memoryModel.getPicture() != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(memoryModel.getPicture(), 0, memoryModel.getPicture().length);
            image.setImageBitmap(bmp);
        }
        return popUp;
    }
}
