package com.emotibot.xychatlib.openapiresult.items;

import java.util.List;

/**
 * Created by ldy on 2017/3/6.
 */

public class XYlibNewsItem {
    String channelName;
    String source;
    String title;
    String link;
    List<ImageItem> imageUrls;

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<ImageItem> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<ImageItem> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public class ImageItem {
        private String url;
        private String width;
        private String height;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }
    }
}
