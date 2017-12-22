package com.andywang.ulife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.andywang.ulife.R;
import com.andywang.ulife.entity.calendar.bean.Joke;
import com.andywang.ulife.util.style.FontChangeManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by parting_soul on 2016/11/5.
 */

public class JokeFragmentAdapter extends BaseFragmentAdapter<Joke> {

    private Context mContext;

    private int textStyleId;

    private Map<Integer, Boolean> map;

    private JokeCollectionCallBack mJokeCollectionCallBack;

    public JokeCollectionCallBack getJokeCollectionCallBack() {
        return mJokeCollectionCallBack;
    }

    public void setJokeCollectionCallBack(JokeCollectionCallBack mJokeCollectionCallBack) {
        this.mJokeCollectionCallBack = mJokeCollectionCallBack;
    }

    public JokeFragmentAdapter(Context context, List<Joke> data) {
        mContext = context;
        this.mLists = data;
        textStyleId = FontChangeManager.changeJokeFontSize();
        map = new HashMap<Integer, Boolean>();
        init_is_collected();
    }

    @Override
    public void getPicUrl() {

    }

    public void init_is_collected() {
        for (int i = 0; i < mLists.size(); i++) {
            map.put(i, mLists.get(i).is_collected());
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        JokeHolder holder = null;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.funny_listview_item, null);
            holder = new JokeHolder();
            holder.content = view.findViewById(R.id.content);
            holder.collection = view.findViewById(R.id.is_collectedBox);
            holder.date = view.findViewById(R.id.update_date);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (JokeHolder) view.getTag();
        }
        holder.content.setText(mLists.get(position).getContent());
        holder.date.setText(mLists.get(position).getDate());
        holder.content.setTextAppearance(mContext, textStyleId);

        final JokeHolder finalHolder = holder;
        holder.collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSelected = !mLists.get(position).is_collected();
                map.put(position, isSelected);
                mLists.get(position).setIs_collected(isSelected);
 //               Toast.makeText(mContext, "click", Toast.LENGTH_SHORT).show();
                if (mJokeCollectionCallBack != null) {
                    mJokeCollectionCallBack.setJokeCollectedState(v,mLists.get(position).getHashId(), position, isSelected);
                }
            }
        });
        holder.collection.setChecked(mLists.get(position).is_collected());
        return view;
    }

    class JokeHolder {
        TextView content;
        TextView date;
        CheckBox collection;
    }

    public interface JokeCollectionCallBack {
        public void setJokeCollectedState(View checkBox, String hashID, int pos, boolean isSelected);
    }

}
