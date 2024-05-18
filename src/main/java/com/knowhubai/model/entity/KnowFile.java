package com.knowhubai.model.entity;

import com.knowhubai.conver.JpaConverterListJson;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author ：何汉叁
 * @date ：2024/4/10 19:41
 * @description：TODO
 */
@Data
@Table(name = "knowFile")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KnowFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    /**
     * 文件名称
     */
    @Column(columnDefinition = "TEXT")
    private String fileName;

    /**
     * Minio文件url
     */
    @Column(columnDefinition = "TEXT")
    private String url;

    /**
     * 该文件分割出的多段向量文本ID
     */
    @Convert(converter = JpaConverterListJson.class)
    @Column(columnDefinition = "TEXT")
    private List<String> vectorId;

    /**
     * 创建时间/上传时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
