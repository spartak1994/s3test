package com.bukhalov.s3test.persistence.dynamic

import org.mybatis.dynamic.sql.insert.InsertDSL
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider
import org.mybatis.dynamic.sql.render.RenderingStrategies.MYBATIS3
import org.mybatis.dynamic.sql.select.SelectModel
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider
import org.mybatis.dynamic.sql.util.Buildable

object DynamicSQLUtils {
    /**
     * Create SelectStatementProvider.
     *
     * @param builder Buildable
     * @return SelectStatementProvider
     */
    fun build(builder: Buildable<*>): SelectStatementProvider {
        val selectModel = builder.build() as SelectModel
        return selectModel.render(MYBATIS3)
    }

    /**
     * Create InsertStatementProvider.
     *
     * @param insertDSL InsertDSL
     * @param <T>       Entity
     * @return InsertStatementProvider
     </T> */
    fun <T> build(insertDSL: InsertDSL<T>): InsertStatementProvider<T> {
        val insertModel = insertDSL.build()
        return insertModel.render(MYBATIS3)
    }

//    /**
//     * Create UpdateStatementProvider.
//     *
//     * @param builder UpdateWhereBuilder
//     * @return UpdateStatementProvider
//     */
//    fun <T> build(builder: UpdateWhereBuilder): UpdateStatementProvider {
//        val updateModel = builder.build() as UpdateModel
//        return updateModel.render(MYBATIS3)
//    }
//
//    /**
//     * Create DeleteStatementProvider.
//     *
//     * @param builder DeleteWhereBuilder
//     * @return DeleteStatementProvider
//     */
//    fun build(builder: DeleteWhereBuilder): DeleteStatementProvider {
//        val deleteModel = builder.build() as DeleteModel
//        return deleteModel.render(MYBATIS3)
//    }
}
