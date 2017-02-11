package com.goodhappiness.widget.social;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.utils.anim.AnimHelper;
import org.lasque.tusdk.impl.components.filter.TuEditFilterBarView.TuEditFilterBarDelegate;
import org.lasque.tusdk.impl.components.widget.filter.GroupFilterBar;
import org.lasque.tusdk.impl.view.widget.ParameterConfigViewInterface;
import org.lasque.tusdk.modules.view.widget.filter.FilterSubtitleViewInterface;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterItem;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterItemViewInterface;
import org.lasque.tusdk.modules.view.widget.filter.TuEditFilterViewBase;

import java.util.List;

/**
 * Created by 电脑 on 2016/5/24.
 */
public class MyEditFilterBarView extends TuEditFilterViewBase implements ParameterConfigViewInterface.ParameterConfigViewDelegate
{
    /** 布局ID */
    public static int getLayoutId()
    {
        return TuSdkContext.getLayoutResId("my_edit_filter_bar_view");
    }

    public MyEditFilterBarView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public MyEditFilterBarView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public MyEditFilterBarView(Context context)
    {
        super(context);
    }


    /** 图片编辑滤镜控制器滤镜栏视图委托 */
    private TuEditFilterBarDelegate mDelegate;

    /** 滤镜组选择栏底部距离 (默认：44dp) */
    private int mFilterBarBottom;

    /** 图片编辑滤镜控制器滤镜栏视图委托 */
    public TuEditFilterBarDelegate getDelegate()
    {
        return mDelegate;
    }

    /** 图片编辑滤镜控制器滤镜栏视图委托 */
    public void setDelegate(TuEditFilterBarDelegate mDelegate)
    {
        this.mDelegate = mDelegate;
    }

    /** 滤镜组选择栏底部距离 (默认：44dp) */
    public int getFilterBarBottom()
    {
        return mFilterBarBottom;
    }

    /** 滤镜组选择栏底部距离 (默认：44dp) */
    public void setFilterBarBottom(int mFilterBarBottom)
    {
        this.mFilterBarBottom = mFilterBarBottom;
        if (this.getGroupFilterBar() != null)
        {
            this.getGroupFilterBar().setMarginBottom(this.getFilterBarBottom());
        }
    }

    /*************** View **************/
    /** 参数配置视图 */
    private ParameterConfigViewInterface mConfigView;
    /** 滤镜组选择栏 */
    private GroupFilterBar mGroupFilterBar;

    /** 参数配置视图  seek bar*/
    @SuppressWarnings("unchecked")
    public <T extends View & ParameterConfigViewInterface> T getConfigView()
    {
        if (mConfigView == null)
        {
            View view = this.getViewById("lsq_param_config_view");
            if (view == null || !(view instanceof ParameterConfigViewInterface)) return null;
            mConfigView = (ParameterConfigViewInterface) view;
            if (mConfigView != null)
            {
                mConfigView.setDelegate(this);
            }
        }
        return (T) mConfigView;
    }

    /** 滤镜标题视图 */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends View & FilterSubtitleViewInterface> T getFilterTitleView()
    {
        return null;
    }
    /** 滤镜栏 **/
    @SuppressWarnings("unchecked")
    @Override
    public GroupFilterBar getGroupFilterBar()
    {
        if (mGroupFilterBar == null)
        {
            mGroupFilterBar = this.getViewById("lsq_group_filter_bar");
            this.configGroupFilterBar(mGroupFilterBar, GroupFilterItemViewInterface.GroupFilterAction.ActionEdit);
        }
        return mGroupFilterBar;
    }


    @Override
    public void loadView()
    {
        super.loadView();
        getConfigView();
        getGroupFilterBar();
    }

    /** 取消设置 */
    @Override
    protected void handleCancelAction()
    {
    }


    @Override
    public void setDefaultShowState(boolean isShow)
    {
        this.showViewIn(this.getConfigView(), false);
    }

    /** 加载滤镜 */
    public void loadFilters(FilterOption option)
    {
        if (getGroupFilterBar() != null)
        {
            getGroupFilterBar().loadFilters(option);
        }
    }

    /** 选中一个滤镜数据 */
    protected boolean onFilterSelected(GroupFilterItem itemData)
    {
        if (mDelegate == null) return true;
        return mDelegate.onFilterSelected(null, itemData);
    }

    /** 显示配置视图 */
    @Override
    protected void showConfigView(final boolean isShow)
    {
        this.showViewIn(this.getConfigView(), true);


        if (this.getGroupFilterBar() == null) return;
        this.showViewIn(this.getGroupFilterBar(), true);
        ViewCompat.animate(this.getGroupFilterBar()).alpha(isShow ? 0 : 1).setDuration(220).setListener(new AnimHelper.TuSdkViewAnimatorAdapter()
        {
            @Override
            public void onAnimationEnd(View view, boolean cancelled)
            {
                if (cancelled) return;
                showViewIn(getGroupFilterBar(), !isShow);
                showViewIn(getConfigView(), isShow);
            }
        });
    }

    /**************** Filter config ******************/

    /** 设置配置视图参数 */
    @Override
    protected void setConfigViewParams(List<String> keys)
    {
        if (this.getConfigView() == null || keys == null || keys.size() == 0) return;
        this.getConfigView().setParams(keys, 0);
    }

    /** 请求渲染 */
    @Override
    protected void requestRender()
    {
        super.requestRender();
        if (mDelegate != null)
        {
            mDelegate.onFilterConfigRequestRender(null);
        }
    }

    /****************** ParameterConfigViewDelegate *******************/
    /**
     * 参数数据改变
     *
     * @param view
     *            参数配置视图
     * @param index
     *            参数索引
     * @param progress
     *            百分比进度
     */
    @Override
    public void onParameterConfigDataChanged(ParameterConfigViewInterface view, int index, float progress)
    {
        SelesParameters.FilterArg arg = this.getFilterArg(index);
        if (arg == null) return;

        arg.setPrecentValue(progress);
        this.requestRender();
    }

    /** 重置参数 */
    @Override
    public void onParameterConfigRest(ParameterConfigViewInterface view, int index)
    {
        SelesParameters.FilterArg arg = this.getFilterArg(index);
        if (arg == null) return;

        arg.reset();
        this.requestRender();

        view.seekTo(arg.getPrecentValue());
    }

    /** 读取参数值 */
    @Override
    public float readParameterValue(ParameterConfigViewInterface view, int index)
    {
        SelesParameters.FilterArg arg = this.getFilterArg(index);
        if (arg == null) return 0;
        return arg.getPrecentValue();
    }
}