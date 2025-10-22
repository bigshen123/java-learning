package com.bigshen.springbootDemo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.*;

@Entity
@Table(name = "import_rule")
@Data
public class ImportRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ruleCode;
    private String fieldName;
    private String ruleType; // NOT_NULL, REGEX, RANGE, CUSTOM_JS

    @Column(columnDefinition = "TEXT")
    private String ruleContent;
    private String errorMsg;
    private Boolean enabled;
}
