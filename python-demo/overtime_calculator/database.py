"""
数据库操作模块
"""
import sqlite3
import os
from datetime import datetime
from typing import List, Optional
from models import OvertimeRecord


class Database:
    """数据库操作类"""

    def __init__(self, db_path: str = None):
        """初始化数据库"""
        if db_path is None:
            # 默认保存在data目录下
            db_dir = os.path.join(os.path.dirname(__file__), 'data')
            os.makedirs(db_dir, exist_ok=True)
            db_path = os.path.join(db_dir, 'overtime.db')

        self.db_path = db_path
        self.init_database()

    def get_connection(self):
        """获取数据库连接"""
        conn = sqlite3.connect(self.db_path)
        conn.row_factory = sqlite3.Row
        return conn

    def init_database(self):
        """初始化数据库表"""
        conn = self.get_connection()
        cursor = conn.cursor()

        # 创建加班记录表
        cursor.execute('''
            CREATE TABLE IF NOT EXISTS overtime_records (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                date TEXT NOT NULL,
                start_time TEXT NOT NULL,
                end_time TEXT NOT NULL,
                overtime_type TEXT NOT NULL DEFAULT '工作日',
                reason TEXT,
                project TEXT,
                hours REAL DEFAULT 0.0,
                created_at TEXT DEFAULT CURRENT_TIMESTAMP
            )
        ''')

        # 创建索引
        cursor.execute('''
            CREATE INDEX IF NOT EXISTS idx_date ON overtime_records(date)
        ''')

        conn.commit()
        conn.close()

    def add_record(self, record: OvertimeRecord) -> int:
        """添加加班记录"""
        conn = self.get_connection()
        cursor = conn.cursor()

        cursor.execute('''
            INSERT INTO overtime_records
            (date, start_time, end_time, overtime_type, reason, project, hours)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        ''', (
            record.date,
            record.start_time,
            record.end_time,
            record.overtime_type,
            record.reason,
            record.project,
            record.hours
        ))

        record_id = cursor.lastrowid
        conn.commit()
        conn.close()

        return record_id

    def get_all_records(self, start_date: str = None, end_date: str = None,
                       project: str = None) -> List[OvertimeRecord]:
        """获取所有加班记录"""
        conn = self.get_connection()
        cursor = conn.cursor()

        query = "SELECT * FROM overtime_records WHERE 1=1"
        params = []

        if start_date:
            query += " AND date >= ?"
            params.append(start_date)

        if end_date:
            query += " AND date <= ?"
            params.append(end_date)

        if project:
            query += " AND project = ?"
            params.append(project)

        query += " ORDER BY date DESC"

        cursor.execute(query, params)
        rows = cursor.fetchall()
        conn.close()

        records = []
        for row in rows:
            record = OvertimeRecord(
                id=row['id'],
                date=row['date'],
                start_time=row['start_time'],
                end_time=row['end_time'],
                overtime_type=row['overtime_type'],
                reason=row['reason'] or '',
                project=row['project'] or '',
                hours=row['hours'],
                created_at=row['created_at']
            )
            records.append(record)

        return records

    def get_record_by_id(self, record_id: int) -> Optional[OvertimeRecord]:
        """根据ID获取记录"""
        conn = self.get_connection()
        cursor = conn.cursor()

        cursor.execute("SELECT * FROM overtime_records WHERE id = ?", (record_id,))
        row = cursor.fetchone()
        conn.close()

        if row:
            return OvertimeRecord(
                id=row['id'],
                date=row['date'],
                start_time=row['start_time'],
                end_time=row['end_time'],
                overtime_type=row['overtime_type'],
                reason=row['reason'] or '',
                project=row['project'] or '',
                hours=row['hours'],
                created_at=row['created_at']
            )
        return None

    def update_record(self, record_id: int, record: OvertimeRecord) -> bool:
        """更新加班记录"""
        conn = self.get_connection()
        cursor = conn.cursor()

        cursor.execute('''
            UPDATE overtime_records
            SET date=?, start_time=?, end_time=?, overtime_type=?,
                reason=?, project=?, hours=?
            WHERE id=?
        ''', (
            record.date,
            record.start_time,
            record.end_time,
            record.overtime_type,
            record.reason,
            record.project,
            record.hours,
            record_id
        ))

        affected = cursor.rowcount
        conn.commit()
        conn.close()

        return affected > 0

    def delete_record(self, record_id: int) -> bool:
        """删除加班记录"""
        conn = self.get_connection()
        cursor = conn.cursor()

        cursor.execute("DELETE FROM overtime_records WHERE id = ?", (record_id,))
        affected = cursor.rowcount
        conn.commit()
        conn.close()

        return affected > 0

    def get_all_projects(self) -> List[str]:
        """获取所有项目名称"""
        conn = self.get_connection()
        cursor = conn.cursor()

        cursor.execute("SELECT DISTINCT project FROM overtime_records WHERE project IS NOT NULL AND project != ''")
        rows = cursor.fetchall()
        conn.close()

        return [row['project'] for row in rows]

    def get_statistics(self, start_date: str = None, end_date: str = None) -> dict:
        """获取统计信息"""
        records = self.get_all_records(start_date, end_date)

        total_hours = sum(r.hours for r in records)
        total_days = len(records)

        # 按类型统计
        weekday_hours = sum(r.hours for r in records if r.overtime_type == '工作日')
        weekend_hours = sum(r.hours for r in records if r.overtime_type == '周末')
        holiday_hours = sum(r.hours for r in records if r.overtime_type == '节假日')

        # 按项目统计
        project_hours = {}
        for record in records:
            if record.project:
                project_hours[record.project] = project_hours.get(record.project, 0) + record.hours

        return {
            'total_hours': total_hours,
            'total_days': total_days,
            'weekday_hours': weekday_hours,
            'weekend_hours': weekend_hours,
            'holiday_hours': holiday_hours,
            'project_hours': project_hours
        }