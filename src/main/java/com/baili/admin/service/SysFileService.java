package com.baili.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baili.admin.entity.SysFile;
import com.baili.admin.vo.response.FileResponseVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author lifenxing
 * @date 2020/9/21 17:49
 */
public interface SysFileService extends IService<SysFile> {
    FileResponseVO add(String url, Integer type, Long fileSize, MultipartFile file);

    List<FileResponseVO> getListFile(String fileName, List<Long> ids);

    void deleteFile(Long fileId) throws Exception;
}
