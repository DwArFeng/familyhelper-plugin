package com.dwarfeng.familyhelper.plugin.settingrepo.service;

import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestFileListNodeFileStream;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestFileListNodeFileStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestFileListNodeFileStreamUpdateInfo;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestFileListNodeFileStreamUploadInfo;
import com.dwarfeng.familyhelper.plugin.settingrepo.handler.DubboRestFileListNodeOperateHandler;
import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import org.springframework.stereotype.Service;

/**
 * Dubbo rest 文件列表节点操作服务实现。
 *
 * @author DwArFeng
 * @since 1.7.1
 */
@Service
public class DubboRestFileListNodeOperateServiceImpl implements DubboRestFileListNodeOperateService {

    private final DubboRestFileListNodeOperateHandler dubboRestFileListNodeOperateHandler;

    private final ServiceExceptionMapper sem;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public DubboRestFileListNodeOperateServiceImpl(
            DubboRestFileListNodeOperateHandler dubboRestFileListNodeOperateHandler,
            ServiceExceptionMapper sem
    ) {
        this.dubboRestFileListNodeOperateHandler = dubboRestFileListNodeOperateHandler;
        this.sem = sem;
    }

    @Override
    public DubboRestFileListNodeFileStream downloadFileStream(
            DubboRestFileListNodeFileStreamDownloadInfo downloadInfo
    ) throws ServiceException {
        try {
            return dubboRestFileListNodeOperateHandler.downloadFileStream(downloadInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("下载文件列表节点文件流时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public VoucherIdWrapper requestFileStreamVoucher(DubboRestFileListNodeFileStreamDownloadInfo downloadInfo)
            throws ServiceException {
        try {
            return dubboRestFileListNodeOperateHandler.requestFileStreamVoucher(downloadInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("请求下载文件列表节点文件流凭证时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public DubboRestFileListNodeFileStream downloadFileStreamByVoucher(VoucherIdWrapper voucherIdWrapper)
            throws ServiceException {
        try {
            return dubboRestFileListNodeOperateHandler.downloadFileStreamByVoucher(voucherIdWrapper);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("通过凭证下载文件列表节点文件流时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public void uploadFileStream(DubboRestFileListNodeFileStreamUploadInfo uploadInfo) throws ServiceException {
        try {
            dubboRestFileListNodeOperateHandler.uploadFileStream(uploadInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("上传文件列表节点文件流时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public void updateFileStream(DubboRestFileListNodeFileStreamUpdateInfo updateInfo) throws ServiceException {
        try {
            dubboRestFileListNodeOperateHandler.updateFileStream(updateInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("更新文件列表节点文件流时发生异常", LogLevel.WARN, e, sem);
        }
    }
}
