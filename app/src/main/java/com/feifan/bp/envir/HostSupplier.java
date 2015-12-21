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
         * API版本
         */
        String VERSION = "v1";

        /**
         * 获取xadmin的API地址前缀
         * @return
         */
        String getXadminApiPrefix();

        /**
         * 获取ffan的API地址前缀
         * @return
         */
        String getFFanApiPrefix();

        /**
         * 获取mapp的API地址前缀
         * @return
         */
        String getMAppApiPrefix();

        /**
         * 获取H5主机域名
         * @return
         */
        String getFFanH5Host();

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
        public String getXadminApiPrefix() {
            return "http://api.sit.ffan.com/xadmin/";
        }

        @Override
        public String getFFanApiPrefix() {
            return "http://api.sit.ffan.com/ffan/".concat(VERSION).concat("/");
        }

        @Override
        public String getMAppApiPrefix() {
            return "http://api.sit.ffan.com/mapp/".concat(VERSION).concat("/");
        }


        @Override
        public String getFFanH5Host() {
            return "http://sop.sit.ffan.com/";
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
        public String getXadminApiPrefix() {
            return "http://api.pre.ffan.com/xadmin/";
        }

        @Override
        public String getFFanApiPrefix() {
            return "http://api.pre.ffan.com/ffan/".concat(VERSION).concat("/");
        }

        @Override
        public String getMAppApiPrefix() {
            return "http://api.pre.ffan.com/mapp/".concat(VERSION).concat("/");
        }

        @Override
        public String getFFanH5Host() {
            return "http://sop.pre.ffan.com/";
        }

        @Override
        public String getFFanApiV1Host() {
            return "https://api.pre.ffan.com/v1/";
        }
    }

    /**
     * 生产环境主机域名工厂
     */
    static class ProductFactory implements IHostFactory {

        @Override
        public String getXadminApiPrefix() {
            return "https://api.ffan.com/xadmin/";
        }

        @Override
        public String getFFanApiPrefix() {
            return "https://api.ffan.com/ffan/".concat(VERSION).concat("/");
        }

        @Override
        public String getMAppApiPrefix() {
            return "https://api.ffan.com/mapp/".concat(VERSION).concat("/");
        }

        @Override
        public String getFFanH5Host() {
            return "https://sop.ffan.com/";
        }

        @Override
        public String getFFanApiV1Host() {
            return "https://api.ffan.com/";
        }
    }
}
