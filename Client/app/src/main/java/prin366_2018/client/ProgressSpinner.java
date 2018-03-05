package prin366_2018.client;

import android.os.Build;
import android.view.View;
import android.widget.ProgressBar;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dryush on 05.03.2018.
 */

public class ProgressSpinner {
    private List<View> toHide = new LinkedList<>();
    private List<ProgressBar> toShow = new LinkedList<>();

    protected void addToShow(ProgressBar pb){
        //TODO: В LoginActivity там ещё какой-то бред, который не понимаю, кажется, он для постепенного исчезновения
    }

    public ProgressSpinner(View toHide, ProgressBar spinner){
        this.toHide.add(toHide);
        this.toShow.add(spinner);
    }


    public ProgressSpinner(List<View> toHide, List<ProgressBar> spinners){
        this.toHide.addAll(toHide);
        this.toShow.addAll(spinners);
    }

    protected void showAll(List<? extends View> views){
        for (View v: views) {
            v.setVisibility(View.VISIBLE);
        }
    }

    protected void hideAll(List<? extends View> views){
        for (View v: views){
            v.setVisibility(View.GONE);
        }
    }

    public void show(){
        hideAll(toHide);
        showAll(toShow);
    }

    public void disable(){
        hideAll(toShow);
        showAll(toHide);
    }

}
