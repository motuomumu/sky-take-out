package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     * @param employeeDTO
     * @return
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * 分页查询的方法
     *
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用与禁用--通过地址栏传参
     * @return
     */
    void startOrStop(Integer status, Long id);

    Employee getById(Long id);

    void update(EmployeeDTO employeeDTO);
}
