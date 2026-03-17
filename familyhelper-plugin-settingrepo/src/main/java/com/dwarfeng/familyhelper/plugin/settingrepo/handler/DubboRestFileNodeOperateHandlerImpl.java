package com.dwarfeng.familyhelper.plugin.settingrepo.handler;

import com.alibaba.fastjson.JSON;
import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestFileNodeFileStream;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestFileNodeFileStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestFileNodeFileStreamUploadInfo;
import com.dwarfeng.settingrepo.stack.bean.dto.FileNodeFileStream;
import com.dwarfeng.settingrepo.stack.bean.dto.FileNodeFileStreamDownloadInfo;
import com.dwarfeng.settingrepo.stack.bean.dto.FileNodeFileStreamUploadInfo;
import com.dwarfeng.settingrepo.stack.service.FileNodeOperateService;
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

/**
 * Dubbo rest 文件节点文件操作处理器实现。
 *
 * @author DwArFeng
 * @since 1.7.1
 */
@Component
public class DubboRestFileNodeOperateHandlerImpl implements DubboRestFileNodeOperateHandler {

    private static final String SPEL_VOUCHER_CATEGORY_KEY = "#{new com.dwarfeng.subgrade.stack.bean.key.StringIdKey(" +
            "'${voucher_category_id.file_node_file_stream_download}')}";

    private final FileNodeOperateService fileNodeOperateService;
    private final VoucherService voucherService;

    @Value(SPEL_VOUCHER_CATEGORY_KEY)
    private StringIdKey voucherCategoryKey;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public DubboRestFileNodeOperateHandlerImpl(
            FileNodeOperateService fileNodeOperateService,
            VoucherService voucherService
    ) {
        this.fileNodeOperateService = fileNodeOperateService;
        this.voucherService = voucherService;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public DubboRestFileNodeFileStream downloadFileStream(
            DubboRestFileNodeFileStreamDownloadInfo downloadInfo
    ) throws HandlerException {
        try {
            // 展开并构造参数。
            String category = downloadInfo.getCategory();
            String[] args = downloadInfo.getArgs();

            // 调用维护服务下载文件节点文件流。
            FileNodeFileStream fileNodeFileStream = fileNodeOperateService.downloadFileStream(
                    new FileNodeFileStreamDownloadInfo(category, args)
            );

            // 如果 fileNodeFileStream 为 null, 直接返回 null。
            if (fileNodeFileStream == null) {
                return null;
            }

            // 拼接 DubboRestFileNodeFileStream 并返回。
            return new DubboRestFileNodeFileStream(
                    fileNodeFileStream.getOriginName(), fileNodeFileStream.getLength(),
                    fileNodeFileStream.getContent()
            );
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    @Override
    public VoucherIdWrapper requestFileStreamVoucher(DubboRestFileNodeFileStreamDownloadInfo downloadInfo)
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
    public DubboRestFileNodeFileStream downloadFileStreamByVoucher(VoucherIdWrapper voucherIdWrapper)
            throws HandlerException {
        try {
            // 查看凭证。
            VoucherInspectResult voucherInspectResult = voucherService.inspect(
                    new VoucherInspectInfo(new LongIdKey(voucherIdWrapper.getVoucherId()))
            );
            String voucherContent = voucherInspectResult.getContent();

            // 解析凭证内容，获取用户键和文件键。
            DubboRestFileNodeFileStreamDownloadInfo downloadInfo = JSON.parseObject(
                    voucherContent, DubboRestFileNodeFileStreamDownloadInfo.class
            );

            // 展开并构造参数。
            String category = downloadInfo.getCategory();
            String[] args = downloadInfo.getArgs();

            // 调用维护服务下载文件节点文件流。
            FileNodeFileStream fileNodeFileStream = fileNodeOperateService.downloadFileStream(
                    new FileNodeFileStreamDownloadInfo(category, args)
            );

            // 如果 fileNodeFileStream 为 null, 直接返回 null。
            if (fileNodeFileStream == null) {
                return null;
            }

            // 拼接 DubboRestFileNodeFileStream 并返回。
            return new DubboRestFileNodeFileStream(
                    fileNodeFileStream.getOriginName(), fileNodeFileStream.getLength(),
                    fileNodeFileStream.getContent()
            );
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    @Override
    public void uploadFileStream(DubboRestFileNodeFileStreamUploadInfo uploadInfo) throws HandlerException {
        try {
            // 展开并构造参数。
            String category = uploadInfo.getCategory();
            String[] args = uploadInfo.getArgs();
            String originName = uploadInfo.getOriginName();
            long length = uploadInfo.getLength();
            InputStream content = uploadInfo.getContent();

            // 构造 FileNodeFileStreamUploadInfo。
            FileNodeFileStreamUploadInfo fileNodeFileStreamUploadInfo = new FileNodeFileStreamUploadInfo(
                    category, args, originName, length, content
            );

            // 调用维护服务上传文件节点文件流。
            fileNodeOperateService.uploadFileStream(fileNodeFileStreamUploadInfo);
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }
}
