package com.knowhubai.service.impl;

import com.knowhubai.common.BaseResponse;
import com.knowhubai.common.ErrorCode;
import com.knowhubai.common.ResultUtils;
import com.knowhubai.exception.BusinessException;
import com.knowhubai.model.dto.QueryFileDTO;
import com.knowhubai.model.entity.AIApi;
import com.knowhubai.model.entity.KnowFile;
import com.knowhubai.repository.KnowFileRepository;
import com.knowhubai.service.AIApiService;
import com.knowhubai.service.KnowFileService;
import com.knowhubai.utils.MatchUtil;
import com.knowhubai.utils.MinioUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.openai.OpenAiEmbeddingClient;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ：何汉叁、NingNing0111
 * @date ：2024/4/7 21:16
 * @description：知识库（Minio文件存储服务版）
 * 获取文件功能未加
 */
@Service()
@Slf4j
@RequiredArgsConstructor
public class KnowFileServiceImpl implements KnowFileService {
    private final AIApiService aiApiService;
    private final JdbcTemplate jdbcTemplate;
    private final TokenTextSplitter tokenTextSplitter;
    private final KnowFileRepository fileRepository;
    private final MinioUtil minioUtil;

    /**
     * 查询文件
     * @param request
     * @return
     */
    @Override
    public BaseResponse queryPage(QueryFileDTO request) {
        Page<KnowFile> fileList = fileRepository.findByFileNameContaining(request.fileName(), PageRequest.of(request.page(), request.pageSize()));
        return ResultUtils.success(fileList);
    }

    /**
     * 删除指定ID列表的所有文件
     * @param ids
     * @return
     */
    @Transactional(rollbackOn = Exception.class)
    @Override
    public BaseResponse deleteFiles(List<Long> ids) {
        List<KnowFile> KnowFiles = fileRepository.findAllById(ids);
        fileRepository.deleteAllById(ids);

        VectorStore vectorStore = randomGetVectorStore();

        for(KnowFile file: KnowFiles){
            String KnowFileName = MatchUtil.getFileName(file.getUrl());
            minioUtil.deleteFile(KnowFileName);
            vectorStore.delete(file.getVectorId());

        }
        return ResultUtils.success("删除成功");
    }

    @Override
    public BaseResponse fileStore(MultipartFile file) {
        try {
            Resource resource = file.getResource();
            VectorStore vectorStore = randomGetVectorStore();
            TikaDocumentReader tkReader = new TikaDocumentReader(resource);
            List<Document> documents = tkReader.get();
            List<Document> applyList = tokenTextSplitter.apply(documents);
            vectorStore.accept(applyList);

            String name = file.getOriginalFilename();
            String url = minioUtil.uploadFile(file);
            log.info(url);
            long currMillis = System.currentTimeMillis();
            fileRepository.save(KnowFile.builder()
                    .fileName(name)
                    .vectorId(applyList.stream().map(Document::getId).collect(Collectors.toList()))
                    .url(url)
                    .createTime(new Date(currMillis))
                    .updateTime(new Date(currMillis))
                    .build());
            return ResultUtils.success("上传成功");
        }catch (Exception e){
            e.printStackTrace();
            throw new BusinessException(ErrorCode.OPERATION_ERROR,e.getMessage());
        }

    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public BaseResponse filesStore(List<MultipartFile> files) {
        try {
            for (MultipartFile file :
                    files) {
                fileStore(file);
            }
            return ResultUtils.success("上传成功");
        }catch (Exception e){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,e.getMessage());
        }
    }

    @Override
    public VectorStore randomGetVectorStore(){
        AIApi oneApi = aiApiService.randomGetOne();
        OpenAiApi openAiApi = new OpenAiApi(oneApi.getBaseUrl(), oneApi.getApiKey());
        EmbeddingClient openAiEmbeddingClient = new OpenAiEmbeddingClient(openAiApi);
        return new PgVectorStore(jdbcTemplate,openAiEmbeddingClient);
    }
}