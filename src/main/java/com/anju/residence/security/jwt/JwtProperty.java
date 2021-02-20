package com.anju.residence.security.jwt;

/**
 * @author cygao
 * @date 2021/1/3 16:28
 **/
public class JwtProperty {

  public static final String TOKEN_START_WITH = "Bearer ";

  public static final transient String SECRET = "2AGL7I2OWm3YZQW89H1SDqWwE3289DISFNJ32SQxMZ27FDSB3G42AsX12CQrWQ10EJ20TvaWV6qO02CL9WI3BXP8HF23IE0FNQZ812SDA34HFZ183A3SWD61BSASF12G6AWC8ASD78";

  public static final int EXPIRATION = 14400000;

  public static final String TOKEN_HEADER = "Authorization";

  public static final String PASSWORD_LOGIN_URL = "/api/v1/login";

  public static final String WECHAT_LOGIN_URL = "/wx_auth";
}
