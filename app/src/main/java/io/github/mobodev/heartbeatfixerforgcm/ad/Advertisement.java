package io.github.mobodev.heartbeatfixerforgcm.ad;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bshao on 5/13/18.
 */

public class Advertisement implements Parcelable {
    public String title;
    public String message;
    public String largeIconUrl;
    public String bigPictureUrl;
    public String landingPageUrl;
    public int version;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.message);
        dest.writeString(this.largeIconUrl);
        dest.writeString(this.bigPictureUrl);
        dest.writeString(this.landingPageUrl);
        dest.writeInt(this.version);
    }

    public Advertisement() {
    }

    protected Advertisement(Parcel in) {
        this.title = in.readString();
        this.message = in.readString();
        this.largeIconUrl = in.readString();
        this.bigPictureUrl = in.readString();
        this.landingPageUrl = in.readString();
        this.version = in.readInt();
    }

    public static final Parcelable.Creator<Advertisement> CREATOR = new Parcelable.Creator<Advertisement>() {
        @Override
        public Advertisement createFromParcel(Parcel source) {
            return new Advertisement(source);
        }

        @Override
        public Advertisement[] newArray(int size) {
            return new Advertisement[size];
        }
    };
}
