package com.anju.residence.controller;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anju.residence.entity.WxUser;
import com.anju.residence.service.ele.WxUserService;
import com.anju.residence.util.WechatUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/wxUser")
public class WxUserController {
  private WxUserService wxUserService;
  private String result200;

  @Autowired
  public WxUserController(WxUserService wxUserService) {
    Map<String, Object> map = new HashMap<>();
    map.put("status", 200);
    map.put("msg", "ok");
    result200 = JSON.toJSONString(map);
    this.wxUserService = wxUserService;
  }

  /**
   * 微信用户登录详情
   */
  @PostMapping("/login")
  @ResponseBody
  public String user_login(@RequestParam(value = "resultCode", required = false) String code,
                           @RequestParam(value = "rawData", required = false) String rawData,
                           @RequestParam(value = "signature", required = false) String signature,
                           @RequestParam(value = "encrypteData", required = false) String encrypteData,
                           @RequestParam(value = "iv", required = false) String iv) {

    // 用户非敏感信息：rawData
    // 签名：signature
    System.out.println(code);
    System.out.println(rawData);
    System.out.println(signature);
    System.out.println(encrypteData);
    System.out.println(iv);
    JSONObject rawDataJson = JSON.parseObject(rawData);
    // 1.接收小程序发送的code
    // 2.开发者服务器 登录凭证校验接口 appi + appsecret + resultCode
    JSONObject SessionKeyOpenId = WechatUtil.getSessionKeyOrOpenId(code);
    // 3.接收微信接口服务 获取返回的参数
    String openid = SessionKeyOpenId.getString("openid");
    String sessionKey = SessionKeyOpenId.getString("session_key");

    // 4.校验签名 小程序发送的签名signature与服务器端生成的签名signature2 = sha1(rawData + sessionKey)
    String signature2 = DigestUtils.sha1Hex(rawData + sessionKey);
    System.out.println("两次signature");
    System.out.println(signature);
    System.out.println(signature2);
    if (!signature.equals(signature2)) {
      Map<String, Object> map = new HashMap<>();
      map.put("status", 500);
      map.put("msg", "签名校验失败");
      return JSON.toJSONString(map);
    }
    // 5.根据返回的User实体类，判断用户是否是新用户，是的话，将用户信息存到数据库；不是的话，更新最新登录时间
    WxUser user = wxUserService.selectUserByOpenId(openid);
    // uuid生成唯一key，用于维护微信小程序用户与服务端的会话
    String skey = IdUtil.randomUUID();
    String nickName = rawDataJson.getString("nickName");
    String avatarUrl = rawDataJson.getString("avatarUrl");
    String gender = rawDataJson.getString("gender");
    String city = rawDataJson.getString("city");
    String country = rawDataJson.getString("country");
    String province = rawDataJson.getString("province");
    if (user == null) {
      // 用户信息入库
      user = new WxUser();
      user.setOpenId(openid);
      user.setSkey(skey);
      user.setCreateTime(new Date());
      user.setLastVisitTime(new Date());
      user.setSessionKey(sessionKey);
      user.setCity(city);
      user.setProvince(province);
      user.setCountry(country);
      user.setAvatarUrl(avatarUrl);
      user.setGender(Integer.parseInt(gender));
      user.setNickName(nickName);
      wxUserService.insert(user);
    } else {
      // 已存在，更新用户登录时间
      user.setLastVisitTime(new Date());
      // 重新设置会话skey
      user.setSkey(skey);
      user.setCity(city);
      user.setProvince(province);
      user.setCountry(country);
      user.setGender(Integer.parseInt(gender));
      user.setNickName(nickName);
      wxUserService.update(user);
    }
    //encrypteData比rowData多了appid和openid
    //JSONObject userInfo = WechatUtil.getUserInfo(encrypteData, sessionKey, iv);
    //6. 把新的skey返回给小程序
    Map<String, Object> map = new HashMap<>();
    map.put("status", 200);
    map.put("msg", "ok");
    Map<String, Object> data = new HashMap<>();
    data.put("skey", skey);
    //TODO: 返回user的POJO
    data.put("user", null);
    map.put("data", data);
    return JSON.toJSONStringWithDateFormat(map, "yyyy-MM-dd HH:mm:ss");
  }

