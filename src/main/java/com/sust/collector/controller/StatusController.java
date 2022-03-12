package com.sust.collector.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.norconex.jef4.status.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class StatusController {
    @GetMapping("/status/{id}")
    public @ResponseBody String getStatus(@PathVariable("id") String id) {

        double progress = getProgress(id);
        JobDuration duration = getDuration(id);
        var start = duration.getStartTime();
        var finish = duration.getEndTime();

        return String.format("progress: {} starttime: {} endtime: {}", progress, start, finish );
    }

    public JobDuration getDuration(String id){
        IJobStatus jobStatus = getJobStatus(id);
        return jobStatus.getDuration();
    }

    public double getProgress(String id){
        IJobStatus jobStatus = getJobStatus(id);
        return jobStatus.getProgress();
    }

    public MutableJobStatus getJobStatus(String id){
        return new MutableJobStatus(id);
    }


}
