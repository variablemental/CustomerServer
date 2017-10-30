package com.emotibot.xychatlib.viewholders;

import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;

import com.emotibot.xychatlib.R;
import com.emotibot.xychatlib.XYlibChatActivity;
import com.emotibot.xychatlib.constants.URLConstants;
import com.emotibot.xychatlib.models.XYlibChatMessage;
import com.emotibot.xychatlib.openapiresult.XYlibResult;
import com.emotibot.xychatlib.openapiresult.items.XYlibNewsItem;
import com.emotibot.xychatlib.openapiresult.items.XYlibResultData;
import com.emotibot.xychatlib.tasks.XYlibImageDownloadTask;
import com.emotibot.xychatlib.utils.XYlibChatMessageUtils;
import com.emotibot.xychatlib.utils.XYlibFileUtils;
import com.emotibot.xychatlib.utils.XYlibLogUtils;

/**
 * Created by ldy on 2017/3/6.
 */

public class XYlibNewsVH extends XYlibBaseViewHolder {
    final int newsOnEachPage = 4;
    final int pageNum = 2;
    final String defaultImage = "news_default.png";

    TextView timeView;
    ViewPager vpNewsPags;
    ImageView ivIndicators[];
    View views[];

    private XYlibResult<XYlibResultData<XYlibNewsItem>> resultNews;
    XYlibChatMessage chatMessage;
    XYlibChatActivity mActivity;

    public static ItemResHolder[] itemResHolders = {
            new ItemResHolder(R.id.rl_news1, R.id.news_title1, R.id.news_img1, R.id.rl_news_line1),
            new ItemResHolder(R.id.rl_news2, R.id.news_title2, R.id.news_img2, R.id.rl_news_line2),
            new ItemResHolder(R.id.rl_news3, R.id.news_title3, R.id.news_img3, R.id.rl_news_line3),
            new ItemResHolder(R.id.rl_news4, R.id.news_title4, R.id.news_img4, R.id.rl_news_line4),
    };

    public XYlibNewsVH(View view) {
        super(view);
    }

    protected void initViews(View v) {
        timeView = (TextView) v.findViewById(R.id.timeView);
        vpNewsPags = (ViewPager) v.findViewById(R.id.vp_news_pager);
        ivIndicators = new ImageView[pageNum];
        ivIndicators[0] = (ImageView) v.findViewById(R.id.iv_page1);
        ivIndicators[1] = (ImageView) v.findViewById(R.id.iv_page2);
    }

    public void bindView(XYlibChatMessage chatMessage, XYlibChatActivity activity, int pos) {
        mActivity = activity;
        this.chatMessage = chatMessage;

        resultNews = parseJson(chatMessage.getMsg());
        int totalPageNum;
        if (resultNews.getData().getItems().size() > newsOnEachPage) {
            totalPageNum = pageNum;
        } else {
            totalPageNum = 1;
        }

        views = new View[totalPageNum];
        for (int i = 0; i < totalPageNum; i++) {
            views[i] = View.inflate(mActivity, R.layout.item_news_page, null);
            initNewsPage(i, resultNews, views[i]);
        }
        initViewPager(vpNewsPags, views);
        XYlibChatMessageUtils.shouldShowTimeTag(mActivity.getChatController().getAdapter(),
                chatMessage, timeView, pos);
    }

    private XYlibResult<XYlibResultData<XYlibNewsItem>> parseJson(String json) {
        Type type = new TypeToken<XYlibResult<XYlibResultData<XYlibNewsItem>>>() {}.getType();
        XYlibResult<XYlibResultData<XYlibNewsItem>> fnews =
                                                        mActivity.getGson().fromJson(json, type);

        return fnews;
    }

    private void initNewsPage(int pageNum, XYlibResult<XYlibResultData<XYlibNewsItem>> resultNews,
                              View page) {
        NewsItemHolder[] nihs = new NewsItemHolder[newsOnEachPage];
        for (int i = 0; i < newsOnEachPage; i++) {
            nihs[i] = new NewsItemHolder();
            nihs[i].initHolder(
                    (TextView) page.findViewById(itemResHolders[i].tvTitle),
                    (ImageView) page.findViewById(itemResHolders[i].ivImg),
                    (RelativeLayout) page.findViewById(itemResHolders[i].rlLine),
                    (RelativeLayout) page.findViewById(itemResHolders[i].rlNews),
                    i+pageNum*newsOnEachPage);
        }

        RelativeLayout rlNoMore = (RelativeLayout) page.findViewById(R.id.rl_no_more);

        //hide all
        for (int i = 0; i < nihs.length; i++) {
            nihs[i].hide();
        }
        rlNoMore.setVisibility(View.GONE);

        if (pageNum == 1 && resultNews.getData().getItems().size() <= newsOnEachPage) {
            XYlibLogUtils.d("adapter", "invalid, only has "+resultNews.getData().getItems().size()
                    +" news, should not create the seconde page");
            return;
        }

        int size;
        if (pageNum == 0) {
            size = (resultNews.getData().getItems().size() > newsOnEachPage) ?
                    newsOnEachPage : resultNews.getData().getItems().size();
        } else {
            size = (resultNews.getData().getItems().size() > 2 * newsOnEachPage) ?
                    newsOnEachPage:resultNews.getData().getItems().size() - newsOnEachPage;
        }

        for (int i = 0; i < size; i++) {
            int resultIdx = i + pageNum*newsOnEachPage;
            nihs[i].setValues(resultNews, resultIdx);

            if(i == size - 1) {
                nihs[i].rlLine.setVisibility(View.GONE);
            }
        }

        if (size < newsOnEachPage) {
            rlNoMore.setVisibility(View.VISIBLE);
        }
    }

