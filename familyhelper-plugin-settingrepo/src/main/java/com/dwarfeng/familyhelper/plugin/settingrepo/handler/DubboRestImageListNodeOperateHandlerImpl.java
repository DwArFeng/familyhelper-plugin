package com.dwarfeng.familyhelper.plugin.settingrepo.handler;

import com.alibaba.fastjson.JSON;
import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestImageListNodeFileStream;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestImageListNodeFileStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestImageListNodeFileStreamUpdateInfo;
import com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto.DubboRestImageListNodeFileStreamUploadInfo;
import com.dwarfeng.settingrepo.stack.bean.dto.ImageListNodeFileStream;
import com.dwarfeng.settingrepo.stack.bean.dto.ImageListNodeFileStreamDownloadInfo;
import com.dwarfeng.settingrepo.stack.bean.dto.ImageListNodeFileStreamUpdateInfo;
import com.dwarfeng.settingrepo.stack.bean.dto.ImageListNodeFileStreamUploadInfo;
import com.dwarfeng.settingrepo.stack.service.ImageListNodeOperateService;
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

@Component
public class DubboRestImageListNodeOperateHandlerImpl implements DubboRestImageListNodeOperateHandler {

    private static final String SPEL_VOUCHER_CATEGORY_KEY = "#{new com.dwarfeng.subgrade.stack.bean.key.StringIdKey(" +
            "'${voucher_category_id.image_list_node_file_stream_download}')}";

    private final ImageListNodeOperateService imageListNodeOperateService;
    private final VoucherService voucherService;

    @Value(SPEL_VOUCHER_CATEGORY_KEY)
    private StringIdKey voucherCategoryKey;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public DubboRestImageListNodeOperateHandlerImpl(
            ImageListNodeOperateService imageListNodeOperateService,
            VoucherService voucherService
    ) {
        this.imageListNodeOperateService = imageListNodeOperateService;
        this.voucherService = voucherService;
    }

    @Override
    public DubboRestImageListNodeFileStream downloadFileStream(
            DubboRestImageListNodeFileStreamDownloadInfo downloadInfo
    ) throws HandlerException {
        try {
            // 展开并构造参数。
            String category = downloadInfo.getCategory();
            String[] args = downloadInfo.getArgs();
            Integer index = downloadInfo.getIndex();

            // 调用维护服务下载图片列表节点文件流。
            ImageListNodeFileStream imageListNodeFileStream = imageListNodeOperateService.downloadFileStream(
                    new ImageListNodeFileStreamDownloadInfo(category, args, index)
            );

            // 如果 imageListNodeFileStream 为 null, 直接返回 null。
            if (imageListNodeFileStream == null) {
                return null;
            }

            // 拼接 DubboRestImageListNodeFileStream 并返回。
            return new DubboRestImageListNodeFileStream(
                    imageListNodeFileStream.getOriginName(), imageListNodeFileStream.getLength(),
                    imageListNodeFileStream.getContent()
            );
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    @Override
    public VoucherIdWrapper requestFileStreamVoucher(DubboRestImageListNodeFileStreamDownloadInfo downloadInfo)
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

    @Override
    public DubboRestImageListNodeFileStream downloadFileStreamByVoucher(VoucherIdWrapper voucherIdWrapper)
            throws HandlerException {
        try {
            // 查看凭证。
            VoucherInspectResult voucherInspectResult = voucherService.inspect(
                    new VoucherInspectInfo(new LongIdKey(voucherIdWrapper.getVoucherId()))
            );
            String voucherContent = voucherInspectResult.getContent();

            // 解析凭证内容，获取用户键和文件键。
            DubboRestImageListNodeFileStreamDownloadInfo downloadInfo = JSON.parseObject(
                    voucherContent, DubboRestImageListNodeFileStreamDownloadInfo.class
            );

            // 展开并构造参数。
            String category = downloadInfo.getCategory();
            String[] args = downloadInfo.getArgs();
            Integer index = downloadInfo.getIndex();

            // 调用维护服务下载图片列表节点文件流。
            ImageListNodeFileStream imageListNodeFileStream = imageListNodeOperateService.downloadFileStream(
                    new ImageListNodeFileStreamDownloadInfo(category, args, index)
            );

            // 如果 imageListNodeFileStream 为 null, 直接返回 null。
            if (imageListNodeFileStream == null) {
                return null;
            }

            // 拼接 DubboRestImageListNodeFileStream 并返回。
            return new DubboRestImageListNodeFileStream(
                    imageListNodeFileStream.getOriginName(), imageListNodeFileStream.getLength(),
                    imageListNodeFileStream.getContent()
            );
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    @Override
    public void uploadFileStream(DubboRestImageListNodeFileStreamUploadInfo uploadInfo) throws HandlerException {
        try {
            // 展开并构造参数。
            String category = uploadInfo.getCategory();
            String[] args = uploadInfo.getArgs();
            Integer index = uploadInfo.getIndex();
            String originName = uploadInfo.getOriginName();
            long length = uploadInfo.getLength();
            InputStream content = uploadInfo.getContent();

            // 构造 ImageListNodeFileStreamUploadInfo。
            ImageListNodeFileStreamUploadInfo imageListNodeFileStreamUploadInfo = new ImageListNodeFileStreamUploadInfo(
                    category, args, index, originName, length, content
            );

            // 调用维护服务上传图片列表节点文件流。
            imageListNodeOperateService.uploadFileStream(imageListNodeFileStreamUploadInfo);
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    @Override
    public void updateFileStream(DubboRestImageListNodeFileStreamUpdateInfo updateInfo) throws HandlerException {
        try {
            // 展开并构造参数。
            String category = updateInfo.getCategory();
            String[] args = updateInfo.getArgs();
            int index = updateInfo.getIndex();
            String originName = updateInfo.getOriginName();
            long length = updateInfo.getLength();
            InputStream content = updateInfo.getContent();

            // 构造 ImageListNodeFileStreamUpdateInfo。
            ImageListNodeFileStreamUpdateInfo imageListNodeFileStreamUpdateInfo = new ImageListNodeFileStreamUpdateInfo(
                    category, args, index, originName, length, content
            );

            // 调用维护服务更新图片列表节点文件流。
            imageListNodeOperateService.updateFileStream(imageListNodeFileStreamUpdateInfo);
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }
}
