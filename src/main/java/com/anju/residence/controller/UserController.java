package com.anju.residence.controller;

import com.anju.residence.dto.UserDTO;
import com.anju.residence.entity.User;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.ApiException;
import com.anju.residence.service.UserService;
import com.anju.residence.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author cygao
 * @date 2020/12/10 19:00
 **/
@Api(tags = "用户API")
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @ApiOperation(value = "添加用户")
  @PostMapping("/add")
  public ResultVO<String> addUser(@RequestBody @Valid UserDTO userDTO) {
    userService.addUser(userDTO);
    StringBuilder sb = new StringBuilder();

    return new ResultVO<>("success");
  }

  @ApiOperation(value = "修改用户信息")
  @PutMapping("/{userId}")
  public ResultVO<String> putUser(@RequestBody @Valid UserDTO userDTO, @PathVariable Integer userId) {
    userService.putUser(userDTO, userId);

    return new ResultVO<>("success");
  }

  @PatchMapping("/{userId}")
  public ResultVO<String> patchUser(@RequestBody @Valid UserDTO userDTO, @PathVariable Integer userId) {
    userService.patchUser(userDTO, userId);

    return new ResultVO<>("success");
  }

  @GetMapping("/{userId}")
  public ResultVO<User> getUserById(@PathVariable Integer userId) {
    return new ResultVO<>(userService.getUserById(userId).orElseThrow(() -> new ApiException(ResultCode.USER_ID_NOT_EXISTS)));
  }


//  @ApiOperation("添加用户")
//  @PostMapping("/add")
//  public ResultVO<String> addUser(@RequestBody @Valid User user, BindingResult bindingResult) {
//    for (ObjectError error : bindingResult.getAllErrors()) {
//      return new ResultVO<>(ResultCode.ERROR, error.getDefaultMessage());
//    }
//    userService.addUser(user);
//    return new ResultVO<>("success");
//  }


}
