package com.incidentops.ai.indexing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class ChunkingService {
    List<Document> chunking(Document document){
        TokenTextSplitter splitter = TokenTextSplitter.builder()
                .withChunkSize(500)
                .withMinChunkSizeChars(200)
                .withMinChunkLengthToEmbed(20)
                .withMaxNumChunks(1000)
                .withKeepSeparator(true)
                .build();
        List<Document> chunks = splitter.split(document);
        log.info("Chunking done");
        return chunks;
    }
}
