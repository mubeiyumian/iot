package com.iot.user.repository;

import com.iot.common.pojo.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/24 22:02
 * @Version: 1.0
 */

public interface SysUserRepository extends JpaRepository<SysUser, Long> {

    Optional<SysUser> findByPhone(String phone);
}
