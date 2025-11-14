package cn.lyp.data.controller;

import cn.lyp.basics.log.LogType;
import cn.lyp.basics.log.SystemLog;
import cn.lyp.basics.utils.*;
import cn.lyp.data.entity.Setting;
import cn.lyp.data.service.IFileService;
import cn.lyp.data.service.ISettingService;
import cn.lyp.data.utils.LypFileUtils;
import cn.lyp.basics.baseVo.Result;
import cn.lyp.data.entity.File;
import cn.hutool.core.util.StrUtil;
import cn.lyp.data.vo.OssSettingVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@Api(tags = "文件上传接口")
@RequestMapping("/upload")
@Transactional
public class UploadController {

    @Autowired
    private LypFileUtils lypFileUtils;

    @Autowired
    private ISettingService iSettingService;

    @Autowired
    private IFileService iFileService;

    @SystemLog(about = "文件上传", type = LogType.DATA_CENTER,doType = "FILE-06")
    @RequestMapping(value = "/file", method = RequestMethod.POST)
    @ApiOperation(value = "文件上传")
    public Result<Object> upload(@RequestParam(required = false) MultipartFile file,@RequestParam(required = false) String base64) {
        if(StrUtil.isNotBlank(base64)){
            file = Base64DecodeMultipartFile.base64Convert(base64);
        }
        String result = null;
        String fKey = CommonUtil.renamePic(file.getOriginalFilename());
        File f = new File();
        try {
            InputStream inputStream = file.getInputStream();
            result = lypFileUtils.inputStreamUpload(inputStream, fKey, file);
            f.setLocation(0);
            f.setName(file.getOriginalFilename());
            f.setSize(file.getSize());
            f.setType(file.getContentType());
            f.setFKey(fKey);
            f.setUrl(result);
            iFileService.saveOrUpdate(f);
        } catch (Exception e) {
            return ResultUtil.error(e.toString());
        }
        OssSettingVo vo = getOssSetting();
        return ResultUtil.data(vo.getFileHttp() + vo.getFileView() + "/" + f.getId());
    }

    public OssSettingVo getOssSetting() {
        Setting s1 = iSettingService.getById("FILE_VIEW");
        Setting s2 = iSettingService.getById("FILE_HTTP");
        Setting s3 = iSettingService.getById("FILE_PATH");
        if(s1 == null || s1 == null || s1 == null) {
            return null;
        }
        return new OssSettingVo(s1.getValue(),s2.getValue(),s3.getValue());
    }
}
