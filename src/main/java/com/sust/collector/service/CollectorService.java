package com.sust.collector.service;

import com.norconex.collector.core.crawler.ICrawler;
import com.norconex.collector.core.crawler.event.CrawlerEvent;
import com.norconex.collector.core.crawler.event.ICrawlerEventListener;
import com.norconex.collector.http.HttpCollector;
import com.norconex.collector.http.HttpCollectorConfig;
import com.norconex.collector.http.crawler.HttpCrawlerConfig;
import com.norconex.collector.http.crawler.URLCrawlScopeStrategy;
import com.norconex.importer.ImporterConfig;
import com.norconex.importer.handler.filter.OnMatch;
import com.norconex.importer.handler.filter.impl.DOMContentFilter;
import com.norconex.importer.handler.tagger.impl.ConstantTagger;
import com.norconex.importer.handler.tagger.impl.DOMTagger;
import com.norconex.importer.handler.tagger.impl.DateFormatTagger;
import com.norconex.importer.handler.transformer.impl.StripAfterTransformer;
import com.norconex.importer.handler.transformer.impl.StripBeforeTransformer;
import com.sust.collector.comitter.KafkaCommiter;
import com.sust.collector.entity.CollectorConfiguration;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Data
@Service
public class CollectorService {

    private String name;
    private String databaseKeySpace;
    private String[] urls;
    private HttpCollector collector;
    private CollectorConfiguration collectorConfig;

    @Autowired
    private KafkaTemplate<Object, Object> kafkaTemplate;

    public CollectorService() {
    }

    public void configure(CollectorConfiguration collectorConfig) {
        this.setName(collectorConfig.getName());
        this.setUrls(new String[]{collectorConfig.getTargetUrl().toString()});
        this.setCollectorConfig(collectorConfig);
    }

    public HttpCollector start(){

        HttpCollectorConfig collectorConfig = new HttpCollectorConfig();
        collectorConfig.setId(name);

        HttpCrawlerConfig crawlerConfig = new HttpCrawlerConfig();
        crawlerConfig.setId(name);
        crawlerConfig.setStartURLs(urls);
        crawlerConfig.setMaxDepth(5);
        crawlerConfig.setImporterConfig(getRegexImporter());
        //crawlerConfig.setImporterConfig(buildImporterConfig());
        //crawlerConfig.setImporterConfig(getRegexImporter());
        crawlerConfig.setCrawlerListeners(getDefaultListener());

        URLCrawlScopeStrategy scopeStrategy = new URLCrawlScopeStrategy();
        scopeStrategy.setStayOnDomain(true);
        crawlerConfig.setUrlCrawlScopeStrategy(scopeStrategy);

        KafkaCommiter committer = new KafkaCommiter(kafkaTemplate);
        committer.setCommitBatchSize(1);
        committer.setQueueSize(1);
        crawlerConfig.setCommitter(committer);

        collectorConfig.setCrawlerConfigs(crawlerConfig);

        HttpCollector collector = new HttpCollector(collectorConfig);
        collector.start(true);
        return collector;
    }

    private ICrawlerEventListener getDefaultListener() {
        return new ICrawlerEventListener() {
            @Override
            public void crawlerEvent(ICrawler crawler, CrawlerEvent event) {
                //if (collectorRepository.existsByName(crawler.getId())){

                    //collectorConfig.setStatus(event.getEventType());
                    //collectorConfig.setProgress((HttpCrawler)crawler.getCrawlerConfig());
                    //collectorRepository.save(collectorConfig);
                //}
            }
        };
    }

    private ImporterConfig buildImporterConfig(){

        DOMContentFilter domContentFilter = new DOMContentFilter();
        domContentFilter.setExtract("text");
        domContentFilter.setOnMatch(OnMatch.INCLUDE);
        domContentFilter.setCaseSensitive(false);
        domContentFilter.setSelector(collectorConfig.getArticleClass());

        StripBeforeTransformer stripBeforeTransformer = new StripBeforeTransformer();
        stripBeforeTransformer.setStripBeforeRegex(collectorConfig.getArticleStartRegex());
        StripAfterTransformer stripAfterTransformer = new StripAfterTransformer();
        stripAfterTransformer.setStripAfterRegex(collectorConfig.getArticleEndRegex());

        DOMTagger domTagger = new DOMTagger();
        DOMTagger.DOMExtractDetails details = new DOMTagger.DOMExtractDetails();
        details.setSelector(collectorConfig.getMetadataDateClass());
        details.setToField("raw_date");
        domTagger.addDOMExtractDetails(details);

        details = new DOMTagger.DOMExtractDetails();
        details.setSelector(collectorConfig.getMetadataTitleClass());
        details.setToField("title");
        domTagger.addDOMExtractDetails(details);

        DateFormatTagger dateTimeFormat = new DateFormatTagger();
        dateTimeFormat.setFromField("raw_date");
        dateTimeFormat.setToField("pub_date_time");
        dateTimeFormat.setFromFormats(collectorConfig.getDateTimeFormatFrom());
        dateTimeFormat.setToFormat(collectorConfig.getDateTimeFormatTo());

        DateFormatTagger dateFormat = new DateFormatTagger();
        dateFormat.setFromField("raw_date");
        dateFormat.setToField("pub_date");
        dateFormat.setFromFormats(collectorConfig.getDateFormatFrom());
        dateFormat.setToFormat(collectorConfig.getDateFormatTo());

        ConstantTagger constantTagger = new ConstantTagger();
        constantTagger.addConstant("source", collectorConfig.getTargetUrl().toString());

        ImporterConfig config = new ImporterConfig();
        config.setPreParseHandlers(
                domContentFilter,
                domTagger,
                dateFormat,
                dateTimeFormat,
                constantTagger,
                stripBeforeTransformer,
                stripAfterTransformer);

        return config;
    }

    private ImporterConfig getRegexImporter() {

        DOMContentFilter domContentFilter = new DOMContentFilter();
        domContentFilter.setExtract("text");
        domContentFilter.setOnMatch(OnMatch.INCLUDE);
        domContentFilter.setCaseSensitive(false);
        domContentFilter.setSelector("div.sics-component__story");

        StripBeforeTransformer stripBeforeTransformer = new StripBeforeTransformer();
        stripBeforeTransformer.setStripBeforeRegex("<div class=\"sics-component__story\".*?>");
        StripAfterTransformer stripAfterTransformer = new StripAfterTransformer();
        stripAfterTransformer.setStripAfterRegex("<div id=\"foot-video\"");

        DOMTagger domTagger = new DOMTagger();
        DOMTagger.DOMExtractDetails details = new DOMTagger.DOMExtractDetails();
        details.setSelector(".sics-component__byline__date");
        details.setToField("raw_date");
        details.setToField("title");
        domTagger.addDOMExtractDetails(details);

        DateFormatTagger dateFormat = new DateFormatTagger();
        dateFormat.setFromField("raw_date");
        dateFormat.setToField("pub_date");
        dateFormat.setFromFormats("mm:ss, MMM dd yyyy");
        dateFormat.setToFormat("yyyy-MM-dd'T'HH:mm:ss");

        ImporterConfig config = new ImporterConfig();
        config.setPreParseHandlers(
                domContentFilter,
                domTagger,
                dateFormat,
                stripBeforeTransformer,
                stripAfterTransformer);

        return config;
    }
}
