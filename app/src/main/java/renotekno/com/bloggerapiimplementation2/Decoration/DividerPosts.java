package renotekno.com.bloggerapiimplementation2.Decoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import renotekno.com.bloggerapiimplementation2.R;

/**
 * Created by zcabez on 27/08/2017.
 */

public class DividerPosts extends RecyclerView.ItemDecoration {

    private Drawable divider;

    public DividerPosts(Drawable divider) {
        this.divider = divider;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        // ensure that the first view didn't get space for the divider
        if (parent.getChildAdapterPosition(view) == 0) {
            return;
        }

        /* add the outer rectangle top some space to draw the divider
         * by increasing the coordinate of the top
         * so that the divider doesn't lap other view below it
         * the divider has its own space to draw
         */
        outRect.top = divider.getIntrinsicHeight();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        // Total recycleView item view on the screen
        int childCount = parent.getChildCount();

        int dividerLeft = 0;
        int dividerRight = 0;

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            if (dividerLeft == 0 && dividerRight == 0) {
                ConstraintLayout constraintItemViewGroup = (ConstraintLayout) child.findViewById(R.id.constraintItemViewGroup);
                dividerLeft = constraintItemViewGroup.getPaddingLeft();
                dividerRight = constraintItemViewGroup.getWidth() - constraintItemViewGroup.getPaddingRight();
            }

            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();

            int dividerTop = child.getBottom() + layoutParams.bottomMargin;
            int dividerBottom = dividerTop + divider.getIntrinsicHeight();

            divider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
            divider.draw(c);
        }

    }
}
