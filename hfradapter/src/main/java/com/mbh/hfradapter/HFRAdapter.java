package com.mbh.hfradapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Createdby MBH on 02/03/2017.
 */

public abstract class HFRAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    static final int TYPE_HEADER = 9876;
    static final int TYPE_FOOTER = 9875;
    static final int TYPE_LOADING = 9874;
    private static final String P_ITEMS = "HFR.items";
    private static final String P_HEADERS = "HFRh";
    private static final String P_FOOTERS = "HFRf";
    private static final String P_LOADING = "HFRl";

    private OnItemClickedListener mOnItemClickedListener;
    private OnItemLongClickedListener mOnItemLongClickedListener;
    private OnLoadMoreListener mOnLoadMoreListener;

    private ArrayList<View> headers = new ArrayList<>();
    private ArrayList<View> footers = new ArrayList<>();
    private View loadingView;
    List<T> items = new ArrayList<>();

    private boolean isLoading = false;
    private boolean isAutoLoading = false;
    private boolean alwaysScrollToLoading = false;

    private WeakReference<RecyclerView> wr_recyclerView;

    public HFRAdapter(List<T> items) {
        this.items = items;
    }

    public HFRAdapter() {
    }

    private RecyclerView.LayoutManager manager;
    private LayoutInflater inflater;
    private GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
        @Override
        public int getSpanSize(int position) {
            return getGridSpan(position);
        }
    };

    /**
     * Called on Item bind to bind views
     *
     * @param viewHolder: View holder to bind its view
     * @param position:   position of the item
     * @param type:       type of the item in the position
     */
    abstract protected void onBindItemViewHolder(VH viewHolder, int position, int type);

    /**
     * @param view: Inflated view from the layout of layoutId
     * @param type: type of the view in its position
     * @return ViewHolder initialized
     */
    protected abstract VH viewHolder(View view, int type);

    protected abstract
    @LayoutRes
    int layoutId(int type);

    /**
     * To get the item list count
     *
     * @return return the count of items list
     */
    public int getRealItemCount() {
        return items.size();
    }

    /**
     * To get the item at certain position
     *
     * @param position: position of item needed to be retained
     * @return the item
     */
    public T getItem(int position) {
        return items.get(position);
    }

    public void add(final int position, T item) {
        items.add(position, item);
        tryNotifying(new Runnable() {
            @Override
            public void run() {
                notifyItemInserted(position);
            }
        });
        final int positionStart = position + getHeadersCount();
        final int itemCount = items.size() - position;
        tryNotifying(new Runnable() {
            @Override
            public void run() {
                notifyItemRangeChanged(positionStart, itemCount);
            }
        });
    }

    public void add(T item) {
        items.add(item);
        tryNotifying(new Runnable() {
            @Override
            public void run() {
                notifyItemInserted(items.size() - 1 + getHeadersCount());
            }
        });
    }

    public void setAll(List<? extends T> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void addAll(final List<? extends T> items) {
        final int size = this.items.size();
        this.items.addAll(items);
        tryNotifying(new Runnable() {
            @Override
            public void run() {
                notifyItemRangeInserted(size + getHeadersCount(), items.size());
            }
        });
    }

    public void addAll(final int position, final List<? extends T> items) {
        this.items.addAll(position, items);
        tryNotifying(new Runnable() {
            @Override
            public void run() {
                notifyItemRangeInserted(position + getHeadersCount(), items.size());
            }
        });
    }

    public void set(final int position, T item) {
        items.set(position, item);
        tryNotifying(new Runnable() {
            @Override
            public void run() {
                notifyItemChanged(position + getHeadersCount());
            }
        });
    }

    public void removeChild(final int position) {
        items.remove(position);
        tryNotifying(new Runnable() {
            @Override
            public void run() {
                notifyItemRemoved(position + getHeadersCount());
            }
        });
        final int positionStart = position + getHeadersCount();
        final int itemCount = items.size() - position;
        tryNotifying(new Runnable() {
            @Override
            public void run() {
                notifyItemRangeChanged(positionStart, itemCount);
            }
        });
    }

    public void clear() {
        final int size = items.size();
        items.clear();
        tryNotifying(new Runnable() {
            @Override
            public void run() {
                notifyItemRangeRemoved(getHeadersCount(), size);
            }
        });
    }

    public void notifyChildChanged(final int position) {
        tryNotifying(new Runnable() {
            @Override
            public void run() {
                notifyItemChanged(position + getHeadersCount());
            }
        });
    }

    public void notifyChildRangeChanged(final int position, final int itemCount) {
        tryNotifying(new Runnable() {
            @Override
            public void run() {
                notifyItemRangeChanged(position + getHeadersCount(), itemCount);
            }
        });
    }

    public void notifyChildRemoved(final int position) {
        tryNotifying(new Runnable() {
            @Override
            public void run() {
                notifyItemRemoved(position + getHeadersCount());
            }
        });
    }

    public void notifyChildDataSetChanged() {
        tryNotifying(new Runnable() {
            @Override
            public void run() {
                notifyItemRangeChanged(getHeadersCount(), items.size());
            }
        });
    }

    public void moveChildTo(final int fromPosition, final int toPosition) {
        if (toPosition != -1 && toPosition < items.size()) {
            final T item = items.remove(fromPosition);
            items.add(toPosition, item);
            tryNotifying(new Runnable() {
                @Override
                public void run() {
                    notifyItemMoved(getHeadersCount() + fromPosition, getHeadersCount() + toPosition);
                }
            });
            final int positionStart = fromPosition < toPosition ? fromPosition : toPosition;
            final int itemCount = Math.abs(fromPosition - toPosition) + 1;
            tryNotifying(new Runnable() {
                @Override
                public void run() {
                    notifyItemRangeChanged(positionStart + getHeadersCount(), itemCount);
                }
            });
        }
    }

    private void tryNotifying(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception ex) {
            ex.printStackTrace();
            notifyDataSetChanged();
        }
    }

    /**
     * Get view holder for the certain position
     *
     * @param rv:       Recycler view which the adapter attached to
     * @param position: position
     * @return the view holder at the certain position
     */
    public VH getViewHolderForItemPosition(RecyclerView rv, int position) {
        // Position here is item position
        return (VH) rv.findViewHolderForLayoutPosition(position + getHeadersCount());
    }


    public
    @Nullable
    VH getViewHolderForItemPosition(int position) {
        // Position here is item position
        if (wr_recyclerView.get() != null) {
            return (VH) wr_recyclerView.get().findViewHolderForAdapterPosition(position + getHeadersCount());
        }
        return null;
    }

    public int indexOf(T object) {
        return items.indexOf(object);
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<? extends T> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        mOnItemClickedListener = onItemClickedListener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.mOnLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup viewGroup, int type) {
        //if our position is one of our items (this comes from getItemViewType(int position) below)
        if (type != TYPE_HEADER && type != TYPE_FOOTER && type != TYPE_LOADING) {
            return onCreateItemViewHolder(viewGroup, type);
            //else we have a header/footer/loading
        } else if (type == TYPE_LOADING && loadingView != null) {
            FrameLayout frameLayout = new FrameLayout(viewGroup.getContext());
            setHeaderFooterLayoutParams(frameLayout);
            return (VH) new LoadingViewHolder(frameLayout);
        } else {
            //create a new framelayout, or inflate from a resource
            FrameLayout frameLayout = new FrameLayout(viewGroup.getContext());
            //make sure it fills the space
            setHeaderFooterLayoutParams(frameLayout);
            return (VH) new HeaderFooterViewHolder(frameLayout);
        }
    }

    protected void setHeaderFooterLayoutParams(ViewGroup viewGroup) {
        ViewGroup.LayoutParams layoutParams;
        if (manager instanceof LinearLayoutManager) {
            int orientation = ((LinearLayoutManager) manager).getOrientation();
            if (orientation == LinearLayoutManager.VERTICAL) {
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            } else {
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
            }
        } else {
            layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        viewGroup.setLayoutParams(layoutParams);
    }

    @Override
    public final void onBindViewHolder(final RecyclerView.ViewHolder vh, final int position) {
        //check what type of view our position is
        if (isHeader(position)) {
            View v = headers.get(position);
            //add our view to a header view and display it
            prepareHeaderFooter((HeaderFooterViewHolder) vh, v);
        } else if (isFooter(position)) {
            View v = footers.get(position - getRealItemCount() - getHeadersCount() - getLoadingViewCount());
            //add our view to a footer view and display it
            prepareHeaderFooter((HeaderFooterViewHolder) vh, v);
        } else if (isLoadingTypeView(position)) {
            prepareLoadingViewHolder((LoadingViewHolder) vh, loadingView);
        } else {
            //it's one of our items, display as required
            final int positionFinal = position - headers.size();
            onBindItemViewHolder((VH) vh, positionFinal, getItemType(position));
            if (mOnItemClickedListener != null) {
                vh.itemView.setClickable(true);
                vh.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickedListener.onItemClicked(v, positionFinal);
                    }
                });
            }
            if (mOnItemLongClickedListener != null) {
                vh.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        mOnItemLongClickedListener.onItemLongClicked(view, positionFinal);
                        return true; // True prevent passing the event to children when long clicked
                    }
                });
            }
        }
    }

    private void prepareHeaderFooter(HeaderFooterViewHolder vh, View view) {
        //if it's a staggered grid, span the whole layout
        if (manager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setFullSpan(true);
            vh.itemView.setLayoutParams(layoutParams);
        }
        //if the view already belongs to another layout, remove it
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        //empty out our FrameLayout and replace with our header/footer
        ((ViewGroup) vh.itemView).removeAllViews();
        ((ViewGroup) vh.itemView).addView(view);
    }

    private void prepareLoadingViewHolder(LoadingViewHolder vh, View view) {
        //if it's a staggered grid, span the whole layout
        if (manager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setFullSpan(true);
            vh.itemView.setLayoutParams(layoutParams);
        }
        //if the view already belongs to another layout, remove it
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        //empty out our FrameLayout and replace with our header/footer
        ((ViewGroup) vh.itemView).removeAllViews();
        ((ViewGroup) vh.itemView).addView(view);
    }

    private boolean isHeader(int position) {
        return (position < headers.size());
    }

    private boolean isLoadingTypeView(int position) {
        return isLoading && (position == (headers.size() + items.size()));
    }

    private boolean isFooter(int position) {
        return footers.size() > 0 && (position >= getHeadersCount() + getRealItemCount() + getLoadingViewCount());
    }

    protected VH onCreateItemViewHolder(ViewGroup parent, int type) {
        View rootView = inflater.inflate(layoutId(type),
                onCreateItemAttachToParent() ? parent : null,
                onCreateItemAttachToParent());
        if (onCreateItemFillWidth()) {
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rootView.setLayoutParams(lp);
        }
        return viewHolder(rootView, type);
    }

    protected boolean onCreateItemAttachToParent() {
        return false;
    }

    protected boolean onCreateItemFillWidth() {
        return true;
    }

    @Override
    public int getItemCount() {
        return headers.size() + getRealItemCount() + footers.size() + getLoadingViewCount();
    }

    public View getLoadingView() {
        return loadingView;
    }

    // add loading view to adapter
    public void setLoadingView(View view) {
        loadingView = view;
    }

    public boolean isAlwaysScrollToLoading() {
        return alwaysScrollToLoading;
    }

    public void setAlwaysScrollToLoading(boolean alwaysScrollToLoading) {
        this.alwaysScrollToLoading = alwaysScrollToLoading;
    }

    public boolean isLoading() {
        return isLoading;
    }


    public void setLoading(boolean status) {
        if (loadingView != null) {
            if (status != this.isLoading) {
                isLoading = status;
                tryNotifying(new Runnable() {
                    @Override
                    public void run() {
                        notifyItemChanged(headers.size() + items.size());
                        if (isAlwaysScrollToLoading() && isLoading()) {
                            if (wr_recyclerView != null && wr_recyclerView.get() != null) {
                                wr_recyclerView.get().smoothScrollToPosition(headers.size() + items.size());
                            }
                        }
                    }
                });
            }
        } else {
            isLoading = false;
        }
    }

    public void setAutoLoading(boolean status) {
        isAutoLoading = status;
        setLoading(isLoading);
    }

    @Override
    final public int getItemViewType(int position) {
        //check what type our position is, based on the assumption that the order is headers > items > footers
        if (isHeader(position)) {
            return TYPE_HEADER;
        } else if (isFooter(position)) {
            return TYPE_FOOTER;
        } else if (isLoadingTypeView(position)) {
            return TYPE_LOADING;
        }
        int type = getItemType(position);
        if (type == TYPE_HEADER || type == TYPE_FOOTER || type == TYPE_LOADING) {
            throw new IllegalArgumentException("Item type cannot equal " + TYPE_HEADER + " or " + TYPE_FOOTER + " or " + TYPE_LOADING);
        }
        return type;
    }

    private void setManager(RecyclerView.LayoutManager manager) {
        this.manager = manager;
        if (this.manager instanceof GridLayoutManager) {
            ((GridLayoutManager) this.manager).setSpanSizeLookup(spanSizeLookup);
        } else if (this.manager instanceof StaggeredGridLayoutManager) {
            ((StaggeredGridLayoutManager) this.manager).setGapStrategy(
                    StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        }
    }

    protected int getGridSpan(int position) {
        if (isHeader(position) || isFooter(position) || isLoadingTypeView(position)) {
            return getMaxGridSpan();
        }
        position -= headers.size();
//        position -= headers.size();
        if (position < getRealItemCount() && getItem(position) instanceof SpanItemInterface) {
            return ((SpanItemInterface) getItem(position)).getGridSpan();
        }
        return 1;
    }

    protected int getMaxGridSpan() {
        if (manager instanceof GridLayoutManager) {
            return ((GridLayoutManager) manager).getSpanCount();
        } else if (manager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) manager).getSpanCount();
        }
        return 1;
    }

    //add a header to the adapter
    public void addHeader(View header) {
        if (!headers.contains(header)) {
            headers.add(header);
            notifyItemInserted(headers.size() - 1);
        }
    }

    public void addHeader(Context context, @LayoutRes int layoutId) {
        addHeader(LayoutInflater.from(context).inflate(layoutId, null));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        wr_recyclerView = new WeakReference<>(recyclerView);
        if (manager == null) {
            setManager(recyclerView.getLayoutManager());
        }
        if (inflater == null) {
            this.inflater = LayoutInflater.from(recyclerView.getContext());
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (loadingView != null) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                    int visibleItemCount = recyclerView.getChildCount();
                    int totalItemCount = linearLayoutManager.getItemCount();
                    boolean loading = isLoading();

                    if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + 5)) {
                        showAutoLoading();
                    }
                }
            }
        });
    }

    private void showAutoLoading() {
        if (isAutoLoading) {
            setLoading(true);
        }
        if (mOnLoadMoreListener != null) {
            mOnLoadMoreListener.onNeedsToLoadMore();
        }
    }

    // add loading view to adapter
    public void setLoadingView(Context context, @LayoutRes int layoutRes) {
        setLoadingView(LayoutInflater.from(context).inflate(layoutRes, null));
    }

    //remove header from adapter
    public void removeHeader(View header) {
        if (headers.contains(header)) {
            //animate
            notifyItemRemoved(headers.indexOf(header));
            headers.remove(header);
        }
    }

    //add a footer to the adapter
    public void addFooter(View footer) {
        if (!footers.contains(footer)) {
            footers.add(footer);
            //animate
            notifyItemInserted(headers.size() + getItemCount() + footers.size() - 1);
        }
    }

    public void addFooter(Context context, @LayoutRes int layoutRes) {
        View footer = LayoutInflater.from(context).inflate(layoutRes, null, false);
        addFooter(footer);
    }

    //remove footer from adapter
    public void removeFooter(View footer) {
        if (footers.contains(footer)) {
            //animate
            notifyItemRemoved(headers.size() + getItemCount() + footers.indexOf(footer));
            footers.remove(footer);
        }
    }

    public int getHeadersCount() {
        return headers.size();
    }

    private int getLoadingViewCount() {
        return isLoading ? 1 : 0;
    }

    public View getHeader(int location) {
        return headers.get(location);
    }

    public int getFootersCount() {
        return footers.size();
    }

    public View getFooter(int location) {
        return footers.get(location);
    }

    protected int getItemType(int position) {
        return 0;
    }

    public void setOnItemLongClickedListener(OnItemLongClickedListener onItemLongClickedListener) {
        mOnItemLongClickedListener = onItemLongClickedListener;
    }

    public interface SpanItemInterface {
        int getGridSpan();
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {
        LoadingViewHolder(View view) {
            super(view);
        }
    }

    //our header/footer RecyclerView.ViewHolder is just a FrameLayout
    static class HeaderFooterViewHolder extends RecyclerView.ViewHolder {
        HeaderFooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    class EmptyStateViewHolder extends RecyclerView.ViewHolder {
        EmptyStateViewHolder(View view) {
            super(view);
        }
    }

    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        if (items.size() > 0 && (items.get(0) instanceof Parcelable
                || items.get(0) instanceof Serializable)) {
            ArrayList<T> arrItems = new ArrayList<>(items);
            bundle.putSerializable(P_ITEMS, arrItems);
        }
        return bundle;
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (state != null && state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            if (bundle.containsKey(P_ITEMS)) {
                items = (ArrayList<T>) bundle.getSerializable(P_ITEMS);
            }
        }
    }

    public interface OnItemClickedListener {
        void onItemClicked(View view, int position);
    }

    public interface OnItemLongClickedListener {
        void onItemLongClicked(View view, int position);
    }

    public interface OnLoadMoreListener {
        void onNeedsToLoadMore();
    }
}
