package com.zopsmart.updatehelper.pojo;

public class UpdateConfig {
    private int latestVersion;
    private String name;
    private int lastSupportedVersion;

    public UpdateConfig(int latestVersion, String name, int lastSupportedVersion) {
        this.latestVersion = latestVersion;
        this.name = name;
        this.lastSupportedVersion = lastSupportedVersion;
    }

    public int getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(int latestVersion) {
        this.latestVersion = latestVersion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLastSupportedVersion() {
        return lastSupportedVersion;
    }

    public void setLastSupportedVersion(int lastSupportedVersion) {
        this.lastSupportedVersion = lastSupportedVersion;
    }

    @Override
    public String toString() {
        return "Update{" +
                "latestVersion=" + latestVersion +
                ", name='" + name + '\'' +
                ", lastSupportedVersion=" + lastSupportedVersion +
                '}';
    }
}
