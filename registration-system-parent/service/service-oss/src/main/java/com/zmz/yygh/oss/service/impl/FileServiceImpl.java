package com.zmz.yygh.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.zmz.yygh.common.exception.YyghException;
import com.zmz.yygh.common.result.ResultCodeEnum;
import com.zmz.yygh.oss.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.oss.secret}")
    private String accessKeySecret;

    @Value("${aliyun.oss.bucket}")
    private String bucket;


    @Override
    public String upload(MultipartFile file) {

// yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
//        String endpoint = endpoint;
// 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
//        String accessKeyId = "yourAccessKeyId";
//        String accessKeySecret = "yourAccessKeySecret";

// 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

// 填写本地文件的完整路径。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
        InputStream inputStream = null;
        String filename = null;
        try {
            inputStream = file.getInputStream();
            filename = file.getOriginalFilename();
            //这里的filename如果不做处理，不同用户上传相同的用户名。会导致前者被覆盖
            //因此这里需要在文件名这里增加一个UUID或者是时间戳避免重名问题
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            filename = uuid.concat("-").concat(filename);

        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }
        log.info("文件正在上传,inputStream为 {}, filename为 {}", inputStream, filename);
// 依次填写Bucket名称（例如examplebucket）和Object完整路径（例如exampledir/exampleobject.txt）。Object完整路径中不能包含Bucket名称。
        ossClient.putObject(bucket, filename, inputStream);
// 关闭OSSClient。
        ossClient.shutdown();

        String url = "https://" + bucket + "." + endpoint + "/" + filename;
        log.info("文件上传成功！返回的utl为 {}", url);
        //返回
        return url;

    }


}
