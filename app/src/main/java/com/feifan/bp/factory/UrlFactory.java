package com.feifan.bp.factory;

/**
 * Created by xuchunlei on 15/6/18.
 */
class UrlFactory {
    /**
     * Gateway集成测试环境Url工厂
     *
     * @author xuchunlei
     *         create at: 2015年4月24日 下午4:11:49
     */
    static class UrlGatewaySitFactory implements IUrlFactory {

        private static IUrlFactory mInstance;

        /**
         * 获取单例实例
         *
         * @return
         * @author: xuchunlei
         * created at: 2015年4月24日 下午4:20:48
         */
        static IUrlFactory getInstance() {
            if (mInstance == null) {
                mInstance = new UrlGatewaySitFactory();
            }
            return mInstance;
        }

        @Override
        public String getFFanHostUrl() {
            return "http://api.sit.ffan.com/";
        }

        @Override
        public String getH5HostUrl() {
            return "http://sop.sit.ffan.com/";
        }
    }


    /**
     * Api集成测试环境Url工厂
     *
     * @author xuchunlei
     *         create at: 2015年4月24日 下午4:14:35
     */
    static class UrlApiSitFactory implements IUrlFactory {

        private static IUrlFactory mInstance;

        /**
         * 获取单例实例
         *
         * @return
         * @author: xuchunlei created at: 2015年4月24日 下午4:20:48
         */
        static IUrlFactory getInstance() {
            if (mInstance == null) {
                mInstance = new UrlApiSitFactory();
            }
            return mInstance;
        }

        @Override
        public String getFFanHostUrl() {
            return "http://xapi.sit.ffan.com/";
        }

        @Override
        public String getH5HostUrl() {
            return "http://sop.sit.ffan.com/";
        }
    }

    /**
     * 预生产环境URL工厂
     */
    static class UrlProductPreFactory implements IUrlFactory {

        private static IUrlFactory mInstance;

        /**
         * 获取单例实例
         *
         * @return
         * @author: xuchunlei created at: 2015年4月24日 下午4:20:48
         */
        static IUrlFactory getInstance() {
            if (mInstance == null) {
                mInstance = new UrlProductPreFactory();
            }
            return mInstance;
        }

        @Override
        public String getFFanHostUrl() {
            return "http://api.pre.ffan.com/";
        }

        @Override
        public String getH5HostUrl() {
            return "http://sop.pre.ffan.com/";
        }
    }

    /**
     * 生产环境URL工厂
     */
    static class UrlProductFactory implements IUrlFactory {

        private static IUrlFactory mInstance;

        /**
         * 获取单例实例
         *
         * @return
         * @author: xuchunlei created at: 2015年4月24日 下午4:20:48
         */
        static IUrlFactory getInstance() {
            if (mInstance == null) {
                mInstance = new UrlProductFactory();
            }
            return mInstance;
        }

        @Override
        public String getFFanHostUrl() {
            return "http://api.ffan.com/";
        }

        @Override
        public String getH5HostUrl() {
            return "http://sop.ffan.com/";
        }
    }
}
