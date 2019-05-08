package com.jipf.example.hadoop;

public class HdfsProperties {

    /**
     * 操作用户
     */
    private String user;

    /**
     * namenode 地址
     */
    private String url;

    /**
     * ha 连接
     */
    private String nameServices;

    private String nameNode1Url;

    private String nameNode2Url;

    /**
     * namenode ha 配置名
     * <property>
     *    <name>dfs.ha.namenodes.geocluster</name>
     *    <value>nn1,nn2</value>
     * </property>
     */
    private String[] nns;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNameServices() {
        return nameServices;
    }

    public void setNameServices(String nameServices) {
        this.nameServices = nameServices;
    }

    public String getNameNode1Url() {
        return nameNode1Url;
    }

    public void setNameNode1Url(String nameNode1Url) {
        this.nameNode1Url = nameNode1Url;
    }

    public String getNameNode2Url() {
        return nameNode2Url;
    }

    public void setNameNode2Url(String nameNode2Url) {
        this.nameNode2Url = nameNode2Url;
    }

    public String[] getNns() {
        return nns;
    }

    public void setNns(String[] nns) {
        this.nns = nns;
    }
}
