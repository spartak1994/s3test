package com.bukhalov.s3test.persistence.mapper

import com.bukhalov.s3test.model.DcsMediaExt
import com.bukhalov.s3test.persistence.dynamic.model.DcsMediaExt_.Companion.dcsMediaExt_
import org.apache.ibatis.annotations.InsertProvider
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.ResultMap
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.SelectProvider
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider
import org.mybatis.dynamic.sql.util.SqlProviderAdapter
import org.mybatis.dynamic.sql.util.kotlin.SelectCompleter
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.insert
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.selectList
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.selectOne
import org.springframework.stereotype.Repository

@Mapper
@Repository
interface DcsMediaExtMapper {
    /**
     * Поиск DcsMediaExt.
     *
     * @param selectStatement Параметры запроса.
     * @return List DcsMediaExt.
     */
    @SelectProvider(type = SqlProviderAdapter::class, method = "select")
    @Results(
        id = "dscMediaMapExt",
        value = [
            Result(property = "assetVersion", column = "ASSET_VERSION"),
            Result(property = "mediaId", column = "MEDIA_ID"),
            Result(property = "length", column = "LENGTH"),
            Result(property = "lastModified", column = "LAST_MODIFIED"),
            Result(property = "data", column = "DATA")
        ]
    )
    fun findListBy(selectStatement: SelectStatementProvider): List<DcsMediaExt>

    /**
     * Поиск DcsMediaExt.
     *
     * @param selectStatement Параметры запроса.
     * @return DcsMediaExt.
     */
    @SelectProvider(type = SqlProviderAdapter::class, method = "select")
    @ResultMap("dscMediaMapExt")
    fun selectMediaExt(selectStatement: SelectStatementProvider): DcsMediaExt?

    /**
     * Создание DcsMediaExt.
     *
     * @param insertStatement DcsMediaExt
     * @return DcsMediaExt
     */
    @InsertProvider(type = SqlProviderAdapter::class, method = "insert")
    fun insert(insertStatement: InsertStatementProvider<DcsMediaExt>): Int
}

fun DcsMediaExtMapper.selectMany(completer: SelectCompleter) =
    selectList(this::findListBy, dcsMediaExt_.all, dcsMediaExt_, completer)

fun DcsMediaExtMapper.findBy(completer: SelectCompleter) =
    selectOne(this::selectMediaExt, dcsMediaExt_.all, dcsMediaExt_, completer)

fun DcsMediaExtMapper.insert(row: DcsMediaExt) =
    insert(this::insert, row, dcsMediaExt_) {
        map(dcsMediaExt_.assetVersion).toProperty("assetVersion")
        map(dcsMediaExt_.mediaId).toProperty("mediaId")
        map(dcsMediaExt_.url).toProperty("url")
    }
