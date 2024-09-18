package legendary.streamer.firebasedbexample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.LoadState;
import androidx.paging.PagingConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import legendary.streamer.firebasedbexample.model.ResponseModel;

public class MainActivity extends AppCompatActivity {

    private ProgressBar loader;

    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loader = findViewById(R.id.loader);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("dummy");
        //myRef.keepSynced(true);


        PagingConfig config = new PagingConfig(10, 2, false);

        //Initialize Firebase Paging Options
        DatabasePagingOptions<ResponseModel> options = new DatabasePagingOptions.Builder<ResponseModel>()
                .setLifecycleOwner(this)
                .setQuery(myRef, config, ResponseModel.class)
                .build();


        FirebaseRecyclerPagingAdapter<ResponseModel, ViewHolder> mAdapter = new FirebaseRecyclerPagingAdapter<ResponseModel, ViewHolder>(options) {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder (@NonNull ViewGroup parent,int viewType){
                View _v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
                _v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
                return new ViewHolder(_v);
            }

            @Override
            protected void onBindViewHolder (@NonNull ViewHolder holder,int position,@NonNull ResponseModel model){

                holder.name.setText(model.getName());
                holder.bio.setText(model.getBio());
                holder.language.setText(model.getLanguage());
                holder.version.setText(model.getVersion().toString());

            }
        };
        recyclerView.setAdapter(mAdapter);

        mAdapter.addLoadStateListener(states -> {
            LoadState refresh = states.getRefresh();
            LoadState append = states.getAppend();

            if (refresh instanceof LoadState.Error) {
                loader.setVisibility(View.GONE);
            }
            if (append instanceof LoadState.Error) {
                loader.setVisibility(View.GONE);
            }

            if (append instanceof LoadState.Loading) {
                loader.setVisibility(View.VISIBLE);
            }

            if (append instanceof LoadState.NotLoading) {
                LoadState.NotLoading notLoading = (LoadState.NotLoading) append;
                if (notLoading.getEndOfPaginationReached()) {
                    // This indicates that the user has scrolled
                    // until the end of the data set.
                    loader.setVisibility(View.GONE);
                    showToast("End of data set");
                    return null;
                }

                if (refresh instanceof LoadState.NotLoading) {
                    // This indicates the most recent load
                    // has finished.
                    loader.setVisibility(View.GONE);
                    return null;
                }
            }
            return null;
        });

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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