package com.emotibot.xychatlib.openapiresult.items;

import java.util.List;

/**
 * Created by ldy on 2017/3/6.
 */

public class XYlibResultData<T> {
    List<String> button;
    List<T> items;

    public List<String> getButton() {
        return button;
    }

    public void setButton(List<String> button) {
        this.button = button;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
