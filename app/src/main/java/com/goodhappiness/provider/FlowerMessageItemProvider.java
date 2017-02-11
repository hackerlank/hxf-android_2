package com.goodhappiness.provider;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.bean.database.PUserInfo;
import com.goodhappiness.bean.im.FlowerMessage;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.utils.DBUtils;
import com.goodhappiness.utils.IntentUtils;
import com.goodhappiness.utils.PreferencesUtil;
import com.goodhappiness.widget.emoji.EmojiTextView;

import io.rong.imkit.RongIM;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;

/**
 * Created by 电脑 on 2016/7/4.
 */
@ProviderTag(messageContent = FlowerMessage.class)
public class FlowerMessageItemProvider extends IContainerItemProvider.MessageProvider<FlowerMessage> {
    private Context context;

    class ViewHolder {
        TextView send;
        EmojiTextView sender;
        TextView flowerCount;
        RelativeLayout rl;
    }

    @Override
    public void bindView(View v, int i, FlowerMessage content, UIMessage message) {
        ViewHolder holder = (ViewHolder) v.getTag();
        if (message.getMessageDirection() != Message.MessageDirection.SEND) {//消息方向，不是自己发送的
            holder.send.setText("送给你");
            holder.sender.setVisibility(View.GONE);
            holder.rl.setBackgroundResource(R.mipmap.flower_bg_left);
        } else {
            holder.send.setText("送给");
            holder.sender.setVisibility(View.VISIBLE);
            if (DBUtils.getUserInDataBase(content.getReceiverId()) != null) {
                holder.sender.setText(DBUtils.getUserInDataBase(content.getReceiverId()).getName());
            }
            holder.rl.setBackgroundResource(R.mipmap.flower_bg_right);
        }
        holder.flowerCount.setText(content.getFlowerCount() + "");
    }

    @Override
    public Spannable getContentSummary(FlowerMessage flowerMessage) {
        DBUtils.setUserInfoToDataBase(flowerMessage.getUserInfo());
        PUserInfo pUserInfo = DBUtils.getUserInDataBase(flowerMessage.getReceiverId());
        if(pUserInfo!=null){
            long uid = PreferencesUtil.getLongPreferences(GoodHappinessApplication.getContext(),FieldFinals.UID,0);
            SpannableString ss;
            if(pUserInfo.getTargetId().equals((uid+""))){
                ss = new SpannableString(flowerMessage.getUserInfo().getName()+"送给你"+flowerMessage.getFlowerCount()+"朵花");
                if(GoodHappinessApplication.getContext()!=null)
                ss.setSpan(new ForegroundColorSpan(GoodHappinessApplication.getContext().getResources().getColor(R.color.advert_blue_text)), 0, flowerMessage.getUserInfo().getName().length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return ss;
            }else{
                ss = new SpannableString("你送给了"+pUserInfo.getName()+flowerMessage.getFlowerCount()+"朵花");
                if(context!=null)
                    ss.setSpan(new ForegroundColorSpan(GoodHappinessApplication.getContext().getResources().getColor(R.color.advert_blue_text)), 4, pUserInfo.getName().length()+4,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return ss;
            }
        }
        return new SpannableString("送花给你了");
    }

    @Override
    public void onItemClick(View view, int i, FlowerMessage flowerMessage, UIMessage uiMessage) {
        if (context != null) {
            if (uiMessage.getUserInfo().getUserId().equals(String.valueOf(PreferencesUtil.getLongPreferences(context, FieldFinals.UID, 0)))) {
                IntentUtils.startToFlower(context, 1);
            } else {
                IntentUtils.startToFlower(context, 0);
            }
        }
    }

    @Override
    public void onItemLongClick(View view, int i, FlowerMessage flowerMessage, final UIMessage uiMessage) {
        if (context != null) {
            String[] names = {"删除消息", "取消"};
            new AlertDialog.Builder(context).setTitle(uiMessage.getUserInfo().getName())
                    .setItems(names, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            switch (which) {
                                case 0:

                                    RongIM.getInstance().deleteMessages(new int[]{uiMessage.getMessage().getMessageId()}, new RongIMClient.ResultCallback<Boolean>() {
                                        @Override
                                        public void onSuccess(Boolean aBoolean) {
                                            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onError(RongIMClient.ErrorCode errorCode) {

                                        }
                                    });
                                    break;
                                case 1:
                                    dialog.dismiss();
                                    break;
                            }

                        }
                    }).show();
        }
    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.send_flower, null);
        ViewHolder holder = new ViewHolder();
        holder.send = (TextView) view.findViewById(R.id.tv_send);
        holder.sender = (EmojiTextView) view.findViewById(R.id.tv_sender);
        holder.flowerCount = (TextView) view.findViewById(R.id.tv_flower_count);
        holder.rl = (RelativeLayout) view.findViewById(R.id.rl);
        view.setTag(holder);
        return view;
    }
}
