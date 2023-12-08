package com.bigshen.springbootDemo.service.impl;

import com.bigshen.springbootDemo.service.PersonService;
import org.springframework.stereotype.Service;

/**
 * @author byj
 * @date 2023/12/8
 * @Description
 */
@Service
public class PersonServiceImpl implements PersonService {
    @Override
    public Object findOne1() {
        return "test";
    }
}
