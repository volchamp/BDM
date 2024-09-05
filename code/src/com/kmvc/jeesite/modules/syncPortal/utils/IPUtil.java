package com.kmvc.jeesite.modules.syncPortal.utils;

public class IPUtil
{
    public static boolean isInnerIP(final String ipAddress) {
        boolean isInnerIp = false;
        final long ipNum = getIpNum(ipAddress);
        final long aBegin = getIpNum("10.0.0.0");
        final long aEnd = getIpNum("10.255.255.255");
        final long bBegin = getIpNum("172.16.0.0");
        final long bEnd = getIpNum("172.31.255.255");
        final long cBegin = getIpNum("192.168.0.0");
        final long cEnd = getIpNum("192.168.255.255");
        isInnerIp = (isInner(ipNum, aBegin, aEnd) || isInner(ipNum, bBegin, bEnd) || isInner(ipNum, cBegin, cEnd) || ipAddress.equals("127.0.0.1"));
        return isInnerIp;
    }

    private static long getIpNum(final String ipAddress) {
        final String[] ip = ipAddress.split("\\.");
        final long a = Integer.parseInt(ip[0]);
        final long b = Integer.parseInt(ip[1]);
        final long c = Integer.parseInt(ip[2]);
        final long d = Integer.parseInt(ip[3]);
        final long ipNum = a * 256L * 256L * 256L + b * 256L * 256L + c * 256L + d;
        return ipNum;
    }

    private static boolean isInner(final long userIp, final long begin, final long end) {
        return userIp >= begin && userIp <= end;
    }
}
