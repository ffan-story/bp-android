package com.feifan.bp.home.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xuchunlei on 15/6/19.
 */
public class CenterModel implements Parcelable {

    public int id;
    public String primaryName;
    public String secondaryName;
    public String logoSrc;

    public CenterModel() {

    }

    public CenterModel(Parcel source) {
        id = source.readInt();
        primaryName = source.readString();
        secondaryName = source.readString();
        logoSrc = source.readString();
    }


    @Override
    public String toString() {
        return "id=" + id + ",primaryName=" + primaryName + ",secondaryName=" + secondaryName + ",logoSrc=" + logoSrc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(primaryName);
        dest.writeString(secondaryName);
        dest.writeString(logoSrc);
    }

    public static final Parcelable.Creator<CenterModel> CREATOR = new Creator<CenterModel>() {
        @Override
        public CenterModel createFromParcel(Parcel source) {
            return new CenterModel(source);
        }

        @Override
        public CenterModel[] newArray(int size) {
            return new CenterModel[0];
        }
    };
}
