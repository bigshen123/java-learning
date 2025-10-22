package com.bigshen.springbootDemo.service;

import com.bigshen.springbootDemo.model.ImportRowDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportService {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void batchInsert(List<ImportRowDTO> rows) {
        String sql = "INSERT INTO target_table (id, name, age, phone, email) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, rows, 1000, (ps, row) -> {
            ps.setObject(1, row.getId());
            Integer age = null;
            try { age = row.getAge() == null ? null : Integer.parseInt(row.getAge()); } catch (Exception e) {}
            ps.setObject(2, row.getName());
            ps.setObject(3, age);
            ps.setObject(4, row.getPhone());
            ps.setObject(5, row.getEmail());
        });
    }
}
