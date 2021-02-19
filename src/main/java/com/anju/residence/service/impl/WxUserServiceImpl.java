package com.anju.residence.service.impl;

import com.anju.residence.dao.WxUserRepository;
import com.anju.residence.dto.wx.WxUserDTO;
import com.anju.residence.entity.User;
import com.anju.residence.entity.WxUser;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.ApiException;
import com.anju.residence.security.model.WxSession;
import com.anju.residence.service.UserService;
import com.anju.residence.service.WxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class WxUserServiceImpl implements WxUserService {

  private final WxUserRepository wxUserRepo;

  private final UserService userService;

  @Autowired
  public WxUserServiceImpl(WxUserRepository wxUserRepository, UserService userService) {
    this.wxUserRepo = wxUserRepository;
    this.userService = userService;
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public WxUser addWxUser(WxUserDTO wxUserDTO) {
    if (existsByOpenid(wxUserDTO.getOpenId())) {
      throw new ApiException(ResultCode.OPEN_ID_ALREADY_EXISTS);
    }
    WxUser wxUser = wxUserDTO.build();
    User user = userService.addByWxUser(wxUserDTO);

    wxUser.setUser(user);
    wxUser.setLastVisitTime(new Date());
    wxUser.setCreateTime(new Date());
    return save(wxUser);
  }

  @Override
  public Optional<WxUser> getWxUserBySkey(String skey) {
    WxUser user = new WxUser();
    user.setSkey(skey);
    Example<WxUser> example = Example.of(user);
    return wxUserRepo.findOne(example);
  }

  @Override
  public Optional<WxUser> getWxUserByOpenId(String openId) {
    return wxUserRepo.findById(openId);
  }

  @Override
  public Optional<String> getSessionKeyByOpenId(String openId) {
    return wxUserRepo.findSessionKeyByOpenId(openId);
  }

  @Override
  public void updateByWxSession(WxSession wxSession) {
    String openId = wxSession.getOpenId();
    WxUser wxUser = getWxUserByOpenId(openId).orElseThrow(() -> new ApiException(ResultCode.OPEN_ID_NOT_EXISTS));

    wxUser.setSkey(wxSession.getSkey());
    wxUser.setSessionKey(wxSession.getSessionKey());
    wxUser.setLastVisitTime(new Date());

    save(wxUser);
  }

  @Override
  public boolean existsByOpenid(String openId) {
    return openId != null && wxUserRepo.existsById(openId);
  }

  @Override
  public WxUser save(WxUser wxUser) {
    return wxUserRepo.saveAndFlush(wxUser);
  }


}
