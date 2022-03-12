package com.sust.collector.helpers;

import com.sust.collector.entity.CollectorConfiguration;

public class TestData {

    public static CollectorConfiguration getDefaultConfig(){
        CollectorConfiguration collectorConfig = new CollectorConfiguration();
        collectorConfig.setName("testing");
        collectorConfig.setTargetUrl("https://www.stuff.co.nz");
        collectorConfig.setArticleClass("div.sics-component__story");
        collectorConfig.setArticleStartRegex("<div class=\"sics-component__story\".*?>");
        collectorConfig.setArticleEndRegex("<div id=\"foot-video\"");
        collectorConfig.setMetadataTitleClass(".sics-component__byline__date");
        collectorConfig.setDateFormatFrom("mm:ss, MMM dd yyyy");
        collectorConfig.setDateFormatTo("yyyy-MM-dd'T'HH:mm:ss");
        collectorConfig.setDateTimeFormatFrom("mm:ss, MMM dd yyyy");
        collectorConfig.setDateTimeFormatTo("yyyy-MM-dd'T'HH:mm:ss");
        return collectorConfig;
    }

}
