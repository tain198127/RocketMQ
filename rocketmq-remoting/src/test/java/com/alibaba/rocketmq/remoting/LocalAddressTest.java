package com.alibaba.rocketmq.remoting;

import com.alibaba.rocketmq.remoting.common.RemotingUtil;
import org.junit.Test;

/**
 * Created by tain on 2017/1/11.
 */
public class LocalAddressTest {
    @Test
    public void getLoacalAddress()
    {
        String address =  RemotingUtil.getLocalAddress();
        System.out.println(address);
    }
}
