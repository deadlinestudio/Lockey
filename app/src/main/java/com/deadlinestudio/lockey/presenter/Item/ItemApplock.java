package com.deadlinestudio.lockey.presenter.Item;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.deadlinestudio.lockey.R;

public class ItemApplock implements Parcelable {
    private String appName;
    private Drawable appIcon;
    private Boolean lockFlag;
    private String appPackage;

    public ItemApplock(String appName, Drawable appIcon, String appPackage){
        this.appName = appName;
        this.appIcon = appIcon;
        //this.appIcon = getBitmapFromDrawable(appIcon);
        this.lockFlag = false;
        this.appPackage = appPackage;
    }

    protected ItemApplock(Parcel in) {
        appName = in.readString();
        appIcon = in.readParcelable(Bitmap.class.getClassLoader());
        byte tmpLockFlag = in.readByte();
        lockFlag = tmpLockFlag == 0 ? null : tmpLockFlag == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(appName);
        dest.writeParcelable((Parcelable) appIcon, flags);
        dest.writeByte((byte) (lockFlag == null ? 0 : lockFlag ? 1 : 2));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ItemApplock> CREATOR = new Creator<ItemApplock>() {
        @Override
        public ItemApplock createFromParcel(Parcel in) {
            return new ItemApplock(in);
        }

        @Override
        public ItemApplock[] newArray(int size) {
            return new ItemApplock[size];
        }
    };
    @NonNull
    private Bitmap getBitmapFromDrawable(@NonNull Drawable drawable) {
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }
    public String getAppName() {
        return appName;
    }

    public void setAppName(String title) {
        this.appName = title;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public Boolean getLockFlag() {
        return lockFlag;
    }

    public void setLockFlag(Boolean lockFlag) {
        this.lockFlag = lockFlag;
    }

    public String getAppPackage() {return appPackage;}

    public void setAppPackage(String appPackage) {  this.appPackage = appPackage;  }
}
