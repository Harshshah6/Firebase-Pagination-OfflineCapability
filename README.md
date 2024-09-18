# Firebase-Pagination-OfflineCapability
An example project of pagination with offline capability using firebase in android application, written in java | kotlin


## Dependencies Used (external dependencies)
Firebase core / realtime database dependency are not listed here, please include the core and db dependency before using below libraries.
``` gradle
implementation 'com.firebaseui:firebase-ui-database:8.0.2'
implementation 'androidx.paging:paging-runtime:3.3.2'
```

## Instructions
### Declaring and Initializing variables
Firebase related variables :-
```java
FirebaseDatabase database = FirebaseDatabase.getInstance();
DatabaseReference myRef = database.getReference("dummy");
```

Paging related variables :-
```java
PagingConfig config = new PagingConfig(
    10, // Item Load Limits 
    2, // Prefetch items limit before next load/scroll
    false // Placeholders (Please refer official doc for more info)
);
```

```java
//Initialize Firebase Paging Options
DatabasePagingOptions<ResponseModel> options = new DatabasePagingOptions.Builder<ResponseModel>() // ResponseModel = Model class of your response data
    .setLifecycleOwner(this) //this = Context
    .setQuery(myRef, config, ResponseModel.class) //myRef = DatabaseReference
    .build();
```

Adapter for RecyclerView
```java
//ResponseModel = Model class
//ViewHolder = ViewHolder class explicitly written by you
//options = DatabasePagingOptions<>()
FirebaseRecyclerPagingAdapter<ResponseModel, ViewHolder> mAdapter = new FirebaseRecyclerPagingAdapter<ResponseModel, ViewHolder>(options) {
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder (@NonNull ViewGroup parent,int viewType){
    ...
    }
@Override
protected void onBindViewHolder (@NonNull ViewHolder holder,int position,@NonNull ResponseModel model){
...
}
};
recyclerView.setAdapter(mAdapter); //setting adapter to RecyclerView
```

Listening to the scroll and states
```java
mAdapter.addLoadStateListener(states -> {
    LoadState refresh = states.getRefresh();
    LoadState append = states.getAppend();

    if (refresh instanceof LoadState.Error || append instanceof LoadState.Error) {
        // There might be some error
        // Request to database error 
        // OR
        // No Network Available
    }
    
    if (append instanceof LoadState.Loading) {
        //Next Items Loading....
    }

    if (append instanceof LoadState.NotLoading) {
        LoadState.NotLoading notLoading = (LoadState.NotLoading) append;
        if (notLoading.getEndOfPaginationReached()) {
            // This indicates that the user has scrolled
            // until the end of the data set.
            return null;
        }
        if (refresh instanceof LoadState.NotLoading) {
            // This indicates the most recent load
            // has finished.
            return null;
        }
    }
    return null;
});
```

## To Enable Offline Capabality
In the example app this code was added in <u>MyApplication.java</u> class extending Application
```java
//enable firebase offline capabilities
FirebaseDatabase.getInstance().setPersistenceEnabled(true);
```