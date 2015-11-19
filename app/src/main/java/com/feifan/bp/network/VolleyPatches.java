package com.feifan.bp.network;

/**
 * Volley补丁类
 * <pre>
 *     重构时发现Volley中尚不支持的类，也许会在新版本中支持，后续重构
 * </pre>
 * Created by xuchunlei on 15/10/26.
 */
public class VolleyPatches {
    /** Callback interface for delivering the progress of the responses. */
    public interface ProgressListener {
        /**
         * Callback method thats called on each byte transfer.
         */
        void onProgress(long transferredBytes, long totalSize);
    }
}
