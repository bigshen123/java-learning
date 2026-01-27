"""
数据模型定义
"""
from dataclasses import dataclass
from datetime import datetime, time
from typing import Optional


@dataclass
class OvertimeRecord:
    """加班记录"""
    id: Optional[int] = None
    date: str = ""  # 日期 YYYY-MM-DD
    start_time: str = ""  # 开始时间 HH:MM
    end_time: str = ""  # 结束时间 HH:MM
    overtime_type: str = "工作日"  # 加班类型: 工作日/周末/节假日
    reason: str = ""  # 加班原因
    project: str = ""  # 项目名称
    hours: float = 0.0  # 加班时长(小时)
    created_at: Optional[str] = None

    def to_dict(self):
        """转换为字典"""
        return {
            'id': self.id,
            'date': self.date,
            'start_time': self.start_time,
            'end_time': self.end_time,
            'overtime_type': self.overtime_type,
            'reason': self.reason,
            'project': self.project,
            'hours': self.hours,
            'created_at': self.created_at
        }


@dataclass
class OvertimeSummary:
    """加班统计摘要"""
    total_hours: float = 0.0  # 总加班时长
    weekday_hours: float = 0.0  # 工作日加班时长
    weekend_hours: float = 0.0  # 周末加班时长
    holiday_hours: float = 0.0  # 节假日加班时长
    total_days: int = 0  # 加班天数
    project_hours: dict = None  # 各项目加班时长

    def __post_init__(self):
        if self.project_hours is None:
            self.project_hours = {}

    def to_dict(self):
        """转换为字典"""
        return {
            '总加班时长(小时)': round(self.total_hours, 2),
            '工作日加班(小时)': round(self.weekday_hours, 2),
            '周末加班(小时)': round(self.weekend_hours, 2),
            '节假日加班(小时)': round(self.holiday_hours, 2),
            '加班天数': self.total_days,
            '各项目时长': self.project_hours
        }