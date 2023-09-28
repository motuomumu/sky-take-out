package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    //接收参数都是DTO这个形式，返回结果里面通过VO进行封装
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);
        //JWT令牌生成好之后，通过构建器bulider对数据进行封装，响应给前端页面，封装成了VO
        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)  //jwt令牌
                .build();   //调用build()这个方法将其构建好

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }


    /**
     * 实现新增员工的功能
     *
     * @return
     */
    @PostMapping
    @ApiOperation("新增员工") //接收的是传入的数据已经打包好的DTO
    public Result save(@RequestBody EmployeeDTO employeeDTO){
        log.info("新增员工：{}",employeeDTO);
        System.out.println("当前线程的id:"+Thread.currentThread().getId());
        employeeService.save(employeeDTO);
        return Result.success();
    }

    /**
     * 分页查询的方法
     *
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("员工分页查询")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("员工分页查询：{}",employeePageQueryDTO);
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);

    }

    /**
     * 启用与禁用--通过地址栏传参
     * @return
     */
    @PostMapping("status/{status}")
    @ApiOperation("启用禁用员工账号")
    public Result startOrStop(@PathVariable Integer status,Long id){
        log.info("启用禁用员工账号:{},{}",status,id);
        employeeService.startOrStop(status,id);
        return Result.success();

    }

    /**
     * 编辑员工：查询员工+编辑员工
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询员工信息")
    public Result<Employee> getById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    @ApiOperation("编辑员工信息")
    @PutMapping
    public Result update(@RequestBody EmployeeDTO employeeDTO){
        log.info("编辑员工信息:{}",employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }

}
