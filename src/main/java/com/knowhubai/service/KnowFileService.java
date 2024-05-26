package com.knowhubai.service;

import com.knowhubai.common.BaseResponse;
import com.knowhubai.model.dto.QueryFileDTO;
import com.knowhubai.model.dto.UploadFileDTO;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.List;

/**
 * @Project: com.ningning0111.service
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/4/5 20:03
 * @Description:
 */
public interface KnowFileService {
    // 分页查询所有上传的文件
    BaseResponse queryPage(QueryFileDTO request);

    // 删除文件
    BaseResponse deleteFiles(List<String> ids);


    BaseResponse filesStore(UploadFileDTO uploadFileDTO);

    // 初始化向量数据库操作接口
    VectorStore randomGetVectorStore();

}
