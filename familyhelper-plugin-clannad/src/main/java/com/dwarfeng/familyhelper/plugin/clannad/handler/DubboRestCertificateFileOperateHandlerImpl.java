package com.dwarfeng.familyhelper.plugin.clannad.handler;

import com.dwarfeng.dutil.basic.io.IOUtil;
import com.dwarfeng.familyhelper.clannad.impl.handler.OperateHandlerValidator;
import com.dwarfeng.familyhelper.clannad.impl.util.FtpConstants;
import com.dwarfeng.familyhelper.clannad.sdk.util.Constants;
import com.dwarfeng.familyhelper.clannad.stack.bean.entity.CertificateFileInfo;
import com.dwarfeng.familyhelper.clannad.stack.service.CertificateFileInfoMaintainService;
import com.dwarfeng.familyhelper.plugin.clannad.bean.dto.DubboRestCertificateFileStream;
import com.dwarfeng.familyhelper.plugin.clannad.bean.dto.DubboRestCertificateFileStreamDownloadInfo;
import com.dwarfeng.familyhelper.plugin.clannad.bean.dto.DubboRestCertificateFileStreamUploadInfo;
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
import java.util.Date;

@Component
public class DubboRestCertificateFileOperateHandlerImpl implements DubboRestCertificateFileOperateHandler {

    private static final String SPEL_VOUCHER_CATEGORY_KEY = "#{new com.dwarfeng.subgrade.stack.bean.key.StringIdKey(" +
            "'${voucher_category_id.certificate_file_stream_download}')}";

    private final CertificateFileInfoMaintainService certificateFileInfoMaintainService;
    private final VoucherService voucherService;

    private final FtpHandler ftpHandler;

    private final KeyGenerator<LongIdKey> keyGenerator;

    private final OperateHandlerValidator operateHandlerValidator;

