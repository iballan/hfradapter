package com.mbh.hfradapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Createdby MBH on 02/03/2017.
 */

public abstract class ParallaxHFRAdapter<T, VH extends RecyclerView.ViewHolder>
        extends HFRAdapter<T, VH> {
    private static final float SCROLL_MULTIPLIER = 0.5f;

    private ClipContainer header;
    private ClipContainer footer;
    private OnParallaxScroll parallaxScroll;
    private boolean isParallaxHeader = true;
    private boolean isParallaxFooter = true;
    private boolean isScaleWithParallax = false;
    private boolean isFadeWithParallax = false;

    public ParallaxHFRAdapter(List<T> items) {
        this.items = items;
    }

    public ParallaxHFRAdapter() {}

    //parallax adapter may have only one header
    @Override
    public void addHeader(View header) {
        if (getHeadersCount() == 0) {
            super.addHeader(header);
        } else {
            removeHeader(getHeader(0));
            super.addHeader(header);
        }
    }

    //parallax adapter may have only one header
    @Override
    public void addFooter(View footer) {
        if (getFootersCount() == 0) {
            super.addFooter(footer);
        } else {
            removeFooter(getFooter(0));
            super.addFooter(footer);
        }
    }

    private void translateView(float of, ClipContainer view, boolean isFooter) {
        float ofCalculated = of * SCROLL_MULTIPLIER;
        ofCalculated = isFooter ? -ofCalculated : ofCalculated;
        // TODO: Fix this line here
        view.setTranslationY(ofCalculated);
//            ViewCompat.setTranslationY(view, ofCalculated);
        view.setOffset(Math.round(ofCalculated));

        if (isScaleWithParallax) {
            float scaleAmount = 1f - ofCalculated * 0.0001f;
            view.setScaleX(scaleAmount);
            view.setScaleY(scaleAmount);
        }

        if (isFadeWithParallax) {
            float fadeAmount = 1f - ofCalculated * 0.001f;
            view.setAlpha(fadeAmount);
        }

        view.invalidate();

        if (parallaxScroll != null && !isFooter) {
            float left = Math.min(1, ((ofCalculated) / (view.getHeight() * SCROLL_MULTIPLIER)));
            parallaxScroll.onParallaxScroll(left, of, view);
        }
    }

    @Override
    public final VH onCreateViewHolder(ViewGroup viewGroup, int type) {
        //if our position is one of our items (this comes from getItemViewType(int position) below)
        if (type != TYPE_HEADER && type != TYPE_FOOTER && type != TYPE_LOADING) {
            return onCreateItemViewHolder(viewGroup, type);
            //else if we have a header
        } else if (type == TYPE_LOADING && getLoadingView() != null) {
            FrameLayout frameLayout = new FrameLayout(viewGroup.getContext());
            setHeaderFooterLayoutParams(frameLayout);
            View lView = getLoadingView();
            if (lView.getParent() != null) {
                Toast.makeText(lView.getContext(), "Has Parent", Toast.LENGTH_SHORT).show();
                ((ViewGroup) lView.getParent()).removeView(getLoadingView());
            }
            frameLayout.addView(getLoadingView());
            return (VH) new LoadingViewHolder(frameLayout);
        } else if (type == TYPE_HEADER) {
            //create a new ParallaxContainer
            header = new ClipContainer(viewGroup.getContext(), isParallaxHeader, false);
            //make sure it fills the space
            setHeaderFooterLayoutParams(header);
            return (VH) new HeaderFooterViewHolder(header);
            //else we have a footer
        } else {
            //create a new ParallaxContainer
            footer = new ClipContainer(viewGroup.getContext(), isParallaxFooter, true);
            //make sure it fills the space
            setHeaderFooterLayoutParams(footer);
            return (VH) new HeaderFooterViewHolder(footer);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (header != null && isParallaxHeader) {
                    translateView(recyclerView.computeVerticalScrollOffset(), header, false);
                }
                if (footer != null && isParallaxFooter) {
                    int range = recyclerView.computeVerticalScrollRange();
                    int extend = recyclerView.computeVerticalScrollExtent();
                    int offset = recyclerView.computeVerticalScrollOffset();
                    translateView(range - (extend + offset), footer, true);
                }
            }
        });
    }

    public void setParallaxHeader(boolean isParallaxHeader) {
        this.isParallaxHeader = isParallaxHeader;
    }

    public void setParallaxFooter(boolean isParallaxFooter) {
        this.isParallaxFooter = isParallaxFooter;
    }

    public void setParallaxHeaderFooter(boolean isParallaxHeader, boolean isParallaxFooter) {
        this.isParallaxHeader = isParallaxHeader;
        this.isParallaxFooter = isParallaxFooter;
    }

    public void setOnParallaxScroll(OnParallaxScroll parallaxScroll) {
        this.parallaxScroll = parallaxScroll;
        this.parallaxScroll.onParallaxScroll(0, 0, header);
    }

    public interface OnParallaxScroll {
        /**
         * Event triggered when the parallax is being scrolled.
         *
         * @param percentage
         * @param offset
         * @param parallax
         */
        void onParallaxScroll(float percentage, float offset, View parallax);
    }
}
