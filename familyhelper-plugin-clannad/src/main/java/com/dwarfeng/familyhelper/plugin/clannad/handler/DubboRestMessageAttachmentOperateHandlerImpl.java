package com.dwarfeng.familyhelper.plugin.clannad.handler;

import com.dwarfeng.dutil.basic.io.IOUtil;
import com.dwarfeng.familyhelper.clannad.impl.handler.OperateHandlerValidator;
import com.dwarfeng.familyhelper.clannad.impl.util.FtpConstants;
import com.dwarfeng.familyhelper.clannad.sdk.util.Constants;
import com.dwarfeng.familyhelper.clannad.stack.bean.entity.Message;
import com.dwarfeng.familyhelper.clannad.stack.bean.entity.MessageAttachmentInfo;
import com.dwarfeng.familyhelper.clannad.stack.service.MessageAttachmentInfoMaintainService;
import com.dwarfeng.familyhelper.clannad.stack.service.MessageMaintainService;
import com.dwarfeng.familyhelper.plugin.clannad.bean.dto.DubboRestMessageAttachmentStream;
import com.dwarfeng.familyhelper.plugin.clannad.bean.dto.DubboRestMessageAttachmentStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.clannad.bean.dto.DubboRestMessageAttachmentStreamUpdateInfo;
import com.dwarfeng.familyhelper.plugin.clannad.bean.dto.DubboRestMessageAttachmentStreamUploadInfo;
import com.dwarfeng.familyhelper.plugin.commons.dto.VoucherIdWrapper;
import com.dwarfeng.ftp.handler.FtpHandler;
import com.dwarfeng.subgrade.sdk.exception.HandlerExceptionHelper;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import com.dwarfeng.subgrade.stack.exception.HandlerException;
import com.dwarfeng.subgrade.stack.generation.KeyGenerator;
import com.dwarfeng.voucher.stack.bean.dto.VoucherCreateInfo;
import com.dwarfeng.voucher.stack.bean.dto.VoucherInspectInfo;
import com.dwarfeng.voucher.stack.bean.dto.VoucherInspectResult;
import com.dwarfeng.voucher.stack.service.VoucherService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

@Component
public class DubboRestMessageAttachmentOperateHandlerImpl implements DubboRestMessageAttachmentOperateHandler {

    private static final String SPEL_VOUCHER_CATEGORY_KEY = "#{new com.dwarfeng.subgrade.stack.bean.key.StringIdKey(" +
            "'${voucher_category_id.certificate_file_stream_download}')}";

    private static final Set<Integer> VALID_MESSAGE_STATUS_SET_DOWNLOAD;
    private static final Set<Integer> VALID_MESSAGE_STATUS_SET_UPLOAD;
    private static final Set<Integer> VALID_MESSAGE_STATUS_SET_UPDATE;

    static {
        Set<Integer> VALID_MESSAGE_STATUS_SET_DOWNLOAD_DEJA_VU = new HashSet<>();
        VALID_MESSAGE_STATUS_SET_DOWNLOAD_DEJA_VU.add(Constants.MESSAGE_STATUS_EDITING);
        VALID_MESSAGE_STATUS_SET_DOWNLOAD_DEJA_VU.add(Constants.MESSAGE_STATUS_SENT);
        VALID_MESSAGE_STATUS_SET_DOWNLOAD_DEJA_VU.add(Constants.MESSAGE_STATUS_RECEIVED);
        VALID_MESSAGE_STATUS_SET_DOWNLOAD = Collections.unmodifiableSet(VALID_MESSAGE_STATUS_SET_DOWNLOAD_DEJA_VU);

        Set<Integer> VALID_MESSAGE_STATUS_SET_UPLOAD_DEJA_VU = new HashSet<>();
        VALID_MESSAGE_STATUS_SET_UPLOAD_DEJA_VU.add(Constants.MESSAGE_STATUS_EDITING);
        VALID_MESSAGE_STATUS_SET_UPLOAD = Collections.unmodifiableSet(VALID_MESSAGE_STATUS_SET_UPLOAD_DEJA_VU);

        Set<Integer> VALID_MESSAGE_STATUS_SET_UPDATE_DEJA_VU = new HashSet<>();
        VALID_MESSAGE_STATUS_SET_UPDATE_DEJA_VU.add(Constants.MESSAGE_STATUS_EDITING);
        VALID_MESSAGE_STATUS_SET_UPDATE = Collections.unmodifiableSet(VALID_MESSAGE_STATUS_SET_UPDATE_DEJA_VU);
    }

