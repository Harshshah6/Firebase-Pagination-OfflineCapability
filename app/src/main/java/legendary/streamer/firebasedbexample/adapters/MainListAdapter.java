package legendary.streamer.firebasedbexample.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import legendary.streamer.firebasedbexample.R;
import legendary.streamer.firebasedbexample.model.ResponseModel;

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.ViewHolder> {

    private final ArrayList<ResponseModel> data;

    public MainListAdapter(ArrayList<ResponseModel> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public MainListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View _v = View.inflate(parent.getContext(), R.layout.list_item, null);
        _v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(_v);
    }

    @Override
    public void onBindViewHolder(@NonNull MainListAdapter.ViewHolder holder, int position) {
        holder.name.setText(data.get(position).getName());
        holder.bio.setText(data.get(position).getBio());
        holder.language.setText(data.get(position).getLanguage());
        holder.version.setText(data.get(position).getVersion().toString());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView name;
        public final TextView bio;
        public final TextView language;
        public final TextView version;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            bio = itemView.findViewById(R.id.bio);
            language = itemView.findViewById(R.id.languauge);
            version = itemView.findViewById(R.id.version);
        }
    }
}
