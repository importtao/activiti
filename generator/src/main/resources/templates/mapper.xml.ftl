<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE  PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package.Mapper}.${table.mapperName}">
    <!-- 自定义sql -->


    <#--<#if enableCache>
        <!-- 开启二级缓存 &ndash;&gt;
        <cache type="org.mybatis.caches.ehcache.LoggingEhcache"/>

    </#if>-->
<#if baseResultMap>
    <!-- 通用查询映射结果 -->
    <resultMap id="BaseResultMap" type="${package.Entity}.${entity}">
<#list table.fields as field>
<#if field.keyFlag><#--生成主键排在第一位-->
        <id column="${field.name}" property="${field.propertyName}" />
</#if>
</#list>
<#list table.commonFields as field><#--生成公共字段 -->
    <result column="${field.name}" property="${field.propertyName}" />
</#list>
<#list table.fields as field>
<#if !field.keyFlag><#--生成普通字段 -->
        <result column="${field.name}" property="${field.propertyName}" />
</#if>
</#list>
    </resultMap>

</#if>
<#if baseColumnList>
    <!-- 通用查询结果列 -->
    <sql id="Base_Column_List">
<#list table.commonFields as field>
        ${field.name},
</#list>
        ${table.fieldNames}
    </sql>

</#if>
    <!-- 自定义sql -->
    <update id="logicDeleteByPrimaryKeyList">
        update ${table.name} set deleted=1 where id in
        <foreach collection="list" item="item" open="(" separator="," close=")">
            #{item}
        </foreach>
    </update>
    <select id="selectByAIdList" resultType="${package.Entity}.${entity}">
        select * from ${table.name} where deleted=0 and aid in
        <foreach collection="list" item="item" open="(" separator="," close=")">
            #{item}
        </foreach>
    </select>
</mapper>
