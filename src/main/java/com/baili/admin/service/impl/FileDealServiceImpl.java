package com.baili.admin.service.impl;

import com.github.tobato.fastdfs.domain.fdfs.FileInfo;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.baili.admin.service.FileDealService;
import com.baili.admin.service.SysFileService;
import com.baili.admin.util.ConfigUtils;
import com.baili.admin.vo.response.FileResponseVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author Administrator
 */
@Service
public class FileDealServiceImpl implements FileDealService {

    @Autowired
    FastFileStorageClient fastFileStorageClient;

    @Autowired
    private SysFileService sysFileService;

    @Override
    public FileResponseVO upload(MultipartFile file, String fileExtName, String uploadImg) throws IOException {
        List<String> list = ConfigUtils.parseParam(uploadImg);
        if (list.contains(fileExtName)) {
            StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream()
                    , file.getSize(), fileExtName, null);
            FileInfo fileInfo = fastFileStorageClient.queryFileInfo(storePath.getGroup(), storePath.getPath());
            FileResponseVO fileResponseVO = sysFileService.add(storePath.getFullPath(), 1, fileInfo.getFileSize(), file);
            return fileResponseVO;
        }
        return null;
    }

    /**
     * 下载文件
     */
    @Override
    public byte[] downloadFileTest(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) {
            return null;
        }
        StorePath storePath = StorePath.parseFromUrl(fileUrl);
        byte[] bytes = fastFileStorageClient.downloadFile(storePath.getGroup(), storePath.getPath(), new DownloadByteArray());
        return bytes;
    }

    /**
     * 删除文件
     */
    @Override
    public String deleteFile(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) {
            return null;
        }
        StorePath storePath = StorePath.parseFromUrl(fileUrl);
        fastFileStorageClient.deleteFile(storePath.getGroup(), storePath.getPath());
        return fileUrl;
    }

}
