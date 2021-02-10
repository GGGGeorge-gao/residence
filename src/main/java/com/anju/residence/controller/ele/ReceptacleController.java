package com.anju.residence.controller.ele;

import com.anju.residence.dto.ele.ReceptacleDTO;
import com.anju.residence.service.ele.ReceptacleService;
import com.anju.residence.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author cygao
 * @date 2020/12/15 16:35
 **/
@Api(tags = "插座API（智能插座）")
@RequestMapping("/api/v1/receptacle")
@RestController
public class ReceptacleController {

  private final ReceptacleService receptacleService;

  @Autowired
  public ReceptacleController(ReceptacleService receptacleService) {
    this.receptacleService = receptacleService;
  }

  @ApiOperation(value = "新建一条插座")
  @PostMapping("add")
  public ResultVO<String> add(@RequestBody @Valid ReceptacleDTO receptacleDTO) {
    receptacleService.addReceptacle(receptacleDTO);

    return new ResultVO<>("success");
  }

  @ApiOperation(value = "修改插座信息")
  @ApiImplicitParams({@ApiImplicitParam(name = "receptacleId", value = "插座id", paramType = "path", dataTypeClass = Integer.class, required = true),
                      @ApiImplicitParam(name = "receptacleDTO", value = "插座信息", paramType = "body", dataTypeClass = ReceptacleDTO.class, required = true)})
  @PutMapping("/{receptacleId}")
  public ResultVO<String> put(@RequestBody @Valid ReceptacleDTO receptacleDTO, @PathVariable Integer receptacleId) {
    receptacleService.putReceptacle(receptacleDTO, receptacleId);

    return new ResultVO<>("success");
  }

  @ApiOperation(value = "删除一个插座")
  @ApiImplicitParams({@ApiImplicitParam(name = "receptacleId", value = "插座id", paramType = "path", dataTypeClass = Integer.class, required = true)})
  @DeleteMapping("/{receptacleId}")
  public ResultVO<String> delete(@PathVariable @Valid Integer receptacleId) {
    receptacleService.deleteReceptacle(receptacleId);

    return new ResultVO<>("success");
  }
}
