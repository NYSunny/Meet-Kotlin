//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.filetransfer;

public class Configuration {
    private final int connectTimeout;
    private final int readTimeout;

    private Configuration(Configuration.Builder builder) {
        this.connectTimeout = builder.connectTimeout;
        this.readTimeout = builder.readTimeout;
    }

    protected int getConnectTimeout() {
        return this.connectTimeout;
    }

    protected int getReadTimeout() {
        return this.readTimeout;
    }

    public static class Builder {
        private int connectTimeout;
        private int readTimeout;
        private boolean isSelfCertificate;

        public Builder() {
        }

        public Configuration.Builder connectTimeout(int connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Configuration.Builder readTimeout(int readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public Configuration.Builder enableHttpsSelfCertificate(boolean isEnable) {
            this.isSelfCertificate = isEnable;
            return this;
        }

        public Configuration build() {
            return new Configuration(this);
        }

        public boolean isSelfCertificate() {
            return this.isSelfCertificate;
        }
    }
}
