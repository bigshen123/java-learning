package com.bigshen.springbootDemo.repository;

import com.bigshen.springbootDemo.model.ImportRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImportRuleRepo extends JpaRepository<ImportRule, Long> {
    List<ImportRule> findByEnabledTrue();
}
