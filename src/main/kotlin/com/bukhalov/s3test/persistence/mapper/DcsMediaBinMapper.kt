package com.bukhalov.s3test.persistence.mapper

import com.bukhalov.s3test.model.DcsMediaBin
import com.bukhalov.s3test.persistence.dynamic.model.DcsMediaBin_.Companion.dcsMediaBin_
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.ResultMap
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.SelectProvider
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider
import org.mybatis.dynamic.sql.util.SqlProviderAdapter
import org.mybatis.dynamic.sql.util.kotlin.SelectCompleter
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.selectList
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.selectOne
import org.springframework.stereotype.Repository

@Mapper
@Repository
interface DcsMediaBinMapper {
    /**
     * Поиск DcsMediaBin.
     *
     * @param selectStatement Параметры запроса.
     * @return List DcsMediaBin.
     */
    @SelectProvider(type = SqlProviderAdapter::class, method = "select")
    @Results(
        id = "dscMediaMapBin",
        value = [
            Result(property = "assetVersion", column = "ASSET_VERSION"),
            Result(property = "mediaId", column = "MEDIA_ID"),
            Result(property = "length", column = "LENGTH"),
            Result(property = "lastModified", column = "LAST_MODIFIED"),
            Result(property = "data", column = "DATA")
        ]
    )
    fun findListBy(selectStatement: SelectStatementProvider): List<DcsMediaBin>

    /**
     * Поиск DcsMediaBin.
     *
     * @param selectStatement Параметры запроса.
     * @return DcsMediaBin.
     */
    @SelectProvider(type = SqlProviderAdapter::class, method = "select")
    @ResultMap("dscMediaMapBin")
    fun selectMediaBin(selectStatement: SelectStatementProvider): DcsMediaBin?
}

fun DcsMediaBinMapper.selectMany(completer: SelectCompleter) =
    selectList(this::findListBy, dcsMediaBin_.all, dcsMediaBin_, completer)

fun DcsMediaBinMapper.findBy(completer: SelectCompleter) =
    selectOne(this::selectMediaBin, dcsMediaBin_.all, dcsMediaBin_, completer)
