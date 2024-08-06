package com.bigshen.learningDemo.JUC.future.crl;

import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author byj
 * @date 2024/7/16
 * @Description
 */
@RestController
@RequestMapping("/crl-cache")
public class CRLCacheController {
    @PostMapping("/build")
    public Map<String, String> buildCrlCache(@RequestParam String crlDir, @RequestParam String crlCacheFilePath) {
        String taskId = CRLCacheBuilder.submitBuildCrlCacheTask(crlDir, crlCacheFilePath);
        Map<String, String> response = new HashMap<>();
        response.put("taskId", taskId);
        return Collections.unmodifiableMap(response);
    }

    @GetMapping("/status/{taskId}")
    public Map<String, String> getTaskStatus(@PathVariable String taskId) {
        TaskStatus status = CRLCacheBuilder.getTaskStatus(taskId);
        Map<String, String> response = new HashMap<>();
        response.put("status", status.toString());
        return Collections.unmodifiableMap(response);
    }
}
