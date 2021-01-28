package com.baili.admin.controller;

import com.baili.admin.entity.SysFile;
import com.baili.admin.service.FileDealService;
import com.baili.admin.service.SysFileService;
import com.baili.admin.vo.response.FileResponseVO;
import com.baili.common.constant.Constant;
import com.baili.common.entity.Result;
import com.baili.common.exception.BaseException;
import com.baili.common.utils.Utils;
import com.baili.component.NonStaticResourceHttpRequestHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;


/**
 * @author Administrator
 */
@Api(tags = "文件模块")
@RestController
@RequestMapping("/file")
@Slf4j
public class FileDealController {

    @Autowired
    FileDealService fileDealService;
    @Autowired
    private SysFileService sysFileService;
    @Autowired
    private NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;
    @Value("${upload.img}")
    private String uploadImg;
    @Value("${fdfs.storage}")
    private String fileUrl;

    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public Result upload(@RequestParam MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        if (StringUtils.isNotBlank(filename)) {
            String[] fileNameArr = filename.split("\\.");
            String suffix = fileNameArr[fileNameArr.length - 1];
            FileResponseVO upload = fileDealService.upload(file, suffix, uploadImg);
            if (upload != null) {
                return Result.success(upload);
            }
        }
        throw new BaseException(900, "文件上传失败,请检查文件格式");
    }

    @ApiOperation("测试文件下载")
    @GetMapping("download")
    public void download(HttpServletRequest request, HttpServletResponse response, @RequestParam Long id) throws IOException {
        SysFile sysFile = sysFileService.getById(id);
        if (Utils.isNull(sysFile)) {
            throw new BaseException(999, "暂无内容");
        }

        /**
         * 传统下载方式
         */
//        byte[] bytes = fileDealService.downloadFileTest(file.getKey());
//        response.setCharacterEncoding("UTF-8");
//        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
//        // 写出
//        ServletOutputStream outputStream = response.getOutputStream();
//        IOUtils.write(bytes, outputStream);

        String filePath = Constant.HTTP + fileUrl + Constant.SPEPARATOR + sysFile.getKey();
        String fileName = sysFile.getName();
        if (!StringUtils.isEmpty(fileName)) {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        }
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            request.setAttribute(NonStaticResourceHttpRequestHandler.ATTR_FILE, filePath);
            nonStaticResourceHttpRequestHandler.handleRequest(request, response);
        } catch (IOException e) {
        } catch (ServletException e) {
            log.error("error", e);
        }
    }

    @ApiOperation("文件删除")
    @DeleteMapping("delete")
    public String deleteFile(@RequestParam Long fileId) {
        try {
            sysFileService.deleteFile(fileId);
        } catch (Exception e) {
            throw new BaseException(999, "delete error");
        }
        return "delete success";
    }

}
