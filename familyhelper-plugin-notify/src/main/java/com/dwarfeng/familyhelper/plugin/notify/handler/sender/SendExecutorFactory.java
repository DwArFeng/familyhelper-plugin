package com.dwarfeng.familyhelper.plugin.notify.handler.sender;

import com.dwarfeng.notify.stack.exception.SenderExecutionException;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;

import java.util.List;

/**
 * 发送执行器工厂。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public interface SendExecutorFactory {

    /**
     * 提供类型。
     *
     * @return 类型。
     */
    String provideType();

    /**
     * 提供标签。
     *
     * @return 标签。
     */
    String provideLabel();

    /**
     * 提供描述。
     *
     * @return 描述。
     */
    String provideDescription();

    /**
     * 提供示例参数。
     *
     * @return 示例参数。
     */
    String provideExampleParam();

    /**
     * 根据指定的参数创建执行器。
     *
     * @param param 指定的参数。
     * @return 创建的执行器。
     * @throws SenderExecutionException 发送器执行异常。
     */
    Executor createExecutor(String param) throws Exception;

    interface Executor {

        /**
         * 向指定的用户发送信息。
         *
         * @param userKey 指定的用户主键。
         * @param context 上下文。
         * @throws Exception 发送器执行过程中出现的任何异常。
         */
        void send(StringIdKey userKey, Object context) throws Exception;

        /**
         * 向一批用户发送信息。
         *
         * @param userKeys 指定的用户组成的列表。
         * @param context  上下文。
         * @throws Exception 发送器执行过程中出现的任何异常。
         */
        default void batchSend(List<StringIdKey> userKeys, Object context) throws Exception {
            for (StringIdKey userKey : userKeys) {
                send(userKey, context);
            }
        }
    }
}
