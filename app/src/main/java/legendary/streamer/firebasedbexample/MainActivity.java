package legendary.streamer.firebasedbexample;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.LoadState;
import androidx.paging.PagedList;
import androidx.paging.PagingConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import legendary.streamer.firebasedbexample.adapters.MainListAdapter;
import legendary.streamer.firebasedbexample.model.ResponseModel;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("dummy");

        ArrayList<ResponseModel> data = new ArrayList<>();

//        myRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
////                for (DataSnapshot ds : snapshot.getChildren()) {
////                    ResponseModel model = ds.getValue(ResponseModel.class);
////                    data.add(model);
////                    recyclerView.setAdapter(new MainListAdapter(data));
////                }
//                ResponseModel model = snapshot.getValue(ResponseModel.class);
//                data.add(model);
//                recyclerView.setAdapter(new MainListAdapter(data));
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        PagingConfig config = new PagingConfig(10, 2, false);

        //Initialize Firebase Paging Options
        DatabasePagingOptions<ResponseModel> options = new DatabasePagingOptions.Builder<ResponseModel>()
                .setLifecycleOwner(this)
                .setQuery(myRef, config, ResponseModel.class)
                .build();



        FirebaseRecyclerPagingAdapter<ResponseModel, MainListAdapter.ViewHolder> mAdapter = new FirebaseRecyclerPagingAdapter<ResponseModel, MainListAdapter.ViewHolder>(options) {
            @NonNull
            @Override
            public MainListAdapter.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent,int viewType){
                View _v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
                _v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
                return new MainListAdapter.ViewHolder(_v);
            }

            @Override
            protected void onBindViewHolder (@NonNull MainListAdapter.ViewHolder holder,int position,@NonNull ResponseModel model){

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
                LoadState.Error loadStateError = (LoadState.Error) refresh;
                //mBinding.swipeRefreshLayout.setRefreshing(false);
                Log.e(TAG, loadStateError.getError().getLocalizedMessage());
                showToast("Error loading data...");
            }
            if (append instanceof LoadState.Error) {
                LoadState.Error loadStateError = (LoadState.Error) append;
                //mBinding.swipeRefreshLayout.setRefreshing(false);
                Log.e(TAG, loadStateError.getError().getLocalizedMessage());
                showToast("Error loading more data...");

            }

            if (append instanceof LoadState.Loading) {
                //mBinding.swipeRefreshLayout.setRefreshing(true);
                showToast("Loading more data...");
            }

            if (append instanceof LoadState.NotLoading) {
                LoadState.NotLoading notLoading = (LoadState.NotLoading) append;
                if (notLoading.getEndOfPaginationReached()) {
                    // This indicates that the user has scrolled
                    // until the end of the data set.
                    //mBinding.swipeRefreshLayout.setRefreshing(false);
                    showToast("Reached end of data set.");
                    return null;
                }

                if (refresh instanceof LoadState.NotLoading) {
                    // This indicates the most recent load
                    // has finished.
                    //mBinding.swipeRefreshLayout.setRefreshing(false);
                    showToast("Reached end of data set.");
                    return null;
                }
            }
            return null;
        });

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}