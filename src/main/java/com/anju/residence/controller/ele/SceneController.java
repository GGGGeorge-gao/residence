package com.anju.residence.controller.ele;

import com.anju.residence.dto.ele.SceneDTO;
import com.anju.residence.entity.ele.Scene;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.ApiException;
import com.anju.residence.service.SceneService;
import com.anju.residence.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author cygao
 * @date 2020/12/15 16:34
 **/
@Api(tags = "场景API（智能插座）")
@RequestMapping("/scene")
@RestController
public class SceneController {

  private final SceneService sceneService;

  @Autowired
  public SceneController(SceneService sceneService) {
    this.sceneService = sceneService;
  }

  @ApiOperation(value = "根据场景id查询场景信息")
  @GetMapping("/{sceneId}")
  public ResultVO<Scene> getById(@PathVariable Integer sceneId) {
    return new ResultVO<>(sceneService.getById(sceneId).orElseThrow(() -> new ApiException(ResultCode.SCENE_ID_NOT_EXISTS)));
  }

  @ApiOperation(value = "添加一个场景")
  @PostMapping("/add")
  public ResultVO<String> add(@RequestBody @Valid SceneDTO sceneDTO) {
    sceneService.addScene(sceneDTO);

    return new ResultVO<>("success");
  }

  @ApiOperation(value = "修改场景信息")
  @PutMapping("/{sceneId}")
  public ResultVO<String> put(@RequestBody @Valid SceneDTO sceneDTO, @PathVariable Integer sceneId) {
    sceneService.putScene(sceneDTO, sceneId);

    return new ResultVO<>("success");
  }

  @ApiOperation(value = "删除一个场景")
  @DeleteMapping("/{sceneId}")
  public ResultVO<String> delete(@PathVariable Integer sceneId) {
    sceneService.deleteScene(sceneId);

    return new ResultVO<>("success");
  }

  @ApiOperation(value = "获取用户所有的场景", notes = "url路径参数为用户id")
  @GetMapping("/user/{userId}")
  public ResultVO<List<SceneDTO>> listTreeByUserId(@PathVariable Integer userId) {
    return new ResultVO<>(sceneService.listTreeByUserId(userId));
  }

  @ApiOperation(value = "更新场景的父id", notes = "父id为request param, 当前场景id为url路径参数")
  @PatchMapping("/parent/{sceneId}")
  public ResultVO<String> updateParentId(@RequestParam Integer parentId, @PathVariable Integer sceneId) {
    if (sceneId.equals(parentId)) {
      throw new ApiException(ResultCode.INVALID_PARENT_SCENE_ID);
    }
    sceneService.updateParentId(sceneId, parentId);

    return new ResultVO<>("success");
  }
}
