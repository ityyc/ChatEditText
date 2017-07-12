package testdemo.qihoo.com.chatedittext.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import testdemo.qihoo.com.chatedittext.R;
import testdemo.qihoo.com.chatedittext.adapter.GroupPersonAdapter;
import testdemo.qihoo.com.chatedittext.bean.Person;
import testdemo.qihoo.com.chatedittext.constant.GlobalConfig;

/**
 * 群组成员列表页面
 *
 * @author yuanyc
 * @time 2017/7/11 17:18
 */

public class GroupPersonActivity extends Activity implements AdapterView.OnItemClickListener {

    @BindView(R.id.lisView)
    ListView lisView;
    /**
     * 保存选中的人对应的id的字符串 id以空格分隔
     */
    private String alreadySelectIds;
    /**
     * 用于存储所有群成员的集合
     */
    private List<Person> personList = new ArrayList<>();

    private GroupPersonAdapter groupPersonAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group);
        ButterKnife.bind(this);
        //获取上一个页面传递过来的数据
        Intent intent = getIntent();
        alreadySelectIds = intent.getStringExtra(GlobalConfig.KEY_SELECTED);
        Log.i(GlobalConfig.TAG, "alreadySelectIds:" + alreadySelectIds);
        //更新ListView
        updateListView();
        //注册单击监听器
        lisView.setOnItemClickListener(this);

    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            Person person = new Person();
            person.setId(String.valueOf(i));
            person.setName("袁永超" + i);
            personList.add(person);
        }
    }


    private void updateListView() {
        if (null != personList) {
            personList.clear();
        }
        initData();
        //移除已经选中的
        removeSelectIds();
        //设置适配器
        groupPersonAdapter = new GroupPersonAdapter(personList, this);
        //更新ListView
        lisView.setAdapter(groupPersonAdapter);
    }

    /**
     * 从集合中移除已经选中的id
     */
    private void removeSelectIds() {
        List<Person> tempList = new ArrayList<>();
        Person person;
        for (int i = 0; i < personList.size(); i++) {
            person = personList.get(i);
            //如果在已经选中的ids中存在
            if (null != alreadySelectIds) {
                if (alreadySelectIds.contains(person.getId())) {
                    tempList.add(person);
                }
            }
        }
        //从群成员列表中移除所有已经@过的成员
        personList.removeAll(tempList);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //通过位置获取当前位置的数据
        Person person = (Person) parent.getItemAtPosition(position);
        if (null != person) {
            Intent intent = new Intent();
            intent.putExtra(GlobalConfig.KEY_ID, person.getId());
            intent.putExtra(GlobalConfig.KEY_NAME, "@" + person.getName() + " ");
            setResult(RESULT_OK, intent);
            //关掉当前页面
            finish();
        }
    }
}
