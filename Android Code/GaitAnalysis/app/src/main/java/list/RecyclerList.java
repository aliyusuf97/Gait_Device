package list;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerList {
    private ArrayList<RecyclerViewItem> cardList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecycleViewAdapter recycleViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ListOnClickCallback onClickCallback;

    public RecyclerList(RecyclerView recyclerViewID, Context context, ListOnClickCallback callback) {
        this.recyclerView = recyclerViewID;
        layoutManager = new LinearLayoutManager(context);
        recycleViewAdapter = new RecycleViewAdapter(cardList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recycleViewAdapter);
        onClickCallback = callback;
        recycleViewAdapter.setOnItemClickListener(new RecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemCLick(int position) {
                onClickCallback.onListClick(position);
            }
        });

    }

    public RecyclerViewItem getCard(int index){
        recycleViewAdapter.notifyDataSetChanged();
        return cardList.get(index);
    }

    public void addCard(RecyclerViewItem item){
        cardList.add(item);
        recycleViewAdapter.notifyDataSetChanged();
    }

    public void removeCard(int index){
        cardList.remove(index);
        recycleViewAdapter.notifyDataSetChanged();
    }

    public void removeAllCards(){
        cardList.clear();
        recycleViewAdapter.notifyDataSetChanged();
    }
}
