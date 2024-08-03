package com.dwarfeng.familyhelper.plugin.settingrepo.service;

import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestImageListNodeFileStream;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestImageListNodeFileStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestImageListNodeFileStreamUpdateInfo;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestImageListNodeFileStreamUploadInfo;
import com.dwarfeng.familyhelper.plugin.settingrepo.handler.DubboRestImageListNodeOperateHandler;
import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import org.springframework.stereotype.Service;

@Service
public class DubboRestImageListNodeOperateServiceImpl implements DubboRestImageListNodeOperateService {

    private final DubboRestImageListNodeOperateHandler dubboRestImageListNodeOperateHandler;

    private final ServiceExceptionMapper sem;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public DubboRestImageListNodeOperateServiceImpl(
            DubboRestImageListNodeOperateHandler dubboRestImageListNodeOperateHandler,
            ServiceExceptionMapper sem
    ) {
        this.dubboRestImageListNodeOperateHandler = dubboRestImageListNodeOperateHandler;
        this.sem = sem;
    }

    @Override
    public DubboRestImageListNodeFileStream downloadFileStream(
            DubboRestImageListNodeFileStreamDownloadInfo downloadInfo
    ) throws ServiceException {
        try {
            return dubboRestImageListNodeOperateHandler.downloadFileStream(downloadInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("下载图片列表节点文件流时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public VoucherIdWrapper requestFileStreamVoucher(DubboRestImageListNodeFileStreamDownloadInfo downloadInfo)
            throws ServiceException {
        try {
            return dubboRestImageListNodeOperateHandler.requestFileStreamVoucher(downloadInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("请求下载图片列表节点文件流凭证时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public DubboRestImageListNodeFileStream downloadFileStreamByVoucher(VoucherIdWrapper voucherIdWrapper)
            throws ServiceException {
        try {
            return dubboRestImageListNodeOperateHandler.downloadFileStreamByVoucher(voucherIdWrapper);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("通过凭证下载图片列表节点文件流时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public void uploadFileStream(DubboRestImageListNodeFileStreamUploadInfo uploadInfo) throws ServiceException {
        try {
            dubboRestImageListNodeOperateHandler.uploadFileStream(uploadInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("上传图片列表节点文件流时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public void updateFileStream(DubboRestImageListNodeFileStreamUpdateInfo updateInfo) throws ServiceException {
        try {
            dubboRestImageListNodeOperateHandler.updateFileStream(updateInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("更新图片列表节点文件流时发生异常", LogLevel.WARN, e, sem);
        }
    }
}
