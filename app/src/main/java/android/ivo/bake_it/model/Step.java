package android.ivo.bake_it.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Step implements Parcelable {

    private final int id;

    private final String shortDescription;

    private final String description;

    private final String videoURL;

    private final String thumbnailURL;

    public Step(Builder builder)
    {
        this.id = builder.id;
        this.shortDescription = builder.shortDescription;
        this.description = builder.description;
        this.videoURL = builder.videoURL;
        this.thumbnailURL = builder.thumbnailURL;
    }

    protected Step(Parcel in) {
        id = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    public int getId() {
        return this.id;
    }

    public String getShortDescription() {
        return this.shortDescription;
    }

    public String getDescription() {
        return this.description;
    }

    public String getVideoURL() {
        return this.videoURL;
    }

    public String getThumbnailURL() {
        return this.thumbnailURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static class Builder {

        private int id;

        private String shortDescription;

        private String description;

        private String videoURL;

        private String thumbnailURL;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder shortDescription(String shortDescription)
        {
            this.shortDescription = shortDescription;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder videoURL(String videoURL) {
            this.videoURL = videoURL;
            return this;
        }

        public Builder thumbnailURL(String thumbnailURL) {
            this.thumbnailURL = thumbnailURL;
            return this;
        }
    }

    @Override
    public String toString() {
        return "Step{" +
                "id=" + id +
                ", shortDescription='" + shortDescription + '\'' +
                ", description='" + description + '\'' +
                ", videoURL='" + videoURL + '\'' +
                ", thumbnailURL='" + thumbnailURL + '\'' +
                '}';
    }
}
