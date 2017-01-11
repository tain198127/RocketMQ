/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.alibaba.rocketmq.client;

import com.alibaba.rocketmq.common.MixAll;
import com.alibaba.rocketmq.common.UtilAll;
import com.alibaba.rocketmq.remoting.common.RemotingUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Client Common configuration
 *
 * @author shijia.wxr
 * @author vongosling
 */
public class ClientConfig {
    public static final String SendMessageWithVIPChannelProperty = "com.rocketmq.sendMessageWithVIPChannel";
    private String namesrvAddr = System.getProperty(MixAll.NAMESRV_ADDR_PROPERTY, System.getenv(MixAll.NAMESRV_ADDR_ENV));
    private String clientIP = RemotingUtil.getLocalAddress();
    private List<String> clientIPArray = RemotingUtil.getLocalAddressArray();
    private boolean isClientIPReplaced=false;
    private String instanceName = System.getProperty("rocketmq.client.name", "DEFAULT");
    private int clientCallbackExecutorThreads = Runtime.getRuntime().availableProcessors();
    /**
     * Pulling topic information interval from the named server
     */
    private int pollNameServerInteval = 1000 * 30;
    /**
     * Heartbeat interval in microseconds with message broker
     */
    private int heartbeatBrokerInterval = 1000 * 30;
    /**
     * Offset persistent interval for consumer
     */
    private int persistConsumerOffsetInterval = 1000 * 5;
    private boolean unitMode = false;
    private String unitName;
    private boolean vipChannelEnabled = Boolean.parseBoolean(System.getProperty(SendMessageWithVIPChannelProperty, "true"));
    ;


    public String buildMQClientId() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClientIP());

        sb.append("@");
        sb.append(this.getInstanceName());
        if (!UtilAll.isBlank(this.unitName)) {
            sb.append("@");
            sb.append(this.unitName);
        }

        return sb.toString();
    }

    public String getClientIP() {
        return clientIP;
    }

    /**
     * to match the right ip,
     * side effort:if the candidate is only one,the clientIP will be replace by the first candidate
     * @param ipPattern:ip parttern,if ipPattern is null or empty,will return all the candidate, e.g:10.1.2
     *
     *
     * @return ip candidate list
     */
    public List<String> computeClientIP(String ipPattern){
        List<String> rstIPArray = new ArrayList<String>(this.clientIPArray.size());
        if(ipPattern != null && !ipPattern.isEmpty())
        {
            for(String ip : this.clientIPArray)
            {
                if(ip.contains(ipPattern))
                    rstIPArray.add(ip);
            }
            if(!isClientIPReplaced && rstIPArray.size()<=0)
                throw new RuntimeException("you do not have ip address pls use ifconfig to check you ip");

            if(!isClientIPReplaced && rstIPArray.size() == 1)
                this.setClientIP(rstIPArray.get(0));
        }
        else{
            rstIPArray.addAll(this.clientIPArray);
        }
        if(rstIPArray.size()>1 && !isClientIPReplaced)
            try{
                throw new RuntimeException("you have more than one ip address, pls use <computeClientIP> to avoid it ,or use <setClientIP>");
            }
            catch(Exception e){
                e.printStackTrace();
            }
        return rstIPArray;
    }
    public void setClientIP(String clientIP) {
        isClientIPReplaced=true;
        this.clientIP = clientIP;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public void changeInstanceNameToPID() {
        if (this.instanceName.equals("DEFAULT")) {
            this.instanceName = String.valueOf(UtilAll.getPid());
        }
    }

    public void resetClientConfig(final ClientConfig cc) {
        this.namesrvAddr = cc.namesrvAddr;
        this.clientIP = cc.clientIP;
        this.instanceName = cc.instanceName;
        this.clientCallbackExecutorThreads = cc.clientCallbackExecutorThreads;
        this.pollNameServerInteval = cc.pollNameServerInteval;
        this.heartbeatBrokerInterval = cc.heartbeatBrokerInterval;
        this.persistConsumerOffsetInterval = cc.persistConsumerOffsetInterval;
        this.unitMode = cc.unitMode;
        this.unitName = cc.unitName;
        this.vipChannelEnabled = cc.vipChannelEnabled;
    }

    public ClientConfig cloneClientConfig() {
        ClientConfig cc = new ClientConfig();
        cc.namesrvAddr = namesrvAddr;
        cc.clientIP = clientIP;
        cc.instanceName = instanceName;
        cc.clientCallbackExecutorThreads = clientCallbackExecutorThreads;
        cc.pollNameServerInteval = pollNameServerInteval;
        cc.heartbeatBrokerInterval = heartbeatBrokerInterval;
        cc.persistConsumerOffsetInterval = persistConsumerOffsetInterval;
        cc.unitMode = unitMode;
        cc.unitName = unitName;
        cc.vipChannelEnabled = vipChannelEnabled;
        return cc;
    }

    public String getNamesrvAddr() {
        return namesrvAddr;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    public int getClientCallbackExecutorThreads() {
        return clientCallbackExecutorThreads;
    }


    public void setClientCallbackExecutorThreads(int clientCallbackExecutorThreads) {
        this.clientCallbackExecutorThreads = clientCallbackExecutorThreads;
    }


    public int getPollNameServerInteval() {
        return pollNameServerInteval;
    }


    public void setPollNameServerInteval(int pollNameServerInteval) {
        this.pollNameServerInteval = pollNameServerInteval;
    }


    public int getHeartbeatBrokerInterval() {
        return heartbeatBrokerInterval;
    }


    public void setHeartbeatBrokerInterval(int heartbeatBrokerInterval) {
        this.heartbeatBrokerInterval = heartbeatBrokerInterval;
    }


    public int getPersistConsumerOffsetInterval() {
        return persistConsumerOffsetInterval;
    }


    public void setPersistConsumerOffsetInterval(int persistConsumerOffsetInterval) {
        this.persistConsumerOffsetInterval = persistConsumerOffsetInterval;
    }


    public String getUnitName() {
        return unitName;
    }


    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }


    public boolean isUnitMode() {
        return unitMode;
    }


    public void setUnitMode(boolean unitMode) {
        this.unitMode = unitMode;
    }


    public boolean isVipChannelEnabled() {
        return vipChannelEnabled;
    }


    public void setVipChannelEnabled(final boolean vipChannelEnabled) {
        this.vipChannelEnabled = vipChannelEnabled;
    }


    @Override
    public String toString() {
        return "ClientConfig [namesrvAddr=" + namesrvAddr + ", clientIP=" + clientIP + ", instanceName=" + instanceName
                + ", clientCallbackExecutorThreads=" + clientCallbackExecutorThreads + ", pollNameServerInteval=" + pollNameServerInteval
                + ", heartbeatBrokerInterval=" + heartbeatBrokerInterval + ", persistConsumerOffsetInterval="
                + persistConsumerOffsetInterval + ", unitMode=" + unitMode + ", unitName=" + unitName + ", vipChannelEnabled="
                + vipChannelEnabled + "]";
    }
}
