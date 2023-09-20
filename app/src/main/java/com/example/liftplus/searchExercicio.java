package com.example.liftplus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

public class searchExercicio extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<exercicioRepository>>{

    private ListView dataListView;
    private EditText requestTag;
    private TextView errorMessage;
    private ProgressBar loadingBar;
    private exercicioAdapter adapter;

    private static final int exercicio_search_loader = 1;
    private static final String exercicio_query_tag = "query";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_exercicio);

        loadingBar = findViewById(R.id.loadingBar);
        requestTag = findViewById(R.id.requestTag);
        errorMessage = findViewById(R.id.errorMessage);


        dataListView = findViewById(R.id.dataListView);
        dataListView.setEmptyView(errorMessage);

        adapter = new exercicioAdapter(getApplicationContext());

        if (savedInstanceState != null) {
            String queryUrl = savedInstanceState.getString(exercicio_query_tag);
            requestTag.setText(queryUrl);
        }
        getSupportLoaderManager().initLoader(exercicio_search_loader, null, this);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString(exercicio_query_tag, requestTag.getText().toString().trim());
    }

    public Loader<List<exercicioRepository>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<List<exercicioRepository>>(this) {

            List<exercicioRepository> mExercicioList;

            @Override
            protected void onStartLoading() {

                if(args == null) {
                    return;
                }
                loadingBar.setVisibility(View.VISIBLE);
                if(mExercicioList != null) {
                    deliverResult(mExercicioList);
                }else{
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public List<exercicioRepository> loadInBackground() {
                String searchQueryUrlString = args.getString(exercicio_query_tag);
                try {
                    List<exercicioRepository> exercicioSearchResults = NetworkUtils.getDataFromApi(searchQueryUrlString);
                    return exercicioSearchResults;
                }catch(IOException e){
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(@Nullable List<exercicioRepository> exercicioJson) {
                mExercicioList = exercicioJson;
                super.deliverResult(exercicioJson);
            }
        };
    }

    public void onLoadFinished(Loader<List<exercicioRepository>> loader, List<exercicioRepository> data) {

        loadingBar.setVisibility(View.INVISIBLE);

        if (data == null) {
            showErrorMessage();
        } else {
            adapter.clear();
            adapter.addAll(data);
            showJsonDataView();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<exercicioRepository>> loader) {

    }


    public void onLoadReset(@NonNull Loader<List<exercicioRepository>> loader) {

    }

    private void showJsonDataView() {
        errorMessage.setVisibility(View.INVISIBLE);
        dataListView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        dataListView.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
    }

    public void searchExercicio(View view) {
        makeExercicioSearchQuery();
    }

    private void makeExercicioSearchQuery() {
        String exercicioQuery = requestTag.getText().toString();

        Bundle queryBundle = new Bundle();
        queryBundle.putString(exercicio_query_tag, exercicioQuery);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> exercicioSearchLoader = loaderManager.getLoader(exercicio_search_loader);
        if(exercicioSearchLoader == null){
            loaderManager.initLoader(exercicio_search_loader, queryBundle, this);
        }else{
            loaderManager.restartLoader(exercicio_search_loader, queryBundle, this);
        }
    }


}