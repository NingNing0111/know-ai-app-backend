# Know Hub AI APP 后端

&emsp;使用Spring AI + MinIO搭建的大语言模型RAG项目,权限校验使用Spring Security.

## API地址

- knife4j: [http://localhost:8818/doc.html#/home](http://localhost:8818/doc.html#/home)
- swagger: [http://localhost:8818/swagger-ui/index.html](http://localhost:8818/swagger-ui/index.html)

## 特性

- 使用Spring AI简化主流大语言模型的调用；
- pgvector作为向量数据库，MinIO作为知识库的存储平台；
- 使用Spring Security实现JWT认证和无感刷新token，简化权限校验操作；
- 使用Spring Mail实现邮件发送，支持通过邮件激活账户；
- 提供dall-e-2/3绘图模型调用接口；
- 支持Docker一键部署