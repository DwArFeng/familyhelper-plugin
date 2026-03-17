package com.dwarfeng.familyhelper.plugin.settingrepo.handler;

import com.alibaba.fastjson.JSON;
import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestFileListNodeFileStream;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestFileListNodeFileStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestFileListNodeFileStreamUpdateInfo;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestFileListNodeFileStreamUploadInfo;
import com.dwarfeng.settingrepo.stack.bean.dto.FileListNodeFileStream;
import com.dwarfeng.settingrepo.stack.bean.dto.FileListNodeFileStreamDownloadInfo;
import com.dwarfeng.settingrepo.stack.bean.dto.FileListNodeFileStreamUpdateInfo;
import com.dwarfeng.settingrepo.stack.bean.dto.FileListNodeFileStreamUploadInfo;
import com.dwarfeng.settingrepo.stack.service.FileListNodeOperateService;
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

import java.io.InputStream;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Component
public class DubboRestFileListNodeOperateHandlerImpl implements DubboRestFileListNodeOperateHandler {

    private static final String SPEL_VOUCHER_CATEGORY_KEY = "#{new com.dwarfeng.subgrade.stack.bean.key.StringIdKey(" +
            "'${voucher_category_id.file_list_node_file_stream_download}')}";

    private final FileListNodeOperateService fileListNodeOperateService;
    private final VoucherService voucherService;

    @Value(SPEL_VOUCHER_CATEGORY_KEY)
    private StringIdKey voucherCategoryKey;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public DubboRestFileListNodeOperateHandlerImpl(
            FileListNodeOperateService fileListNodeOperateService,
            VoucherService voucherService
    ) {
        this.fileListNodeOperateService = fileListNodeOperateService;
        this.voucherService = voucherService;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public DubboRestFileListNodeFileStream downloadFileStream(
            DubboRestFileListNodeFileStreamDownloadInfo downloadInfo
    ) throws HandlerException {
        try {
            // 展开并构造参数。
            String category = downloadInfo.getCategory();
            String[] args = downloadInfo.getArgs();
            Integer index = downloadInfo.getIndex();

            // 调用维护服务下载文件列表节点文件流。
            FileListNodeFileStream fileListNodeFileStream = fileListNodeOperateService.downloadFileStream(
                    new FileListNodeFileStreamDownloadInfo(category, args, index)
            );

            // 如果 fileListNodeFileStream 为 null, 直接返回 null。
            if (fileListNodeFileStream == null) {
                return null;
            }

            // 拼接 DubboRestFileListNodeFileStream 并返回。
            return new DubboRestFileListNodeFileStream(
                    fileListNodeFileStream.getOriginName(), fileListNodeFileStream.getLength(),
                    fileListNodeFileStream.getContent()
            );
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    @Override
    public VoucherIdWrapper requestFileStreamVoucher(DubboRestFileListNodeFileStreamDownloadInfo downloadInfo)
            throws HandlerException {
        try {
            // 生成凭证。
            String voucherContent = JSON.toJSONString(downloadInfo);
            LongIdKey voucher = voucherService.create(
                    new VoucherCreateInfo(voucherCategoryKey, null, voucherContent, "我是备注")
            );

            // 返回凭证 ID。
            return new VoucherIdWrapper(voucher.getLongId());
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public DubboRestFileListNodeFileStream downloadFileStreamByVoucher(VoucherIdWrapper voucherIdWrapper)
            throws HandlerException {
        try {
            // 查看凭证。
            VoucherInspectResult voucherInspectResult = voucherService.inspect(
                    new VoucherInspectInfo(new LongIdKey(voucherIdWrapper.getVoucherId()))
            );
            String voucherContent = voucherInspectResult.getContent();

            // 解析凭证内容，获取用户键和文件键。
            DubboRestFileListNodeFileStreamDownloadInfo downloadInfo = JSON.parseObject(
                    voucherContent, DubboRestFileListNodeFileStreamDownloadInfo.class
            );

            // 展开并构造参数。
            String category = downloadInfo.getCategory();
            String[] args = downloadInfo.getArgs();
            Integer index = downloadInfo.getIndex();

            // 调用维护服务下载文件列表节点文件流。
            FileListNodeFileStream fileListNodeFileStream = fileListNodeOperateService.downloadFileStream(
                    new FileListNodeFileStreamDownloadInfo(category, args, index)
            );

            // 如果 fileListNodeFileStream 为 null, 直接返回 null。
            if (fileListNodeFileStream == null) {
                return null;
            }

            // 拼接 DubboRestFileListNodeFileStream 并返回。
            return new DubboRestFileListNodeFileStream(
                    fileListNodeFileStream.getOriginName(), fileListNodeFileStream.getLength(),
                    fileListNodeFileStream.getContent()
            );
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    @Override
    public void uploadFileStream(DubboRestFileListNodeFileStreamUploadInfo uploadInfo) throws HandlerException {
        try {
            // 展开并构造参数。
            String category = uploadInfo.getCategory();
            String[] args = uploadInfo.getArgs();
            Integer index = uploadInfo.getIndex();
            String originName = uploadInfo.getOriginName();
            long length = uploadInfo.getLength();
            InputStream content = uploadInfo.getContent();

            // 构造 FileListNodeFileStreamUploadInfo。
            FileListNodeFileStreamUploadInfo fileListNodeFileStreamUploadInfo = new FileListNodeFileStreamUploadInfo(
                    category, args, index, originName, length, content
            );

            // 调用维护服务上传文件列表节点文件流。
            fileListNodeOperateService.uploadFileStream(fileListNodeFileStreamUploadInfo);
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    @Override
    public void updateFileStream(DubboRestFileListNodeFileStreamUpdateInfo updateInfo) throws HandlerException {
        try {
            // 展开并构造参数。
            String category = updateInfo.getCategory();
            String[] args = updateInfo.getArgs();
            int index = updateInfo.getIndex();
            String originName = updateInfo.getOriginName();
            long length = updateInfo.getLength();
            InputStream content = updateInfo.getContent();

            // 构造 FileListNodeFileStreamUpdateInfo。
            FileListNodeFileStreamUpdateInfo fileListNodeFileStreamUpdateInfo = new FileListNodeFileStreamUpdateInfo(
                    category, args, index, originName, length, content
            );

            // 调用维护服务更新文件列表节点文件流。
            fileListNodeOperateService.updateFileStream(fileListNodeFileStreamUpdateInfo);
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }
}
