package com.zz.core.mybatis;

import com.baomidou.mybatisplus.core.parser.AbstractJsqlParser;
import com.zz.core.auth.ZzUserContextHolder;
import com.zz.core.enums.DataScopeType;
import com.zz.core.vo.AuthResourcesVo;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class DataScopeSqlParser extends AbstractJsqlParser {
    @Override
    public void processInsert(Insert insert) {

    }

    @Override
    public void processDelete(Delete delete) {

    }

    @Override
    public void processUpdate(Update update) {

    }

    /**
     * select 语句处理 ,开始注入数据过滤逻辑
     * @param selectBody
     */
    @Override
    public void processSelectBody(SelectBody selectBody) {
        if (selectBody instanceof PlainSelect) {
            this.processPlainSelect((PlainSelect)selectBody);
        } else if (selectBody instanceof WithItem) {
            WithItem withItem = (WithItem)selectBody;
            if (withItem.getSelectBody() != null) {
                this.processSelectBody(withItem.getSelectBody());
            }
        } else {
            SetOperationList operationList = (SetOperationList)selectBody;
            if (operationList.getSelects() != null && operationList.getSelects().size() > 0) {
                operationList.getSelects().forEach(this::processSelectBody);
            }
        }
        // 清除参数
        DataScopeContextHolder.clear();
    }

    protected void processPlainSelect(PlainSelect plainSelect) {
        FromItem fromItem = plainSelect.getFromItem();
        if (fromItem instanceof Table) {
            plainSelect.setWhere(builderExpression(plainSelect.getWhere()));
        }
    }

    /**
     *  把原来sql 语句的where 条件和数据字段筛选拼接
     * @param currentExpression
     * @return
     */
    protected Expression builderExpression(Expression currentExpression) {
        // 循环构造数据权限过滤
        Expression dataScopeExpression = null;
        DataScopeConfig dataScopeConfig = DataScopeContextHolder.getDataScopeConfig();
        List<String> filedList = dataScopeConfig.getFilterFiled();
        for (int i = 0; i < filedList.size(); i++){
            final Expression scopeExpression = this.dataScopeCondition(filedList.get(i) , dataScopeConfig.getDataScopeType()[i]);
            if(dataScopeExpression == null){
                dataScopeExpression = scopeExpression;
            } else {
                dataScopeExpression = new AndExpression(dataScopeExpression, scopeExpression);
            }
        }
        if (currentExpression == null) {
            return dataScopeExpression;
        }

        if (currentExpression instanceof BinaryExpression) {
            BinaryExpression binaryExpression = (BinaryExpression) currentExpression;
            doExpression(binaryExpression.getLeftExpression());
            doExpression(binaryExpression.getRightExpression());
        } else if (currentExpression instanceof InExpression) {
            InExpression inExp = (InExpression) currentExpression;
            ItemsList rightItems = inExp.getRightItemsList();
            if (rightItems instanceof SubSelect) {
                processSelectBody(((SubSelect) rightItems).getSelectBody());
            }
        }
        if (currentExpression instanceof OrExpression) {
            return new AndExpression(new Parenthesis(currentExpression), dataScopeExpression);
        } else {
            return new AndExpression(currentExpression, dataScopeExpression);
        }
    }

    protected void doExpression(Expression expression) {
        if (expression instanceof FromItem) {
            processFromItem((FromItem) expression);
        } else if (expression instanceof InExpression) {
            InExpression inExp = (InExpression) expression;
            ItemsList rightItems = inExp.getRightItemsList();
            if (rightItems instanceof SubSelect) {
                processSelectBody(((SubSelect) rightItems).getSelectBody());
            }
        }
    }

    /**
     * 处理子查询等
     */
    protected void processFromItem(FromItem fromItem) {
        if (fromItem instanceof SubJoin) {
            SubJoin subJoin = (SubJoin) fromItem;
            if (subJoin.getJoinList() != null) {
                subJoin.getJoinList().forEach(this::processJoin);
            }
            if (subJoin.getLeft() != null) {
                processFromItem(subJoin.getLeft());
            }
        } else if (fromItem instanceof SubSelect) {
            SubSelect subSelect = (SubSelect) fromItem;
            if (subSelect.getSelectBody() != null) {
                processSelectBody(subSelect.getSelectBody());
            }
        } else if (fromItem instanceof ValuesList) {
            logger.debug("Perform a subquery, if you do not give us feedback");
        } else if (fromItem instanceof LateralSubSelect) {
            LateralSubSelect lateralSubSelect = (LateralSubSelect) fromItem;
            if (lateralSubSelect.getSubSelect() != null) {
                SubSelect subSelect = lateralSubSelect.getSubSelect();
                if (subSelect.getSelectBody() != null) {
                    processSelectBody(subSelect.getSelectBody());
                }
            }
        }
    }

    /**
     * 处理联接语句
     */
    protected void processJoin(Join join) {
        if (join.getRightItem() instanceof Table) {
            join.setOnExpression(builderExpression(join.getOnExpression()));
        }
    }

    private Expression dataScopeCondition(String filed, DataScopeType scopeType) {
        final InExpression inExpression = new InExpression();
        inExpression.setLeftExpression(new Column(filed));
        final ExpressionList itemsList = new ExpressionList();
        final List<Expression> inValues = new ArrayList<>(2);
        // 获取值
        List<AuthResourcesVo> resourcesList = ZzUserContextHolder.getUser().getResourcesList();
        if(!CollectionUtils.isEmpty(resourcesList)){
            // 获取当前用户该scopeType 的权限配置
            List<Expression> resourceIds = resourcesList.stream()
                    .filter(r->r.getType() == 2 && r.getSubType().equals(scopeType.getCode()))
                    .map(r->new StringValue(r.getBizId())).collect(Collectors.toList());
            if(!CollectionUtils.isEmpty(resourceIds)){
                inValues.addAll(resourceIds);
            } else {
                // 如果启用数据权限，但用户又没配置权限，则默认使用0
                inValues.add(new StringValue("0"));
            }
        }
        itemsList.setExpressions(inValues);
        inExpression.setRightItemsList(itemsList);
        return inExpression;
    }
}
