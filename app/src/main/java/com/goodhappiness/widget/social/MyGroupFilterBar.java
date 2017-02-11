package com.goodhappiness.widget.social;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.view.recyclerview.TuSdkTableView;
import org.lasque.tusdk.impl.components.widget.filter.GroupFilterItemView;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterBarBase;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterBarInterface;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterItem;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterItemViewBase;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterTableViewInterface;

/**
 * Created by 电脑 on 2016/5/24.
 */
public class MyGroupFilterBar extends GroupFilterBarBase implements GroupFilterBarInterface
{
    /** 布局ID */
    public static int getLayoutId()
    {
        return TuSdkContext.getLayoutResId("my_group_filter_bar");
    }

    public MyGroupFilterBar(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public MyGroupFilterBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public MyGroupFilterBar(Context context)
    {
        super(context);
    }

    /************** 参数 ***************/
    /** 行视图宽度 */
    private int mGroupFilterCellWidth;
    /** 滤镜列表行视图布局资源ID */
    private int mFilterTableCellLayoutId;

    /** 行视图宽度 */
    public int getGroupFilterCellWidth()
    {
        return mGroupFilterCellWidth;
    }

    /** 行视图宽度 */
    @Override
    public void setGroupFilterCellWidth(int mGroupFilterCellWidth)
    {
        this.mGroupFilterCellWidth = mGroupFilterCellWidth;
        if (this.getGroupTable() != null)
        {
            this.getGroupTable().setGroupFilterCellWidth(this.getGroupFilterCellWidth());
        }
        if (this.getFilterTable() != null)
        {
            this.getFilterTable().setGroupFilterCellWidth(this.getGroupFilterCellWidth());
        }
    }


    @Override
    public void setGroupTableCellLayoutId(int mGroupTableCellLayoutId)
    {
    }

    /**
     * 滤镜列表行视图布局资源ID
     *
     * @see #
     *      {@link org.lasque.tusdk.modules.view.widget.filter.GroupFilterItemViewBase}
     * @param /滤镜列表行视图布局资源ID
     *            (默认:
     *            tusdk_impl_component_widget_group_filter_item_view，如需自定义请继承自
     *            GroupFilterItemViewBase)
     */
    public int getFilterTableCellLayoutId()
    {
        if (mFilterTableCellLayoutId == 0)
        {
            mFilterTableCellLayoutId = GroupFilterItemView.getLayoutId();
        }
        return mFilterTableCellLayoutId;
    }

    /**
     * 滤镜列表行视图布局资源ID
     *
     * @see #
     *      {@link org.lasque.tusdk.modules.view.widget.filter.GroupFilterItemViewBase}
     * @param   /滤镜列表行视图布局资源ID
     *            (默认:
     *            tusdk_impl_component_widget_group_filter_item_view，如需自定义请继承自
     *            GroupFilterItemViewBase)
     */
    @Override
    public void setFilterTableCellLayoutId(int mFilterTableCellLayoutId)
    {
        this.mFilterTableCellLayoutId = mFilterTableCellLayoutId;
        if (this.getFilterTable() != null)
        {
            this.getFilterTable().setCellLayoutId(this.getFilterTableCellLayoutId());
        }
    }

    /** 滤镜列表 */
    private GroupFilterTableViewInterface mFilterTable;
    /** 后退按钮 */
    private View mBackButton;

    /** 滤镜分组列表 */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends View & GroupFilterTableViewInterface> T getGroupTable()
    {
        return null;
    }

    /** 滤镜列表 */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends View & GroupFilterTableViewInterface> T getFilterTable()
    {
        if (mFilterTable == null)
        {
            View view = this.getViewById("lsq_filter_list_view");
            if (view == null || !(view instanceof GroupFilterTableViewInterface)) return null;

            mFilterTable = (GroupFilterTableViewInterface) view;

            mFilterTable.setCellLayoutId(this.getFilterTableCellLayoutId());
            mFilterTable.setDisplaySelectionIcon(this.isEnableFilterConfig());
            mFilterTable.setGroupFilterCellWidth(this.getGroupFilterCellWidth());
            mFilterTable.setItemClickDelegate(mFilterTableItemClickDelegate);
            mFilterTable.reloadData();
        }
        return (T) mFilterTable;
    }

    /** 滤镜列表点击事件 */
    private TuSdkTableView.TuSdkTableViewItemClickDelegate<GroupFilterItem, GroupFilterItemViewBase> mFilterTableItemClickDelegate = new TuSdkTableView.TuSdkTableViewItemClickDelegate<GroupFilterItem, GroupFilterItemViewBase>()
    {
        @Override
        public void onTableViewItemClick(GroupFilterItem itemData, GroupFilterItemViewBase itemView, int position)
        {
            onFilterItemSeleced(itemData, itemView, position);
        }
    };

    /** 加载视图 */
    @Override
    public void loadView()
    {
        super.loadView();
//        // 滤镜分组列表
//        getGroupTable();
        // 滤镜列表
        this.showViewIn(getFilterTable(), true);
    }

    /**
     * 显示 滤镜列表视图
     *
     * @param viewCenterX
     * @param isShow
     *            是否显示
     */
    @Override
    protected void showFilterTable(int viewCenterX, boolean isShow)
    {
        super.showFilterTable(viewCenterX, isShow);
    }
}