  /**
   * 微信用户获取用户数据
   */
  @RequestMapping("/fetch")
  @ResponseBody
  public String fetch(HttpServletRequest request) {
    Map<String, Object> map = new HashMap<>();
    String skey = request.getHeader("skey");
    if ("".equals(skey)) {
      return return500("skey missing!");
    }
    WxUser user = wxUserService.selectUserBySkey(skey);
    user.setLastVisitTime(new Date());
    wxUserService.update(user);
    Map<String, Object> data = new HashMap<>();
    data.put("user", null);
    return return200(data);
  }

//    @RequestMapping("/countNewMessage")
//    @ResponseBody
//    public String countNewMessage(HttpServletRequest request) {
//        BbsUser user = bbsUserService.selectUserBySkey(request);
//        Map<String, Object> data = new HashMap<>();
//        data.put("newMessageCount", bbsMessageService.countNewMessage(user));
//        return return200(data);
//    }
//
//    @RequestMapping("/getMyMessage")
//    @ResponseBody
//    public String getMyMessage(HttpServletRequest request) {
//        BbsUser user = bbsUserService.selectUserBySkey(request);
//        Map<String, Object> data = new HashMap<>();
//        data.put("messages", bbsMessageService.getMyMessage(user));
//        return return200(data);
//    }
//
//    @RequestMapping("/confirmMessage")
//    @ResponseBody
//    public String confirmMessage(HttpServletRequest request, @RequestBody List<String> mIds) {
//        bbsMessageService.confirmMessages(mIds);
//        return return200();
//    }
//
//    @RequestMapping("/subscribe")
//    @ResponseBody
//    public String subscribe(HttpServletRequest request, @RequestBody List<String> templateIds) {
//        BbsUser user = bbsUserService.selectUserBySkey(request);
//        bbsSubscribeMessageService.subscribe(user, templateIds);
//        return return200();
//    }
//
//    @RequestMapping("/getAllSchools")
//    public String getAllSchools() {
//        Map<String, Object> data = new HashMap<>();
//        data.put("schools", bbsSchoolService.selectAll());
//        return return200(data);
//    }
//
//    @RequestMapping("/setSchool")
//    public String setSchool(HttpServletRequest request, @RequestParam int scId) {
//        BbsUser user = bbsUserService.selectUserBySkey(request);
//        if (Info.ifSchoolSwitchNeedVerify) {
//            BbsVerify verify = bbsVerifyMapper.selectByPrimaryKey(user.getOpenId());
//            if (verify != null && verify.getVStatus() != 2) {
//                if (verify.getVStatus()==0){
//                    return return500("正在审核中，当前禁止切换学校");
//                }
//                user.setScId(verify.getVScId());
//                bbsUserService.update(user);
//                if (verify.getVScId() == scId) {
//                    return return200();
//                } else {
//                    return return500("已提交认证，当前禁止切换学校");
//                }
//            }
//        }
//        user.setScId(scId);
//        bbsUserService.update(user);
//        return return200();
//
//    }
//
//    @RequestMapping("/getTags")
//    public String getTags() {
//        Map<String, Object> data = new HashMap<>();
//        data.put("tags", bbsSchoolService.selectTags());
//        return return200(data);
//    }
//
//    @RequestMapping("/getwxacodeunlimit")
//    public String getwxacodeunlimit(@RequestBody String json) {
//        JSONObject req = JSONObject.parseObject(json);
//        String pId = req.getString("pId");
//        Example example = new Example(BbsShortPId.class);
//        Example.Criteria criteria = example.createCriteria();
//        criteria.andEqualTo("pId", pId);
//        BbsShortPId t;
//        List<BbsShortPId> ts = bbsShortPIdMapper.selectByExample(example);
//        if (ts.size() == 0) {
//            t = new BbsShortPId();
//            t.setPId(pId);
//            bbsShortPIdMapper.insertUseGeneratedKeys(t);
//            t = bbsShortPIdMapper.selectByExample(example).get(0);
//        } else {
//            t = ts.get(0);
//        }
//        req.remove("pId");
//        req.put("scene", "pShortId=" + t.getPIdShort());
//        req.put("page", "pages/postDtl/postDtl");
////        String res = HttpUtil.post("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + Param.AccessToken, JSON.toJSONString(req));
//        HttpRequest request = HttpUtil.createPost("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + Param.AccessToken);
//        request.header("responseType", "arraybuffer");
//        request.body(req.toJSONString());
//        HttpResponse response = request.execute();
//        int count = 0;
//        while (Util.isJSONValid(response.body()) && count < 10) {
//            ScheduleTask.reGetWxToken();
//            request = HttpUtil.createPost("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + Param.AccessToken);
//            request.header("responseType", "arraybuffer");
//            request.body(req.toJSONString());
//            response = request.execute();
//            if (!Util.isJSONValid(response.body())) {
//                break;
//            }
//            count++;
//        }
//        byte[] res = response.bodyBytes();
//        Map<String, Object> data = new HashMap<>();
//        data.put("base64", Base64.encode(res));
//        return return200(data);
//    }
//
//    @RequestMapping("/setAvatar")
//    public String setAvatar(HttpServletRequest request, @RequestBody Map<String, String> map) {
//        BbsUser user = bbsUserService.selectUserBySkey(request);
//        user.setAvatarUrl(map.get("url"));
//        bbsUserService.update(user);
//        return return200();
//    }
//
//    @RequestMapping("/setFakeName")
//    public String setFakeName(HttpServletRequest request, @RequestBody Map<String, String> map) {
//        BbsUser user = bbsUserService.selectUserBySkey(request);
//        Map<String, Object> map1 = new HashMap<>();
//        map1.put("content", map.get("fakeName"));
//        JSONObject ret = JSON.parseObject(HttpUtil.post("https://api.weixin.qq.com/wxa/msg_sec_check?access_token=" + Param.AccessToken, JSON.toJSONString(map1)));
//        int count = 0;
//        while (ret.getIntValue("errcode") == 40001 && count < 10) {
//            ScheduleTask.reGetWxToken();
//            ret = JSON.parseObject(HttpUtil.post("https://api.weixin.qq.com/wxa/msg_sec_check?access_token=" + Param.AccessToken, JSON.toJSONString(map1)));
//            count++;
//        }
//        if (ret.getIntValue("errcode") == 87014) {
//            return return500("risky");
//        }
//        user.setFakeName(map.get("fakeName"));
//        bbsUserService.update(user);
//        return return200();
//    }
//
//    @RequestMapping("/onlineAddRq")
//    public String onlineAddRq(HttpServletRequest request, @RequestParam int onlineMinutes) {
//        BbsUser user = bbsUserService.selectUserBySkey(request);
//        user.setRq(user.getRq() + onlineMinutes);
//        user.setLevelRq(user.getLevelRq() + onlineMinutes);
//        bbsUserService.update(user);
//        BbsRq rq = new BbsRq();
//        rq.setRqOpenId(user.getOpenId());
//        rq.setRqRemark("在线" + onlineMinutes + "分钟");
//        rq.setRqTime(new Date());
//        rq.setRqValue(onlineMinutes);
//        bbsRqMapper.insert(rq);
//        return return200();
//    }
//
//    @RequestMapping("/getMyRqHistory")
//    public String getMyRqHistory(HttpServletRequest request) {
//        BbsUser user = bbsUserService.selectUserBySkey(request);
//        Example example = new Example(BbsRq.class);
//        Example.Criteria criteria = example.createCriteria();
//        criteria.andEqualTo("rqOpenId", user.getOpenId());
//        example.setOrderByClause("rq_time desc limit 0,100");
//        List<BbsRq> rqHistory = bbsRqMapper.selectByExample(example);
//        Map<String, Object> data = new HashMap<>();
//        data.put("rqHistory", rqHistory);
//        return return200(data);
//    }
//
//    @RequestMapping("/check")
//    public String check(HttpServletRequest request) {
//        BbsUser user = bbsUserService.selectUserBySkey(request);
//        Date now = new Date();
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DATE, -1);
//        Date yesterday = cal.getTime();
//        Example example = new Example(BbsCheck.class);
//        Example.Criteria criteria = example.createCriteria();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String dateStr = sdf.format(yesterday);
//        criteria.andCondition("ck_date like '" + dateStr + "%'");
//        criteria.andEqualTo("ckOpenId", user.getOpenId());
//        BbsCheck lastCheck = bbsCheckMapper.selectOneByExample(example);
//        BbsCheck check = new BbsCheck();
//        check.setCkId(IdUtil.randomUUID());
//        if (lastCheck == null) {
//            check.setCkDate(now);
//            check.setCkConti(1);
//            check.setCkOpenId(user.getOpenId());
//        } else {
//            check.setCkDate(now);
//            check.setCkConti(lastCheck.getCkConti() + 1);
//            check.setCkOpenId(user.getOpenId());
//        }
//        bbsCheckMapper.insert(check);
//        user.setRq(user.getRq() + check.getCkConti() * Rq.checkTimes);
//        bbsUserService.update(user);
//        bbsRqMapper.insert(new BbsRq("连续签到" + check.getCkConti() + "天", user.getOpenId(), check.getCkConti() * Rq.checkTimes, now));
//        return return200();
//    }
//
//    @RequestMapping("/getCheckInfo")
//    public String getCheckInfo(HttpServletRequest request) {
//        BbsUser user = bbsUserService.selectUserBySkey(request);
//        Date now = new Date();
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DATE, -1);
//        Date yesterday = cal.getTime();
//        Example example = new Example(BbsCheck.class);
//        example.setOrderByClause("ck_date desc");
//        Example.Criteria criteria = example.createCriteria();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        criteria.andCondition("ck_date like '" + sdf.format(now) + "%'");
//        criteria.andEqualTo("ckOpenId", user.getOpenId());
//        BbsCheck check = bbsCheckMapper.selectOneByExample(example);
//        Map<String, Object> data = new HashMap<>();
//        if (check != null) {
//            data.put("check", check);
//            data.put("checkedToday", true);
//        } else {
//            Example example1 = new Example(BbsCheck.class);
//            Example.Criteria criteria1 = example1.createCriteria();
//            criteria1.andCondition("ck_date like '" + sdf.format(yesterday) + "%'");
//            criteria1.andEqualTo("ckOpenId", user.getOpenId());
//            BbsCheck check1 = bbsCheckMapper.selectOneByExample(example1);
//            if (check1 != null) {
//                data.put("check", check1);
//                data.put("checkedToday", false);
//            } else {
//                data.put("checkedToday", false);
//            }
//        }
//        return return200(data);
//    }
//
//    @RequestMapping("/getIfStudyShare")
//    public String getIfStudyShare() {
//        Map<String, Object> data = new HashMap<>();
//        data.put("ifStudyShare", Info.ifStudyShare);
//        return return200(data);
//    }
//
//    @RequestMapping("/getMyVerify")
//    public String getMyVerify(HttpServletRequest request) {
//        BbsUser user = bbsUserService.selectUserBySkey(request);
//        Map<String, Object> data = new HashMap<>();
//        data.put("verify", bbsVerifyMapper.selectByPrimaryKey(user.getOpenId()));
//        return return200(data);
//    }
//
//    @RequestMapping("/submitVerify")
//    public String submitVerify(HttpServletRequest request, @RequestBody String json) {
//        BbsVerify verify = JSONObject.parseObject(json, BbsVerify.class);
//        BbsUser user = bbsUserService.selectUserBySkey(request);
//        BbsVerify origin = bbsVerifyMapper.selectByPrimaryKey(user.getOpenId());
//        verify.setVOpenId(user.getOpenId());
//        verify.setVScId(user.getScId());
//        verify.setVStatus(0);
//        verify.setVRemark("");
//        if(origin!=null){
//            bbsVerifyMapper.updateByPrimaryKey(verify);
//        }else {
//            bbsVerifyMapper.insert(verify);
//        }
//        return return200();
//    }
//
//    @RequestMapping("/getVerifyList")
//    public String getVerifyList(HttpServletRequest request){
//        BbsUser user = bbsUserService.selectUserBySkey(request);
//        Map<String, Object> data = new HashMap<>();
//        data.put("verifyList", bbsVerifyMapper.selectPojos(user.getScId()));
//        return return200(data);
//    }
//
//    @RequestMapping("/confirmVerify")
//    public String confirmVerify(@RequestBody Map<String,Object> map){
//        BbsVerify verify = bbsVerifyMapper.selectByPrimaryKey(map.get("openId"));
//        verify.setVStatus((int)map.get("status"));
//        verify.setVRemark((String)map.get("remark"));
//        bbsVerifyMapper.updateByPrimaryKey(verify);
//        return return200();
//    }

  private String return200() {
    return result200;
  }

  private String return200(Map<String, Object> data) {
    Map<String, Object> map = new HashMap<>();
    map.put("status", 200);
    map.put("msg", "ok");
    map.put("data", data);
    return JSON.toJSONString(map);
  }

  private String return500(String msg) {
    Map<String, Object> map = new HashMap<>();
    map.put("status", 500);
    map.put("msg", msg);
    return JSON.toJSONString(map);
  }
}

