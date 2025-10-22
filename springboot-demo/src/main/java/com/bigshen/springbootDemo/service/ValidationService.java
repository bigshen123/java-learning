package com.bigshen.springbootDemo.service;

import com.bigshen.springbootDemo.model.ImportRowDTO;
import com.bigshen.springbootDemo.model.ImportRule;
import com.bigshen.springbootDemo.repository.ImportRuleRepo;
import com.bigshen.springbootDemo.util.RuleEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ValidationService {

    private final ImportRuleRepo ruleRepo;

    private final ExecutorService executor = Executors.newFixedThreadPool(
            Math.max(4, Runtime.getRuntime().availableProcessors())
    );

    public List<ImportRowDTO> validateAll(List<ImportRowDTO> rows) {
        List<ImportRule> rules = ruleRepo.findByEnabledTrue();
        if (rules == null) rules = Collections.emptyList();

        Map<String, List<ImportRule>> rulesByField = rules.stream()
                .collect(Collectors.groupingBy(ImportRule::getFieldName));

        // 保留顺序
        return rows.stream()
                .map(row -> validateRow(row, rulesByField))
                .collect(Collectors.toList());
    }

    private ImportRowDTO validateRow(ImportRowDTO row, Map<String, List<ImportRule>> rulesByField) {
        List<String> errors = new ArrayList<>();

        List<ImportRule> nameRules = rulesByField.getOrDefault("name", Collections.emptyList());
        for (ImportRule rule : nameRules) {
            boolean ok = RuleEngine.eval(rule, row.getName());
            if (!ok) errors.add(rule.getErrorMsg());
        }

        List<ImportRule> ageRules = rulesByField.getOrDefault("age", Collections.emptyList());
        for (ImportRule rule : ageRules) {
            boolean ok = RuleEngine.eval(rule, row.getAge());
            if (!ok) errors.add(rule.getErrorMsg());
        }

        List<ImportRule> phoneRules = rulesByField.getOrDefault("phone", Collections.emptyList());
        for (ImportRule rule : phoneRules) {
            boolean ok = RuleEngine.eval(rule, row.getPhone());
            if (!ok) errors.add(rule.getErrorMsg());
        }

        List<ImportRule> emailRules = rulesByField.getOrDefault("email", Collections.emptyList());
        for (ImportRule rule : emailRules) {
            boolean ok = RuleEngine.eval(rule, row.getEmail());
            if (!ok) {
                errors.add(rule.getErrorMsg());
            }
        }

        if (!errors.isEmpty()) {
            row.setErrorInfo(String.join("；", errors));
        }
        return row;
    }
}
