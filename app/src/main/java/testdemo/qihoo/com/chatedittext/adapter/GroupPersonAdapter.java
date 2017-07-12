package testdemo.qihoo.com.chatedittext.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import testdemo.qihoo.com.chatedittext.R;
import testdemo.qihoo.com.chatedittext.bean.Person;

/**
 * 群成员界面的适配器
 *
 * @author yuanyc
 * @time 2017/7/11 17:49
 */

public class GroupPersonAdapter extends BaseAdapter {
    private List<Person> mPersonList;
    private Context mContext;
    private int[] imageIds = {R.mipmap.icon_1, R.mipmap.icon_2, R.mipmap.icon_3, R.mipmap.icon_4, R.mipmap.icon_5, R.mipmap.icon_6, R.mipmap.icon_7, R.mipmap.icon_8, R.mipmap.icon_9, R.mipmap.icon_10, R.mipmap.icon_11, R.mipmap.icon_12, R.mipmap.icon_13, R.mipmap.icon_14, R.mipmap.icon_15, R.mipmap.icon_16, R.mipmap.icon_17, R.mipmap.icon_18, R.mipmap.icon_19, R.mipmap.icon_20};

    public GroupPersonAdapter(List<Person> personList, Context context) {
        this.mPersonList = personList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return null!=mPersonList?mPersonList.size():0;
    }

    @Override
    public Object getItem(int position) {
        Person person = null;
        if(null!=mPersonList){
            person = mPersonList.get(position);
        }
        return person;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.group_item, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.imageView.setImageResource(imageIds[position]);
        viewHolder.textView.setText(mPersonList.get(position).getName());
        return convertView;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView textView;
    }

}
