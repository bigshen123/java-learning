"""
加班时长统计工具 - Streamlit Web界面
"""
import streamlit as st
import pandas as pd
import plotly.express as px
import plotly.graph_objects as go
from datetime import datetime, timedelta, time
from database import Database
from calculator import OvertimeCalculator
from models import OvertimeRecord, OvertimeSummary


# 页面配置
st.set_page_config(
    page_title="加班时长统计工具",
    page_icon="⏰",
    layout="wide",
    initial_sidebar_state="expanded"
)

# 初始化数据库
@st.cache_resource
def get_db():
    """获取数据库实例"""
    return Database()


db = get_db()
calculator = OvertimeCalculator()


def show_statistics():
    """显示统计页面"""
    st.title("📊 统计报表")

    # 日期筛选
    col1, col2 = st.columns(2)
    with col1:
        start_date = st.date_input("开始日期", value=None)
    with col2:
        end_date = st.date_input("结束日期", value=None)

    # 转换日期格式
    start_str = start_date.strftime('%Y-%m-%d') if start_date else None
    end_str = end_date.strftime('%Y-%m-%d') if end_date else None

    # 获取统计数据
    stats = db.get_statistics(start_str, end_str)
    records = db.get_all_records(start_str, end_str)

    if not records:
        st.info("暂无数据，请先添加加班记录")
        return

    # 总览卡片
    st.subheader("总览")
    col1, col2, col3, col4 = st.columns(4)

    with col1:
        st.metric(
            label="总加班时长",
            value=f"{stats['total_hours']:.1f} 小时",
            delta=f"{stats['total_days']} 天"
        )

    with col2:
        st.metric(
            label="工作日加班",
            value=f"{stats['weekday_hours']:.1f} 小时"
        )

    with col3:
        st.metric(
            label="周末加班",
            value=f"{stats['weekend_hours']:.1f} 小时"
        )

    with col4:
        st.metric(
            label="节假日加班",
            value=f"{stats['holiday_hours']:.1f} 小时"
        )

    # 图表
    st.subheader("可视化分析")

    # 类型分布饼图
    col1, col2 = st.columns(2)

    with col1:
        fig_pie = go.Figure(data=[go.Pie(
            labels=['工作日', '周末', '节假日'],
            values=[stats['weekday_hours'], stats['weekend_hours'], stats['holiday_hours']],
            hole=0.3
        )])
        fig_pie.update_layout(title="加班类型分布")
        st.plotly_chart(fig_pie, use_container_width=True)

    with col2:
        # 项目分布柱状图
        if stats['project_hours']:
            projects = list(stats['project_hours'].keys())
            hours = list(stats['project_hours'].values())

            fig_bar = px.bar(
                x=projects,
                y=hours,
                title="各项目加班时长",
                labels={'x': '项目', 'y': '时长(小时)'}
            )
            st.plotly_chart(fig_bar, use_container_width=True)
        else:
            st.info("暂无项目数据")

    # 时间趋势图
    if records:
        df = pd.DataFrame([r.to_dict() for r in records])
        df['date'] = pd.to_datetime(df['date'])
        df = df.sort_values('date')

        # 按日期累计加班时长
        daily_hours = df.groupby('date')['hours'].sum().reset_index()
        daily_hours['累计'] = daily_hours['hours'].cumsum()

        fig_line = px.line(
            daily_hours,
            x='date',
            y=['hours', '累计'],
            title="加班时长趋势",
            labels={'date': '日期', 'value': '时长(小时)'},
            markers=True
        )
        st.plotly_chart(fig_line, use_container_width=True)

    # 导出数据
    st.subheader("数据导出")
    if st.button("导出Excel", type="primary"):
        df_export = pd.DataFrame([r.to_dict() for r in records])
        df_export = df_export[['date', 'start_time', 'end_time', 'overtime_type',
                               'project', 'hours', 'reason']]

        # 重命名列
        df_export.columns = ['日期', '开始时间', '结束时间', '加班类型',
                            '项目', '时长(小时)', '原因']

        st.download_button(
            label="下载Excel文件",
            data=df_export.to_excel(index=False, engine='openpyxl'),
            file_name=f"加班记录_{datetime.now().strftime('%Y%m%d')}.xlsx",
            mime="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        )


def show_records():
    """显示记录管理页面"""
    st.title("📝 加班记录管理")

    # 获取所有记录
    records = db.get_all_records()

    # 筛选选项
    with st.expander("筛选选项", expanded=False):
        col1, col2, col3 = st.columns(3)
        with col1:
            start_date = st.date_input("开始日期")
        with col2:
            end_date = st.date_input("结束日期")
        with col3:
            projects = db.get_all_projects()
            project = st.selectbox("项目", ["全部"] + projects)

        if st.button("应用筛选"):
            start_str = start_date.strftime('%Y-%m-%d')
            end_str = end_date.strftime('%Y-%m-%d')
            project_filter = project if project != "全部" else None
            records = db.get_all_records(start_str, end_str, project_filter)

    if not records:
        st.info("暂无加班记录，请在侧边栏添加新记录")
        return

    # 显示记录列表
    for record in records:
        with st.expander(f"{record.date} - {record.project or '无项目'} ({record.hours}小时)",
                        expanded=False):
            col1, col2, col3 = st.columns([2, 1, 1])

            with col1:
                st.write(f"**时间:** {record.start_time} - {record.end_time}")
                st.write(f"**类型:** {record.overtime_type}")
                if record.reason:
                    st.write(f"**原因:** {record.reason}")

            with col2:
                if record.project:
                    st.write(f"**项目:** {record.project}")

            with col3:
                if st.button("编辑", key=f"edit_{record.id}"):
                    st.session_state[f'edit_record_{record.id}'] = record
                if st.button("删除", key=f"delete_{record.id}", type="secondary"):
                    if db.delete_record(record.id):
                        st.success("删除成功")
                        st.rerun()
                    else:
                        st.error("删除失败")


def show_add_record():
    """显示添加记录页面"""
    st.title("➕ 添加加班记录")

    # 选择输入模式
    input_mode = st.radio(
        "输入模式",
        ["打卡自动计算", "手动输入加班时间"],
        help="打卡模式会根据标准工作时间(8:30-17:30)自动计算加班时长"
    )

    with st.form("add_record_form"):
        col1, col2 = st.columns(2)

        with col1:
            date = st.date_input("日期*", value=datetime.now().date())

            if input_mode == "打卡自动计算":
                st.info("💡 标准工作时间: 08:30 - 17:30")
                punch_in = st.time_input("上班打卡时间*", value=time(8, 0), step=60, help="早于8:30的部分算加班")
                punch_out = st.time_input("下班打卡时间*", value=time(18, 30), step=60, help="晚于17:30的部分算加班")
            else:
                start_time = st.time_input("加班开始时间*", value=time(18, 0), step=60)
                end_time = st.time_input("加班结束时间*", value=time(21, 0), step=60)

        with col2:
            project = st.text_input("项目名称", placeholder="输入项目名称")
            reason = st.text_area("加班原因", placeholder="简要说明加班原因")

        # 自动判断加班类型
        overtime_type = calculator.determine_overtime_type(date.strftime('%Y-%m-%d'))
        st.info(f"系统自动识别加班类型: **{overtime_type}**")

        # 计算时长
        if input_mode == "打卡自动计算":
            punch_in_str = punch_in.strftime('%H:%M')
            punch_out_str = punch_out.strftime('%H:%M')
            hours, error = calculator.calculate_by_punch_time(punch_in_str, punch_out_str)

            # 显示详细计算说明
            if not error:
                with st.expander("📊 计算详情", expanded=False):
                    work_start = datetime.strptime(calculator.STANDARD_WORK_START, '%H:%M')
                    work_end = datetime.strptime(calculator.STANDARD_WORK_END, '%H:%M')
                    punch_in_dt = datetime.strptime(punch_in_str, '%H:%M')
                    punch_out_dt = datetime.strptime(punch_out_str, '%H:%M')

                    details = []
                    if punch_in_dt < work_start:
                        early_minutes = (work_start - punch_in_dt).seconds / 60
                        details.append(f"🌅 早到: {early_minutes:.0f}分钟")

                    if punch_out_dt > work_end:
                        late_minutes = (punch_out_dt - work_end).seconds / 60
                        details.append(f"🌙 晚退: {late_minutes:.0f}分钟")

                    st.markdown(" | ".join(details))
                    st.markdown(f"**标准工作时间:** {calculator.STANDARD_WORK_START} - {calculator.STANDARD_WORK_END}")
        else:
            start_str = start_time.strftime('%H:%M')
            end_str = end_time.strftime('%H:%M')
            hours, error = calculator.calculate_hours(start_str, end_str)

        if error:
            st.error(error)
        else:
            st.success(f"预计加班时长: **{hours} 小时**")

        submitted = st.form_submit_button("提交", type="primary")

        if submitted:
            if error:
                st.error(f"提交失败: {error}")
            else:
                if input_mode == "打卡自动计算":
                    # 打卡模式,保存打卡时间
                    record = OvertimeRecord(
                        date=date.strftime('%Y-%m-%d'),
                        start_time=punch_in_str,
                        end_time=punch_out_str,
                        overtime_type=overtime_type,
                        reason=reason,
                        project=project,
                        hours=hours
                    )
                else:
                    # 手动模式
                    record = OvertimeRecord(
                        date=date.strftime('%Y-%m-%d'),
                        start_time=start_str,
                        end_time=end_str,
                        overtime_type=overtime_type,
                        reason=reason,
                        project=project,
                        hours=hours
                    )

                record_id = db.add_record(record)
                st.success(f"添加成功! 记录ID: {record_id}")
                st.balloons()


def main():
    """主函数"""
    # 侧边栏
    with st.sidebar:
        st.title("⏰ 加班统计工具")
        st.markdown("---")

        page = st.radio(
            "选择功能",
            ["添加记录", "记录管理", "统计报表"]
        )

        st.markdown("---")
        st.markdown("### 快速统计")
        stats = db.get_statistics()
        st.metric("总时长", f"{stats['total_hours']:.1f}小时")
        st.metric("总天数", f"{stats['total_days']}天")

    # 根据选择显示不同页面
    if page == "添加记录":
        show_add_record()
    elif page == "记录管理":
        show_records()
    elif page == "统计报表":
        show_statistics()


if __name__ == "__main__":
    main()