    private String getDefaultImgUrl() {
        //FILE_URL
        String filename = defaultImage;

        String ret = URLConstants.FILE_URL + filename;

        return ret;
    }

    private String generateNewsImageName(String url) {
        String strs[] = url.split("/");
        String filename;

        if (strs.length <= 0) {
            return "";
        }

        if (strs.length == 1) {
            return "";
        }

        filename = strs[strs.length-2] + "_" +strs[strs.length-1];
        return filename;
    }

    class NewsItemHolder {
        RelativeLayout rlNews;
        TextView tvTitle;
        ImageView ivImg;
        RelativeLayout rlLine;
        int idx;

        public void initHolder(TextView tv, ImageView iv, RelativeLayout rl, RelativeLayout rlnews, int index) {
            tvTitle = tv;
            ivImg = iv;
            rlLine = rl;
            rlNews = rlnews;
            idx = index;
        }

        public void hide() {
            rlNews.setVisibility(View.GONE);
        }

        public void show() {
            rlNews.setVisibility(View.VISIBLE);
        }

        public void setValues(XYlibResult<XYlibResultData<XYlibNewsItem>> resultNews
                , int resultIdx) {
            XYlibNewsItem item = resultNews.getData().getItems().get(resultIdx);
            String url;
            tvTitle.setText(item.getTitle());
            if (item.getImageUrls().size() > 0) {
                url = item.getImageUrls().get(0).getUrl();
                if (TextUtils.isEmpty(url)) {
                    url = getDefaultImgUrl();
                }
            } else {
                url = getDefaultImgUrl();
            }

            String filename = defaultImage;

            if (!url.contains(defaultImage)) {
                filename = generateNewsImageName(url);
            }

            if (TextUtils.isEmpty(filename)) {
                return;
            }

            String path = XYlibFileUtils.buildImageChatPath() + filename;
            File file = new File(path);
            if (file.exists()) {
                ivImg.setImageURI(Uri.parse("file:///" + file));
            } else {
                new XYlibImageDownloadTask(mActivity.getChatController(), path) {
                    @Override
                    protected void onPostExecute(File result) {
                        if (result == null) {
                            ivImg.setImageResource(R.drawable.loading_pic);
                            return;
                        }

                        ivImg.setImageURI(Uri.parse("file:///" + result));
                    }
                }.execute(url);
            }
            int pageIdx = resultIdx > 3 ? 1 : 0;
            rlNews.setOnClickListener(new NewsItemOnClickListener(
                    resultNews.getData().getItems().get(resultIdx).getLink(), pageIdx));
            show();
        }
    }

    static class ItemResHolder {
        int rlNews;
        int tvTitle;
        int ivImg;
        int rlLine;

        ItemResHolder(int a, int b, int c, int d) {
            rlNews = a;
            tvTitle = b;
            ivImg = c;
            rlLine = d;
        }
    }

    class NewsItemOnClickListener implements View.OnClickListener {
        int pageIdx;
        String url;

        NewsItemOnClickListener(String url, int index) {
            this.url = url;
            pageIdx = index;
        }
        @Override
        public void onClick(View v) {
            setClickedPageIndex(pageIdx);
            mActivity.getWebViewDialog().loadWebPage(url);
            mActivity.getWebViewDialog().show();
        }
    }

    private void setClickedPageIndex(int idx) {
        if (resultNews != null) {
            resultNews.setPageIdx(idx);
            chatMessage.setMsg(mActivity.getGson().toJson(resultNews));
        }
    }

    private void initViewPager(final ViewPager vp, final View[] views) {
        ivIndicators[0].setVisibility(View.VISIBLE);
        ivIndicators[1].setVisibility(View.VISIBLE);
        if (views.length == 1) {
            ivIndicators[0].setVisibility(View.GONE);
            ivIndicators[1].setVisibility(View.GONE);
        }

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                int position = vp.getCurrentItem();

                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    setIndicator(position);
                }
            }
        });

        vp.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return views.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(views[position]);

            }

            @Override
            public int getItemPosition(Object object) {

                return super.getItemPosition(object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(views[position]);
                return views[position];
            }
        });


        vp.setCurrentItem(resultNews.getPageIdx());
        setIndicator(resultNews.getPageIdx());
    }

    private void setIndicator(int position){
        for (int i = 0; i < ivIndicators.length; i++) {
            if (i == position) {
                ivIndicators[i].setImageResource(R.drawable.circle_indicator_highlight);
            } else {
                ivIndicators[i].setImageResource(R.drawable.circle_indicator_dark);
            }
        }
    }
}
