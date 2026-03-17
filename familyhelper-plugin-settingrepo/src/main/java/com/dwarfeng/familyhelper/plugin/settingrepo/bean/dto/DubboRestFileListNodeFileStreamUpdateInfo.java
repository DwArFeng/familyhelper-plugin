package com.dwarfeng.familyhelper.plugin.settingrepo.bean.dto;

import com.dwarfeng.subgrade.stack.bean.dto.Dto;
import org.jboss.resteasy.annotations.jaxrs.FormParam;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Dubbo rest 文件列表节点文件流更新信息。
 *
 * @author DwArFeng
 * @since 1.7.1
 */
public class DubboRestFileListNodeFileStreamUpdateInfo implements Dto {

    private static final long serialVersionUID = 180332224333947003L;

    @FormParam("category")
    @PartType("text/plain;charset=utf-8")
    private String category;

    @FormParam("args")
    @PartType("application/json;charset=utf-8")
    private String[] args;

    @FormParam("index")
    @PartType(MediaType.TEXT_PLAIN)
    private int index;

    @FormParam("origin-name")
    @PartType("text/plain;charset=utf-8")
    private String originName;

    @FormParam("length")
    @PartType(MediaType.TEXT_PLAIN)
    private long length;

    @FormParam("content")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private InputStream content;

    public DubboRestFileListNodeFileStreamUpdateInfo() {
    }

    public DubboRestFileListNodeFileStreamUpdateInfo(
            String category, String[] args, int index, String originName, long length, InputStream content
    ) {
        this.category = category;
        this.args = args;
        this.index = index;
        this.originName = originName;
        this.length = length;
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public InputStream getContent() {
        return content;
    }

    public void setContent(InputStream content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "DubboRestFileListNodeFileStreamUpdateInfo{" +
                "category='" + category + '\'' +
                ", args=" + Arrays.toString(args) +
                ", index=" + index +
                ", originName='" + originName + '\'' +
                ", length=" + length +
                ", content=" + content +
                '}';
    }
}
