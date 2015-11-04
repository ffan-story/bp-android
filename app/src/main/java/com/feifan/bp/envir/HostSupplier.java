package com.feifan.bp.envir;

/**
 * Created by xuchunlei on 15/10/14.
 */
public class HostSupplier {

    /**
     * 主机域名工厂接口
     * Created by xuchunlei on 15/10/14.
     */
    public interface IHostFactory {

        /**
         * 获取API主机域名
         * @return
         */
        String getFFanApiHost();

        /**
         * 获取H5主机域名
         * @return
         */
        String getFFanH5Host();

        /**
         * 获取图片服务器域名
         * @return
         */
        String getFFanPicHost();

        /**
         * 获取V1版的API主机域名
         * @return
         */
        String getFFanApiV1Host();
    }

    /**
     * SIT环境主机域名工厂
     */
    static class SitHostFactory implements IHostFactory {

        @Override
        public String getFFanApiHost() {
            return "http://api.sit.ffan.com/";
        }

        @Override
        public String getFFanH5Host() {
            return "http://sop.sit.ffan.com/";
//            return "http://10.1.171.103:1111/";
//            return "http://10.1.171.60:81/";
        }

        @Override
        public String getFFanPicHost() {
            return "http://api.sit.ffan.com/ffan/v1/";
        }

        @Override
        public String getFFanApiV1Host() {
            return "http://api.sit.ffan.com/v1/";
        }
    }

    /**
     * 预生产环境主机域名工厂
     */
    static class ProductPreFactory implements IHostFactory {

        @Override
        public String getFFanApiHost() {
            return "https://api.ffan.com/";
        }

        @Override
        public String getFFanH5Host() {
            return "http://sop.pre.ffan.com/";
        }

        @Override
        public String getFFanPicHost() {
            return "https://api.pre.ffan.com/ffan/v1/";
        }

        @Override
        public String getFFanApiV1Host() {
            return "https://api.ffan.com/";
        }
    }

    /**
     * 生产环境主机域名工厂
     */
    static class ProductFactory implements IHostFactory {

        @Override
        public String getFFanApiHost() {
            return "https://api.ffan.com/";
        }

        @Override
        public String getFFanH5Host() {
            return "https://sop.ffan.com/";
        }

        @Override
        public String getFFanPicHost() {
            return "https://api.ffan.com/ffan/v1/";
        }

        @Override
        public String getFFanApiV1Host() {
            return "https://api.ffan.com/";
        }
    }
}
