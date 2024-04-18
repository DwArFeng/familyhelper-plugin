package com.dwarfeng.familyhelper.plugin.commons.dto;

import com.dwarfeng.subgrade.stack.bean.dto.Dto;
import org.jboss.resteasy.annotations.jaxrs.FormParam;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.core.MediaType;

/**
 * 凭证 ID 包装器。
 *
 * @author DwArFeng
 * @since 1.4.0
 */
public class VoucherIdWrapper implements Dto {

    private static final long serialVersionUID = -6390138555511578640L;

    @FormParam("voucher-id")
    @PartType(MediaType.TEXT_PLAIN)
    private Long voucherId;

    public VoucherIdWrapper() {
    }

    public VoucherIdWrapper(Long voucherId) {
        this.voucherId = voucherId;
    }

    public Long getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(Long voucherId) {
        this.voucherId = voucherId;
    }

    @Override
    public String toString() {
        return "VoucherIdWrapper{" +
                "voucherId=" + voucherId +
                '}';
    }
}
