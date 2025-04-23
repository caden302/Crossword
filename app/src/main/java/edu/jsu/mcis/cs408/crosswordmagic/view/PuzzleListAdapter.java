package edu.jsu.mcis.cs408.crosswordmagic.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import edu.jsu.mcis.cs408.crosswordmagic.R;
import edu.jsu.mcis.cs408.crosswordmagic.model.PuzzleListItem;

public class PuzzleListAdapter extends RecyclerView.Adapter<PuzzleListAdapter.ViewHolder> {

    private final List<PuzzleListItem> puzzles;
    private final Context context;

    public PuzzleListAdapter(Context context, List<PuzzleListItem> puzzles) {
        this.context = context;
        this.puzzles = puzzles;
    }

    @Override
    public PuzzleListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.puzzle_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PuzzleListAdapter.ViewHolder holder, int position) {
        PuzzleListItem item = puzzles.get(position);
        holder.textView.setText(item.toString());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("puzzleid", item.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return puzzles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.textViewPuzzleName);
        }
    }
}
