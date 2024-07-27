package com.dwarfeng.familyhelper.plugin.settingrepo.service;

import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestImageNodeFileStream;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestImageNodeFileStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestImageNodeFileStreamUploadInfo;
import com.dwarfeng.familyhelper.plugin.settingrepo.handler.DubboRestImageNodeOperateHandler;
import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import org.springframework.stereotype.Service;

@Service
public class DubboRestImageNodeOperateServiceImpl implements DubboRestImageNodeOperateService {

    private final DubboRestImageNodeOperateHandler dubboRestImageNodeOperateHandler;

    private final ServiceExceptionMapper sem;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public DubboRestImageNodeOperateServiceImpl(
            DubboRestImageNodeOperateHandler dubboRestImageNodeOperateHandler,
            ServiceExceptionMapper sem
    ) {
        this.dubboRestImageNodeOperateHandler = dubboRestImageNodeOperateHandler;
        this.sem = sem;
    }

    @Override
    public DubboRestImageNodeFileStream downloadFileStream(DubboRestImageNodeFileStreamDownloadInfo downloadInfo)
            throws ServiceException {
        try {
            return dubboRestImageNodeOperateHandler.downloadFileStream(downloadInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("下载图片节点文件流时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public VoucherIdWrapper requestFileStreamVoucher(DubboRestImageNodeFileStreamDownloadInfo downloadInfo)
            throws ServiceException {
        try {
            return dubboRestImageNodeOperateHandler.requestFileStreamVoucher(downloadInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("请求下载图片节点文件流凭证时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public DubboRestImageNodeFileStream downloadFileStreamByVoucher(VoucherIdWrapper voucherIdWrapper)
            throws ServiceException {
        try {
            return dubboRestImageNodeOperateHandler.downloadFileStreamByVoucher(voucherIdWrapper);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("通过凭证下载图片节点文件流时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public void uploadFileStream(DubboRestImageNodeFileStreamUploadInfo uploadInfo) throws ServiceException {
        try {
            dubboRestImageNodeOperateHandler.uploadFileStream(uploadInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("上传图片节点文件流时发生异常", LogLevel.WARN, e, sem);
        }
    }
}