    private final MessageAttachmentInfoMaintainService messageAttachmentInfoMaintainService;
    private final MessageMaintainService messageMaintainService;
    private final VoucherService voucherService;

    private final FtpHandler ftpHandler;

    private final KeyGenerator<LongIdKey> keyGenerator;

    private final OperateHandlerValidator operateHandlerValidator;

    @Value(SPEL_VOUCHER_CATEGORY_KEY)
    private StringIdKey voucherCategoryKey;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public DubboRestMessageAttachmentOperateHandlerImpl(
            MessageAttachmentInfoMaintainService messageAttachmentInfoMaintainService,
            MessageMaintainService messageMaintainService,
            VoucherService voucherService,
            FtpHandler ftpHandler,
            KeyGenerator<LongIdKey> keyGenerator,
            OperateHandlerValidator operateHandlerValidator
    ) {
        this.messageAttachmentInfoMaintainService = messageAttachmentInfoMaintainService;
        this.messageMaintainService = messageMaintainService;
        this.voucherService = voucherService;
        this.ftpHandler = ftpHandler;
        this.keyGenerator = keyGenerator;
        this.operateHandlerValidator = operateHandlerValidator;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public DubboRestMessageAttachmentStream downloadMessageAttachmentStream(
            DubboRestMessageAttachmentStreamDownloadInfo downloadInfo
    ) throws HandlerException {
        try {
            // 展开并构造参数。
            StringIdKey userKey = new StringIdKey(downloadInfo.getUserStringId());
            LongIdKey messageAttachmentKey = new LongIdKey(downloadInfo.getMessageAttachmentLongId());

            // 确认用户存在。
            operateHandlerValidator.makeSureUserExists(userKey);

            // 确认留言附件存在。
            operateHandlerValidator.makeSureMessageAttachmentExists(messageAttachmentKey);
            // 确认留言存在。
            MessageAttachmentInfo messageAttachmentInfo = messageAttachmentInfoMaintainService.get(
                    messageAttachmentKey
            );
            LongIdKey messageKey = messageAttachmentInfo.getMessageKey();
            operateHandlerValidator.makeSureMessageExists(messageKey);
            // 确认操作用户是消息发送人或消息接收人。
            Message message = messageMaintainService.get(messageKey);
            StringIdKey sendUserKey = message.getSendUserKey();
            StringIdKey receiveUserKey = message.getReceiveUserKey();
            Set<StringIdKey> validUserKeys = new HashSet<>();
            Optional.ofNullable(sendUserKey).ifPresent(validUserKeys::add);
            Optional.ofNullable(receiveUserKey).ifPresent(validUserKeys::add);
            operateHandlerValidator.makeSureUserMatch(userKey, validUserKeys);
            // 确认消息状态合法。
            operateHandlerValidator.makeSureMessageStatusMatch(messageKey, VALID_MESSAGE_STATUS_SET_DOWNLOAD);

            // 获取留言附件的内容。
            InputStream content = ftpHandler.openInputStream(
                    FtpConstants.PATH_MESSAGE_ATTACHMENT, getFileName(messageAttachmentKey)
            );

            // 拼接 DubboRestMessageAttachmentStream 并返回。
            return new DubboRestMessageAttachmentStream(
                    messageAttachmentInfo.getOriginName(), messageAttachmentInfo.getLength(), content
            );
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public VoucherIdWrapper requestMessageAttachmentStreamVoucher(
            DubboRestMessageAttachmentStreamDownloadInfo downloadInfo
    ) throws HandlerException {
        try {
            // 展开并构造参数。
            StringIdKey userKey = new StringIdKey(downloadInfo.getUserStringId());
            LongIdKey messageAttachmentKey = new LongIdKey(downloadInfo.getMessageAttachmentLongId());

            // 确认用户存在。
            operateHandlerValidator.makeSureUserExists(userKey);

            // 确认留言附件存在。
            operateHandlerValidator.makeSureMessageAttachmentExists(messageAttachmentKey);
            // 确认留言存在。
            MessageAttachmentInfo messageAttachmentInfo = messageAttachmentInfoMaintainService.get(
                    messageAttachmentKey
            );
            LongIdKey messageKey = messageAttachmentInfo.getMessageKey();
            operateHandlerValidator.makeSureMessageExists(messageKey);
            // 确认操作用户是消息发送人或消息接收人。
            Message message = messageMaintainService.get(messageKey);
            StringIdKey sendUserKey = message.getSendUserKey();
            StringIdKey receiveUserKey = message.getReceiveUserKey();
            Set<StringIdKey> validUserKeys = new HashSet<>();
            Optional.ofNullable(sendUserKey).ifPresent(validUserKeys::add);
            Optional.ofNullable(receiveUserKey).ifPresent(validUserKeys::add);
            operateHandlerValidator.makeSureUserMatch(userKey, validUserKeys);
            // 确认消息状态合法。
            operateHandlerValidator.makeSureMessageStatusMatch(messageKey, VALID_MESSAGE_STATUS_SET_DOWNLOAD);

            // 生成凭证。
            String voucherContent = userKey.getStringId() + "@" + messageAttachmentKey.getLongId();
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
    public DubboRestMessageAttachmentStream downloadMessageAttachmentStreamByVoucher(VoucherIdWrapper voucherIdWrapper)
            throws HandlerException {
        try {
            // 查看凭证。
            VoucherInspectResult voucherInspectResult = voucherService.inspect(
                    new VoucherInspectInfo(new LongIdKey(voucherIdWrapper.getVoucherId()))
            );
            String voucherContent = voucherInspectResult.getContent();

            // 解析凭证内容，获取用户键和文件键。
            String[] split = voucherContent.split("@");
            StringIdKey userKey = new StringIdKey(split[0]);
            LongIdKey messageAttachmentKey = new LongIdKey(Long.parseLong(split[1]));

            // 确认用户存在。
            operateHandlerValidator.makeSureUserExists(userKey);

            // 确认留言附件存在。
            operateHandlerValidator.makeSureMessageAttachmentExists(messageAttachmentKey);
            // 确认留言存在。
            MessageAttachmentInfo messageAttachmentInfo = messageAttachmentInfoMaintainService.get(
                    messageAttachmentKey
            );
            LongIdKey messageKey = messageAttachmentInfo.getMessageKey();
            operateHandlerValidator.makeSureMessageExists(messageKey);
            // 确认操作用户是消息发送人或消息接收人。
            Message message = messageMaintainService.get(messageKey);
            StringIdKey sendUserKey = message.getSendUserKey();
            StringIdKey receiveUserKey = message.getReceiveUserKey();
            Set<StringIdKey> validUserKeys = new HashSet<>();
            Optional.ofNullable(sendUserKey).ifPresent(validUserKeys::add);
            Optional.ofNullable(receiveUserKey).ifPresent(validUserKeys::add);
            operateHandlerValidator.makeSureUserMatch(userKey, validUserKeys);
            // 确认消息状态合法。
            operateHandlerValidator.makeSureMessageStatusMatch(messageKey, VALID_MESSAGE_STATUS_SET_DOWNLOAD);

            // 获取留言附件的内容。
            InputStream content = ftpHandler.openInputStream(
                    FtpConstants.PATH_MESSAGE_ATTACHMENT, getFileName(messageAttachmentKey)
            );

            // 拼接 DubboRestMessageAttachmentStream 并返回。
            return new DubboRestMessageAttachmentStream(
                    messageAttachmentInfo.getOriginName(), messageAttachmentInfo.getLength(), content
            );
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void uploadMessageAttachmentStream(DubboRestMessageAttachmentStreamUploadInfo uploadInfo)
            throws HandlerException {
        try {
            // 展开并构造参数。
            StringIdKey userKey = new StringIdKey(uploadInfo.getUserStringId());
            LongIdKey messageKey = new LongIdKey(uploadInfo.getMessageLongId());

            // 确认用户存在。
            operateHandlerValidator.makeSureUserExists(userKey);

            // 确认留言附件所属的留言存在。
            operateHandlerValidator.makeSureMessageExists(messageKey);
            // 确认操作用户是消息发送人。
            Message message = messageMaintainService.get(messageKey);
            StringIdKey sendUserKey = message.getSendUserKey();
            Set<StringIdKey> validUserKeys = new HashSet<>();
            Optional.ofNullable(sendUserKey).ifPresent(validUserKeys::add);
            operateHandlerValidator.makeSureUserMatch(userKey, validUserKeys);
            // 确认消息状态合法。
            operateHandlerValidator.makeSureMessageStatusMatch(messageKey, VALID_MESSAGE_STATUS_SET_UPLOAD);

            // 分配主键。
            LongIdKey messageAttachmentKey = keyGenerator.generate();

            // 项目文件内容并存储（覆盖）。
            InputStream cin = uploadInfo.getContent();
            try (OutputStream fout = ftpHandler.openOutputStream(
                    FtpConstants.PATH_MESSAGE_ATTACHMENT, getFileName(messageAttachmentKey)
            )) {
                IOUtil.trans(cin, fout, Constants.IO_TRANS_BUFFER_SIZE);
            }

            // 映射属性。
            Date currentDate = new Date();
            MessageAttachmentInfo messageAttachmentInfo = new MessageAttachmentInfo();
            messageAttachmentInfo.setKey(messageAttachmentKey);
            messageAttachmentInfo.setMessageKey(messageKey);
            messageAttachmentInfo.setOriginName(uploadInfo.getOriginName());
            messageAttachmentInfo.setLength(uploadInfo.getLength());
            messageAttachmentInfo.setUploadDate(currentDate);
            messageAttachmentInfo.setRemark("通过 familyhelper-clannad 服务上传/更新留言附件");

            // 调用维护服务插入留言附件信息。
            messageAttachmentInfoMaintainService.insert(messageAttachmentInfo);

            // 更新留言的属性。
            message.setAttachmentCount(message.getAttachmentCount() + 1);

            // 调用维护服务更新留言。
            messageMaintainService.update(message);
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void updateMessageAttachmentStream(DubboRestMessageAttachmentStreamUpdateInfo updateInfo)
            throws HandlerException {
        try {
            // 展开并构造参数。
            StringIdKey userKey = new StringIdKey(updateInfo.getUserStringId());
            LongIdKey messageAttachmentKey = new LongIdKey(updateInfo.getMessageAttachmentLongId());

            // 确认用户存在。
            operateHandlerValidator.makeSureUserExists(userKey);

            // 确认留言附件信息存在。
            operateHandlerValidator.makeSureMessageAttachmentExists(messageAttachmentKey);
            // 确认留言存在。
            MessageAttachmentInfo messageAttachmentInfo = messageAttachmentInfoMaintainService.get(
                    messageAttachmentKey
            );
            LongIdKey messageKey = messageAttachmentInfo.getMessageKey();
            operateHandlerValidator.makeSureMessageExists(messageKey);
            // 确认操作用户是消息发送人。
            Message message = messageMaintainService.get(messageKey);
            StringIdKey sendUserKey = message.getSendUserKey();
            Set<StringIdKey> validUserKeys = new HashSet<>();
            Optional.ofNullable(sendUserKey).ifPresent(validUserKeys::add);
            operateHandlerValidator.makeSureUserMatch(userKey, validUserKeys);
            // 确认消息状态合法。
            operateHandlerValidator.makeSureMessageStatusMatch(messageKey, VALID_MESSAGE_STATUS_SET_UPDATE);

            // 项目文件内容并存储（覆盖）。
            InputStream cin = updateInfo.getContent();
            try (OutputStream fout = ftpHandler.openOutputStream(
                    FtpConstants.PATH_MESSAGE_ATTACHMENT, getFileName(messageAttachmentKey)
            )) {
                IOUtil.trans(cin, fout, Constants.IO_TRANS_BUFFER_SIZE);
            }

            // 更新留言附件信息。
            Date currentDate = new Date();
            messageAttachmentInfo.setOriginName(updateInfo.getOriginName());
            messageAttachmentInfo.setLength(updateInfo.getLength());
            messageAttachmentInfo.setUploadDate(currentDate);
            messageAttachmentInfo.setRemark("通过 familyhelper-clannad 服务上传/更新留言附件");

            // 调用维护服务更新留言附件信息。
            messageAttachmentInfoMaintainService.update(messageAttachmentInfo);
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    private String getFileName(LongIdKey messageAttachmentKey) {
        return Long.toString(messageAttachmentKey.getLongId());
    }
}
