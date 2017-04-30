package com.mbh.hfradapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.widget.FrameLayout;


/**
 * Createdby MBH on 02/03/2017.
 */
public class ClipContainer extends FrameLayout {

    private boolean isParallax;
    private boolean isFooter;
    private int offset;

    public ClipContainer(@NonNull Context context) {
        super(context);
        this.isParallax = true;
        this.isFooter = false;
        this.offset = 0;
    }

    public ClipContainer(Context context, boolean shouldClick, boolean isFooter) {
        super(context);
        isParallax = shouldClick;
        this.isFooter = isFooter;
    }

    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        if (isParallax) {
            int top = isFooter ? -offset : 0;
            int bottom = isFooter ? getBottom() : getBottom() + offset;
            Rect rect = new Rect(getLeft(), top, getRight(), bottom);
            canvas.clipRect(rect);
        }
        super.dispatchDraw(canvas);
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        offset = ss.offset;
        isParallax = ss.isParallax;
        isFooter = ss.isFooter;
        invalidate();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.offset = offset;
        savedState.isParallax = isParallax;
        savedState.isFooter = isFooter;
        return savedState;
    }

    private static class SavedState extends BaseSavedState {
        int offset;
        boolean isParallax;
        boolean isFooter;

        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public String toString() {
            return "ClipContainer.SavedState{"
                    + "offset=" + offset
                    + "isParallax=" + isParallax
                    + "isFooter=" + isFooter
                    + '}';
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.offset);
            dest.writeByte(this.isParallax ? (byte) 1 : (byte) 0);
            dest.writeByte(this.isFooter ? (byte) 1 : (byte) 0);
        }

        SavedState(Parcel in) {
            super(in);
            this.offset = in.readInt();
            this.isParallax = in.readByte() != 0;
            this.isFooter = in.readByte() != 0;
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
