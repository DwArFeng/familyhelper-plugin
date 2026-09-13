package com.dwarfeng.familyhelper.plugin.fileio.handler;

import com.alibaba.fastjson.JSON;
import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestImportFileStream;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestImportFileStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.fileio.bean.dto.DubboRestImportFileStreamUploadInfo;
import com.dwarfeng.fileio.stack.bean.dto.FileStream;
import com.dwarfeng.fileio.stack.bean.dto.FileStreamDownloadInfo;
import com.dwarfeng.fileio.stack.bean.dto.FileStreamUploadInfo;
import com.dwarfeng.fileio.stack.bean.key.TaskItemKey;
import com.dwarfeng.fileio.stack.service.ImportService;
import com.dwarfeng.subgrade.sdk.exception.HandlerExceptionHelper;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import com.dwarfeng.subgrade.stack.exception.HandlerException;
import com.dwarfeng.voucher.stack.bean.dto.VoucherCreateInfo;
import com.dwarfeng.voucher.stack.bean.dto.VoucherInspectInfo;
import com.dwarfeng.voucher.stack.bean.dto.VoucherInspectResult;
import com.dwarfeng.voucher.stack.service.VoucherService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DubboRestImportFileOperateHandlerImpl implements DubboRestImportFileOperateHandler {

    private static final String SPEL_VOUCHER_CATEGORY_KEY = "#{new com.dwarfeng.subgrade.stack.bean.key.StringIdKey(" +
            "'${voucher_category_id.import_file_stream_download}')}";

    private final ImportService importService;
    private final VoucherService voucherService;

    @Value(SPEL_VOUCHER_CATEGORY_KEY)
    private StringIdKey voucherCategoryKey;

    public DubboRestImportFileOperateHandlerImpl(
            ImportService importService,
            VoucherService voucherService
    ) {
        this.importService = importService;
        this.voucherService = voucherService;
    }

    @Override
    public DubboRestImportFileStream downloadFileStream(DubboRestImportFileStreamDownloadInfo downloadInfo)
            throws HandlerException {
        try {
            FileStream fileStream = importService.downloadFileStream(
                    new FileStreamDownloadInfo(resolveTaskItemKey(downloadInfo))
            );

            if (fileStream == null) {
                return null;
            }

            return new DubboRestImportFileStream(
                    fileStream.getOriginName(), fileStream.getLength(), fileStream.getContent()
            );
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    @Override
    public VoucherIdWrapper requestFileStreamVoucher(DubboRestImportFileStreamDownloadInfo downloadInfo)
            throws HandlerException {
        try {
            String voucherContent = JSON.toJSONString(downloadInfo);
            LongIdKey voucher = voucherService.create(
                    new VoucherCreateInfo(voucherCategoryKey, null, voucherContent, "fileio 导入文件流下载凭证")
            );
            return new VoucherIdWrapper(voucher.getLongId());
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    @Override
    public DubboRestImportFileStream downloadFileStreamByVoucher(VoucherIdWrapper voucherIdWrapper)
            throws HandlerException {
        try {
            VoucherInspectResult voucherInspectResult = voucherService.inspect(
                    new VoucherInspectInfo(new LongIdKey(voucherIdWrapper.getVoucherId()))
            );
            String voucherContent = voucherInspectResult.getContent();

            DubboRestImportFileStreamDownloadInfo downloadInfo = JSON.parseObject(
                    voucherContent, DubboRestImportFileStreamDownloadInfo.class
            );

            FileStream fileStream = importService.downloadFileStream(
                    new FileStreamDownloadInfo(resolveTaskItemKey(downloadInfo))
            );

            if (fileStream == null) {
                return null;
            }

            return new DubboRestImportFileStream(
                    fileStream.getOriginName(), fileStream.getLength(), fileStream.getContent()
            );
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    @Override
    public void uploadFileStream(DubboRestImportFileStreamUploadInfo uploadInfo) throws HandlerException {
        try {
            importService.uploadFileStream(
                    new FileStreamUploadInfo(
                            new TaskItemKey(uploadInfo.getTaskId(), uploadInfo.getIdentifier()),
                            uploadInfo.getOriginName(),
                            uploadInfo.getLength(),
                            uploadInfo.getContent()
                    )
            );
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    private TaskItemKey resolveTaskItemKey(DubboRestImportFileStreamDownloadInfo downloadInfo) {
        return new TaskItemKey(downloadInfo.getTaskId(), downloadInfo.getIdentifier());
    }
}
