package org.iubbo.proxy.common.utils;

/**
 * @author idea
 * @date 2019/7/19
 * @version V1.0
 */
public class TokenKeyUtil {

    private static final String TOKEN_PREFIX="iubbox:%s";

    public static String buildToken(String token) {
        return String.format(TOKEN_PREFIX, token);
    }


    public static void main(String[] args) {
        System.out.println(TokenKeyUtil.buildToken("cc8e6d87-c9a4-4fee-8d29-b6434c56aa26"));
    }
}
