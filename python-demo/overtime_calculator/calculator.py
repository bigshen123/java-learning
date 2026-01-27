"""
加班时长计算工具
"""
from datetime import datetime, time
from typing import Tuple


class OvertimeCalculator:
    """加班时长计算器"""

    # 标准工作时间为 8:30-17:30
    STANDARD_WORK_START = "08:30"
    STANDARD_WORK_END = "17:30"

    @staticmethod
    def calculate_by_punch_time(punch_in: str, punch_out: str) -> Tuple[float, str]:
        """
        根据打卡时间自动计算加班时长(早到或晚退都算加班)

        标准工作时间: 8:30-17:30
        - 早于8:30打卡的,早到的时间算加班
        - 晚于17:30打卡的,晚退的时间算加班
        - 周末和节假日全天算加班

        Args:
            punch_in: 上班打卡时间 (HH:MM)
            punch_out: 下班打卡时间 (HH:MM)

        Returns:
            (加班时长(小时), 错误信息)
        """
        try:
            punch_in_time = datetime.strptime(punch_in, '%H:%M')
            punch_out_time = datetime.strptime(punch_out, '%H:%M')
            work_start = datetime.strptime(OvertimeCalculator.STANDARD_WORK_START, '%H:%M')
            work_end = datetime.strptime(OvertimeCalculator.STANDARD_WORK_END, '%H:%M')

            overtime_minutes = 0

            # 计算早到时间(早于8:30的部分)
            if punch_in_time < work_start:
                early_minutes = (work_start - punch_in_time).seconds / 60
                overtime_minutes += early_minutes

            # 计算晚退时间(晚于17:30的部分)
            if punch_out_time > work_end:
                late_minutes = (punch_out_time - work_end).seconds / 60
                overtime_minutes += late_minutes

            # 转换为小时
            hours = round(overtime_minutes / 60, 2)

            if hours <= 0:
                return 0.0, "没有加班(请在8:30前打卡或17:30后打卡)"

            return hours, ""

        except ValueError as e:
            return 0.0, f"时间格式错误: {str(e)}"

    @staticmethod
    def calculate_hours(start_time: str, end_time: str,
                       break_minutes: int = 0) -> Tuple[float, str]:
        """
        计算加班时长

        Args:
            start_time: 开始时间 (HH:MM)
            end_time: 结束时间 (HH:MM)
            break_minutes: 休息时长(分钟)

        Returns:
            (加班时长(小时), 错误信息)
        """
        try:
            # 解析时间
            start = datetime.strptime(start_time, '%H:%M')
            end = datetime.strptime(end_time, '%H:%M')

            # 计算时长(分钟)
            if end >= start:
                # 当天加班
                total_minutes = (end - start).seconds / 60
            else:
                # 跨天加班(比如到第二天凌晨)
                total_minutes = (24 * 60 - (start - end).seconds / 60)

            # 减去休息时间
            total_minutes -= break_minutes

            # 转换为小时
            hours = round(total_minutes / 60, 2)

            if hours <= 0:
                return 0.0, "加班时长不能为负数或零"

            return hours, ""

        except ValueError as e:
            return 0.0, f"时间格式错误: {str(e)}"

    @staticmethod
    def determine_overtime_type(date_str: str) -> str:
        """
        判断加班类型(工作日/周末/节假日)

        Args:
            date_str: 日期字符串 (YYYY-MM-DD)

        Returns:
            加班类型: '工作日', '周末', '节假日'
        """
        try:
            date_obj = datetime.strptime(date_str, '%Y-%m-%d')
            weekday = date_obj.weekday()  # 0=周一, 6=周日

            if weekday >= 5:  # 5=周六, 6=周日
                return '周末'
            else:
                return '工作日'

        except ValueError:
            return '工作日'  # 默认为工作日

    @staticmethod
    def format_hours(hours: float) -> str:
        """
        格式化时长显示

        Args:
            hours: 小时数

        Returns:
            格式化的字符串 (如: 2.5小时 = 2小时30分钟)
        """
        total_minutes = int(hours * 60)
        h = total_minutes // 60
        m = total_minutes % 60

        if m == 0:
            return f"{h}小时"
        else:
            return f"{h}小时{m}分钟"

    @staticmethod
    def calculate_duration_hours(start_time: str, end_time: str) -> float:
        """
        简单计算两个时间点之间的小时数

        Args:
            start_time: 开始时间
            end_time: 结束时间

        Returns:
            小时数
        """
        hours, error = OvertimeCalculator.calculate_hours(start_time, end_time)
        return hours

    @staticmethod
    def validate_time_range(start_time: str, end_time: str) -> Tuple[bool, str]:
        """
        验证时间范围是否合理

        Args:
            start_time: 开始时间
            end_time: 结束时间

        Returns:
            (是否有效, 错误信息)
        """
        try:
            start = datetime.strptime(start_time, '%H:%M')
            end = datetime.strptime(end_time, '%H:%M')

            # 计算时长
            if end >= start:
                total_minutes = (end - start).seconds / 60
            else:
                total_minutes = (24 * 60 - (start - end).seconds / 60)

            # 单次加班不能超过24小时
            if total_minutes > 24 * 60:
                return False, "加班时长不能超过24小时"

            # 单次加班至少15分钟
            if total_minutes < 15:
                return False, "加班时长至少需要15分钟"

            return True, ""

        except ValueError:
            return False, "时间格式不正确"