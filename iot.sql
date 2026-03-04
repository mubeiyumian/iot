/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80030 (8.0.30)
 Source Host           : localhost:3306
 Source Schema         : smart_iot_platform

 Target Server Type    : MySQL
 Target Server Version : 80030 (8.0.30)
 File Encoding         : 65001

 Date: 26/02/2026 17:34:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for iot_device
-- ----------------------------
DROP TABLE IF EXISTS `iot_device`;
CREATE TABLE `iot_device`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NOT NULL COMMENT '产品ID',
  `device_sn` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '设备序列号',
  `device_name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `status` int NULL DEFAULT 0 COMMENT '0未激活 1在线 2离线',
  `activate_time` datetime NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_device
-- ----------------------------
INSERT INTO `iot_device` VALUES (1, 1, 'LIGHTSN001', '客厅主灯', 1, '2026-02-25 00:10:50', '2026-02-25 00:10:50', NULL);
INSERT INTO `iot_device` VALUES (2, 1, 'LIGHTSN002', '卧室灯', 1, '2026-02-25 00:10:50', '2026-02-25 00:10:50', NULL);
INSERT INTO `iot_device` VALUES (3, 2, 'SENSORSN001', '客厅温湿度', 1, '2026-02-25 00:10:50', '2026-02-25 00:10:50', NULL);

-- ----------------------------
-- Table structure for iot_device_binding
-- ----------------------------
DROP TABLE IF EXISTS `iot_device_binding`;
CREATE TABLE `iot_device_binding`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `device_id` bigint NOT NULL COMMENT '设备ID',
  `binding_time` datetime NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1 COMMENT '0解绑 1已绑定',
  `remark` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_device_binding
-- ----------------------------
INSERT INTO `iot_device_binding` VALUES (1, 1, 1, '2026-02-25 00:10:50', 1, NULL, '2026-02-25 00:10:50', NULL);
INSERT INTO `iot_device_binding` VALUES (2, 1, 3, '2026-02-25 00:10:50', 1, NULL, '2026-02-25 00:10:50', NULL);

-- ----------------------------
-- Table structure for iot_product
-- ----------------------------
DROP TABLE IF EXISTS `iot_product`;
CREATE TABLE `iot_product`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_key` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '产品唯一标识',
  `product_name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '产品名称',
  `category` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '设备分类',
  `redis_prefix` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'redis前缀',
  `description` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_product
-- ----------------------------
INSERT INTO `iot_product` VALUES (1, 'LIGHT_V1', '智能灯V1', 'light', 'iot:light:', '支持开关和亮度调节', '2026-02-25 00:10:50', NULL);
INSERT INTO `iot_product` VALUES (2, 'SENSOR_TH_V1', '温湿度传感器V1', 'sensor', 'iot:sensor:', '支持温度湿度检测', '2026-02-25 00:10:50', NULL);

-- ----------------------------
-- Table structure for iot_product_property
-- ----------------------------
DROP TABLE IF EXISTS `iot_product_property`;
CREATE TABLE `iot_product_property`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NOT NULL COMMENT '产品ID',
  `property_code` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '属性编码',
  `property_name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '属性名称',
  `data_type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '数据类型 int string float bool',
  `unit` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '单位',
  `is_required` int NULL DEFAULT 0 COMMENT '是否必填',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_product_property
-- ----------------------------
INSERT INTO `iot_product_property` VALUES (1, 1, 'power', '开关', 'bool', NULL, 1, '2026-02-25 00:10:50', NULL);
INSERT INTO `iot_product_property` VALUES (2, 1, 'brightness', '亮度', 'int', '%', 1, '2026-02-25 00:10:50', NULL);
INSERT INTO `iot_product_property` VALUES (3, 2, 'power', '开关', 'bool', NULL, 1, '2026-02-25 00:10:50', NULL);
INSERT INTO `iot_product_property` VALUES (4, 2, 'temperature', '温度', 'float', '℃', 1, '2026-02-25 00:10:50', NULL);
INSERT INTO `iot_product_property` VALUES (5, 2, 'humidity', '湿度', 'float', '%', 1, '2026-02-25 00:10:50', NULL);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名',
  `phone` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `password` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
  `status` int NULL DEFAULT 1 COMMENT '状态 0禁用 1正常',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'kai', '13800000000', '123456', 1, '2026-02-25 00:10:50', NULL);

SET FOREIGN_KEY_CHECKS = 1;
