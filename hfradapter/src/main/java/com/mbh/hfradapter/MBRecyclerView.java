package com.mbh.hfradapter;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Createdby MBH on 02/05/2017.
 */

public class MBRecyclerView extends RecyclerView {

    private OnScrollListener wrappedUpDownScrollListener;
    private OnScrollListener wrappedLeftRightScrollListener;

    public MBRecyclerView(Context context) {
        super(context);
        init();
    }

    public MBRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MBRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {}

    // region Scrolling Listener for Up, Down, Left and Right
    public void setOnUpDownScrollListener(final OnUpDownScrollListener onUpDownScrollListener) {
        if (wrappedUpDownScrollListener == null) {
            wrappedUpDownScrollListener = getWrappedUpDownScrollListener(onUpDownScrollListener);
            addOnScrollListener(wrappedUpDownScrollListener);
        }
    }

    public void removeOnUpDownScrollListener() {
        if (wrappedUpDownScrollListener != null) {
            removeOnScrollListener(wrappedUpDownScrollListener);
            wrappedUpDownScrollListener = null;
        }
    }

    public void setLeftOnRightScrollListener(final OnLeftRightScrollListener onLeftRightScrollListener) {
        if (wrappedLeftRightScrollListener == null) {
            wrappedLeftRightScrollListener = getWrappedLeftRightScrollListener(onLeftRightScrollListener);
            addOnScrollListener(wrappedLeftRightScrollListener);
        }
    }

    public void removeOnLeftRightScrollListener() {
        if (wrappedLeftRightScrollListener != null) {
            removeOnScrollListener(wrappedLeftRightScrollListener);
            wrappedLeftRightScrollListener = null;
        }
    }

    private OnScrollListener getWrappedUpDownScrollListener(final OnUpDownScrollListener onUpDownScrollListener) {
        return new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (onUpDownScrollListener != null) {
                    if (dy > 0) {
                        onUpDownScrollListener.onScrollDown(dy);
                    } else if (dy < 0) {
                        onUpDownScrollListener.onScrollUp(dy);
                    }

                    // Negative to check scrolling up, positive to check scrolling down
                    if (!ViewCompat.canScrollVertically(recyclerView, -1)) {
                        onUpDownScrollListener.onScrolledToTop();
                    } else if (!ViewCompat.canScrollVertically(recyclerView, 1)) {
                        onUpDownScrollListener.onScrolledToBottom();
                    } else {
                        onUpDownScrollListener.onScrolledToMiddle();
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (onUpDownScrollListener != null) {
                        onUpDownScrollListener.onScrollStopped();
                    }
                }
            }
        };
    }

    private OnScrollListener getWrappedLeftRightScrollListener(final OnLeftRightScrollListener onLeftRightScrollListener) {
        return new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (onLeftRightScrollListener != null) {
                    if (dy > 0) {
                        onLeftRightScrollListener.onScrollRight(dx);
                    } else if (dy < 0) {
                        onLeftRightScrollListener.onScrollLeft(dx);
                    }

                    // Negative to check scrolling left, positive to check scrolling right
                    if (!ViewCompat.canScrollHorizontally(recyclerView, -1)) {
                        onLeftRightScrollListener.onScrolledToMostLeft();
                    } else if (!ViewCompat.canScrollHorizontally(recyclerView, 1)) {
                        onLeftRightScrollListener.onScrolledToMostRight();
                    }else {
                        onLeftRightScrollListener.onScrolledToMiddle();
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (onLeftRightScrollListener != null) {
                        onLeftRightScrollListener.onScrollStopped();
                    }
                }
            }
        };
    }

    public static abstract class OnUpDownScrollListener {
        public void onScrollUp(int dy) {}

        public void onScrollDown(int dy) {}

        public void onScrolledToTop() {}

        public void onScrolledToBottom() {}

        /**
         * Not Top Not Bottom, Just in the middle :)
         */
        public void onScrolledToMiddle() {}

        public void onScrollStopped() {}
    }

    public static abstract class OnLeftRightScrollListener {
        public void onScrollLeft(int dx) {}

        public void onScrollRight(int dx) {}

        public void onScrolledToMostRight() {}

        public void onScrolledToMostLeft() {}

        /**
         * Not Left Not Right, Just in the middle :)
         */
        public void onScrolledToMiddle() {}

        public void onScrollStopped() {}
    }
    // endregion
}