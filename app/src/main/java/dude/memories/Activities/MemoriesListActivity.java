package dude.memories.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dude.memories.Adapter.MemoriesListAdapter;
import dude.memories.Helper.DB_Helper;
import dude.memories.R;

public class MemoriesListActivity extends AppCompatActivity {
    DB_Helper db_helper;

    @BindView(R.id.list_of_memories)
    RecyclerView list_of_memories;
    MemoriesListAdapter memoriesListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memories_list);
        ButterKnife.bind(this);
        db_helper = new DB_Helper(this);

        LinearLayoutManager llm = new LinearLayoutManager(MemoriesListActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        list_of_memories.setLayoutManager(llm);
        memoriesListAdapter = new MemoriesListAdapter(MemoriesListActivity.this, db_helper.getAllMemories());
        list_of_memories.setAdapter(memoriesListAdapter);
        memoriesListAdapter.notifyDataSetChanged();
    }

}
