package com.emotibot.xychatlib.viewholders;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emotibot.xychatlib.R;
import com.emotibot.xychatlib.XYlibChatActivity;
import com.emotibot.xychatlib.models.XYlibChatMessage;
import com.emotibot.xychatlib.openapiresult.XYlibResponse;

import org.w3c.dom.Text;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Response;

/**
 * Created by Administrator on 2017\11\27 0027.
 */

public class XYlibResponseVH extends XYlibBaseViewHolder {

    private XYlibChatActivity mActivity;
    private final int capacityEachPage=4;

    private ViewPager vp;
    private View v;

    private LinkedList<String> response_msg;
    public ItemResHolder[] itemResHolders={
            new ItemResHolder(R.id.rl_response1,R.id.rl_title1,R.id.rl_line1),
            new ItemResHolder(R.id.rl_response2,R.id.rl_title2,R.id.rl_line2),
            new ItemResHolder(R.id.rl_response3,R.id.rl_title3,R.id.rl_line3),
            new ItemResHolder(R.id.rl_response4,R.id.rl_title4,R.id.rl_line4),
    };
    public XYlibResponseVH(View view) {
        super(view);

    }

    @Override
    protected void initViews(View view) {
        vp=(ViewPager)view.findViewById(R.id.vp_response_pager);
    }

    @Override
    public void bindView(XYlibChatMessage chatMessage, XYlibChatActivity activity, int pos) {
        mActivity=activity;
        //JSON 解析
        XYlibResponse response=mActivity.getGson().fromJson(chatMessage.getMsg(),XYlibResponse.class);
        //答案解析
        parse_msg(response.getValue());
        //全视图获取
        View view=View.inflate(mActivity,R.layout.item_response_page,null);
        v=view;
        ResponseItemHolder items[]=new ResponseItemHolder[capacityEachPage];
        //控件绑定
        for(int i=0;i<capacityEachPage;i++){
            items[i]=new ResponseItemHolder();
            items[i].initHolder((RelativeLayout) view.findViewById(itemResHolders[i].response),
                    (TextView) view.findViewById(itemResHolders[i].response_title),
                    (RelativeLayout) view.findViewById(itemResHolders[i].response_line),i,response_msg.get(i));
        }
        initViewpager();
    }

    private void initViewpager(){
        vp.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(v);

            }

            @Override
            public int getItemPosition(Object object) {

                return super.getItemPosition(object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(v);
                return v;
            }
        });
        vp.setCurrentItem(0);

    }

    //组装答案
    private void parse_msg(String msg){
        String pattern = "\\[[1-9]*\\]";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(msg);
        String[] splits=msg.split(pattern);
        LinkedList<String> seq=new LinkedList<String>();
        for(int i=1;i<splits.length;i++){
            seq.add(splits[i]);
        }
        if(seq.size()>0) {
            response_msg=seq;
        }
    }

    //组件资源类
    static class ItemResHolder{
        int response;
        int response_title;
        int response_line;

        ItemResHolder(int a,int b,int c){
            response=a;
            response_title=b;
            response_line=c;
        }
    }

    //一条选项
    class ResponseItemHolder{
        RelativeLayout rl_response;
        TextView rl_title;
        RelativeLayout rl_line;
        int response_index;

/*        ResponseItemHolder(RelativeLayout response, TextView txt,RelativeLayout line,int index){
            rl_response=response;
            rl_title=txt;
            rl_line=line;
            response_index=index;
        }*/

        void hide(){
            if(rl_response!=null){
                rl_response.setVisibility(View.GONE);
            }
        }

        void show(){
            if(rl_response!=null){
                rl_response.setVisibility(View.VISIBLE);
            }
        }

        void initHolder(RelativeLayout response,TextView txt,RelativeLayout line,int index,String title){
            rl_response=response;
            rl_title=txt;
            rl_line=line;
            response_index=index;
            rl_title.setText(title);
            rl_response.setOnClickListener(new ItemClickListener(Integer.toString(response_index+1)));
            show();
        }


    }

    //点击监听器，并绑定回复内容
    class ItemClickListener implements View.OnClickListener{
        private String utterance;

        ItemClickListener(String utterance){
            this.utterance=utterance;
        }

        @Override
        public void onClick(View v) {
            mActivity.utter(utterance);
        }
    }

}