    @Value(SPEL_VOUCHER_CATEGORY_KEY)
    private StringIdKey voucherCategoryKey;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public DubboRestCertificateFileOperateHandlerImpl(
            CertificateFileInfoMaintainService certificateFileInfoMaintainService,
            VoucherService voucherService,
            FtpHandler ftpHandler,
            KeyGenerator<LongIdKey> keyGenerator,
            OperateHandlerValidator operateHandlerValidator
    ) {
        this.certificateFileInfoMaintainService = certificateFileInfoMaintainService;
        this.voucherService = voucherService;
        this.ftpHandler = ftpHandler;
        this.keyGenerator = keyGenerator;
        this.operateHandlerValidator = operateHandlerValidator;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public DubboRestCertificateFileStream downloadCertificateFileStream(
            DubboRestCertificateFileStreamDownloadInfo downloadInfo
    ) throws HandlerException {
        try {
            // 展开并构造参数。
            StringIdKey userKey = new StringIdKey(downloadInfo.getUserStringId());
            LongIdKey certificateFileKey = new LongIdKey(downloadInfo.getCertificateFileLongId());

            // 确认用户存在。
            operateHandlerValidator.makeSureUserExists(userKey);

            // 确认凭证文件存在。
            operateHandlerValidator.makeSureCertificateFileExists(certificateFileKey);

            // 获取凭证文件对应的凭证，并确认用户有权限操作凭证。
            CertificateFileInfo certificateFileInfo = certificateFileInfoMaintainService.get(certificateFileKey);
            operateHandlerValidator.makeSureUserInspectPermittedForCertificate(
                    userKey, certificateFileInfo.getCertificateKey()
            );

            // 获取凭证文件的内容。
            InputStream content = ftpHandler.openInputStream(
                    FtpConstants.PATH_CERTIFICATE_FILE, getFileName(certificateFileKey)
            );

            // 拼接 DubboRestCertificateFileStream 并返回。
            return new DubboRestCertificateFileStream(
                    certificateFileInfo.getOriginName(), certificateFileInfo.getLength(), content
            );
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public VoucherIdWrapper requestCertificateFileStreamVoucher(DubboRestCertificateFileStreamDownloadInfo downloadInfo)
            throws HandlerException {
        try {
            // 展开并构造参数。
            StringIdKey userKey = new StringIdKey(downloadInfo.getUserStringId());
            LongIdKey certificateFileKey = new LongIdKey(downloadInfo.getCertificateFileLongId());

            // 确认用户存在。
            operateHandlerValidator.makeSureUserExists(userKey);

            // 确认凭证文件存在。
            operateHandlerValidator.makeSureCertificateFileExists(certificateFileKey);

            // 获取凭证文件对应的凭证，并确认用户有权限操作凭证。
            CertificateFileInfo certificateFileInfo = certificateFileInfoMaintainService.get(certificateFileKey);
            operateHandlerValidator.makeSureUserInspectPermittedForCertificate(
                    userKey, certificateFileInfo.getCertificateKey()
            );

            // 生成凭证。
            String voucherContent = userKey.getStringId() + "@" + certificateFileKey.getLongId();
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
    public DubboRestCertificateFileStream downloadCertificateFileStreamByVoucher(VoucherIdWrapper voucherIdWrapper)
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
            LongIdKey certificateFileKey = new LongIdKey(Long.parseLong(split[1]));

            // 确认用户存在。
            operateHandlerValidator.makeSureUserExists(userKey);

            // 确认凭证文件存在。
            operateHandlerValidator.makeSureCertificateFileExists(certificateFileKey);

            // 获取凭证文件对应的凭证，并确认用户有权限操作凭证。
            CertificateFileInfo certificateFileInfo = certificateFileInfoMaintainService.get(certificateFileKey);
            operateHandlerValidator.makeSureUserInspectPermittedForCertificate(
                    userKey, certificateFileInfo.getCertificateKey()
            );

            // 获取凭证文件的内容。
            InputStream content = ftpHandler.openInputStream(
                    FtpConstants.PATH_CERTIFICATE_FILE, getFileName(certificateFileKey)
            );

            // 拼接 DubboRestCertificateFileStream 并返回。
            return new DubboRestCertificateFileStream(
                    certificateFileInfo.getOriginName(), certificateFileInfo.getLength(), content
            );
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    @Override
    public void uploadCertificateFileStream(DubboRestCertificateFileStreamUploadInfo uploadInfo)
            throws HandlerException {
        try {
            // 展开并构造参数。
            StringIdKey userKey = new StringIdKey(uploadInfo.getUserStringId());
            LongIdKey certificateKey = new LongIdKey(uploadInfo.getCertificateLongId());

            // 确认用户存在。
            operateHandlerValidator.makeSureUserExists(userKey);

            // 确认凭证文件所属的凭证存在。
            operateHandlerValidator.makeSureCertificateExists(certificateKey);

            // 确认用户有权限操作凭证。
            operateHandlerValidator.makeSureUserModifyPermittedForCertificate(userKey, certificateKey);

            // 分配主键。
            LongIdKey certificateFileKey = keyGenerator.generate();

            // 凭证文件内容并存储（覆盖）。
            InputStream cin = uploadInfo.getContent();
            try (OutputStream fout = ftpHandler.openOutputStream(
                    FtpConstants.PATH_CERTIFICATE_FILE, getFileName(certificateFileKey)
            )) {
                IOUtil.trans(cin, fout, Constants.IO_TRANS_BUFFER_SIZE);
            }

            // 根据 certificateFileStreamUploadInfo 构造 CertificateFileInfo，插入或更新。
            Date currentDate = new Date();
            // 映射属性。
            CertificateFileInfo certificateFileInfo = new CertificateFileInfo();
            certificateFileInfo.setKey(certificateFileKey);
            certificateFileInfo.setCertificateKey(certificateKey);
            certificateFileInfo.setOriginName(uploadInfo.getOriginName());
            certificateFileInfo.setLength(uploadInfo.getLength());
            certificateFileInfo.setUploadDate(currentDate);
            certificateFileInfo.setRemark("通过 familyhelper-clannad 服务上传/更新证件文件");
            certificateFileInfoMaintainService.insertOrUpdate(certificateFileInfo);
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    private String getFileName(LongIdKey certificateFileKey) {
        return Long.toString(certificateFileKey.getLongId());
    }
}
