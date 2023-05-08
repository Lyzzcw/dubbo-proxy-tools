/*
 Navicat Premium Data Transfer

 Source Server         : local_mysql
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : localhost:3306
 Source Schema         : dubbo-proxy-tools

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 08/05/2023 16:20:04
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_dubbo_invoke_req_record
-- ----------------------------
DROP TABLE IF EXISTS `t_dubbo_invoke_req_record`;
CREATE TABLE `t_dubbo_invoke_req_record`  (
                                              `id` int NOT NULL AUTO_INCREMENT,
                                              `user_id` int NULL DEFAULT NULL COMMENT '用户id',
                                              `arg_json` varchar(5000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'dubbo请求参数',
                                              `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
                                              `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_login_status
-- ----------------------------
DROP TABLE IF EXISTS `t_login_status`;
CREATE TABLE `t_login_status`  (
                                   `id` int NOT NULL AUTO_INCREMENT,
                                   `user_id` int NULL DEFAULT NULL COMMENT '用户id',
                                   `user_token` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '用户token',
                                   `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                   PRIMARY KEY (`id`) USING BTREE,
                                   INDEX `idx_user_token`(`user_token`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_register_config
-- ----------------------------
DROP TABLE IF EXISTS `t_register_config`;
CREATE TABLE `t_register_config`  (
                                      `id` int NOT NULL AUTO_INCREMENT,
                                      `host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'host地址',
                                      `ip` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '注册中心真实ip',
                                      `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
                                      `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                      `type` smallint NULL DEFAULT NULL COMMENT '注册中心类型：1 zk,2 nacos',
                                      PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
                           `id` int NOT NULL AUTO_INCREMENT,
                           `username` varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
                           `password` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
                           `createTime` datetime NULL DEFAULT CURRENT_TIMESTAMP,
                           `updateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
