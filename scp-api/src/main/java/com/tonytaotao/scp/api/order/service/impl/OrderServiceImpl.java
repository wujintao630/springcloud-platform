package com.tonytaotao.scp.api.order.service.impl;

import com.tonytaotao.scp.api.invoke.BusinessFeign;
import com.tonytaotao.scp.api.order.vo.PlaceOrderReq;
import com.tonytaotao.scp.api.order.service.OrderService;
import com.tonytaotao.scp.common.vo.CommodityStorageVO;
import com.tonytaotao.scp.common.vo.UserAccountVO;
import com.tonytaotao.scp.common.base.GlobalResult;
import com.tonytaotao.scp.common.vo.UserOrderVO;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务接口实现类
 * </p>
 *
 * @author tonytaotao
 * @since 2019-10-25
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private BusinessFeign businessFeign;

    @GlobalTransactional
    @Override
    public GlobalResult<String> placeOrder(PlaceOrderReq placeOrderReq) {


        UserOrderVO userOrderVO = new UserOrderVO();
        userOrderVO.setUserId(placeOrderReq.getUserId());
        userOrderVO.setCommodityId(placeOrderReq.getCommodityId());
        userOrderVO.setQuantity(placeOrderReq.getQuantity());
        userOrderVO.setTotalMoney(placeOrderReq.getTotalMoney());
        businessFeign.addUserOrder(userOrderVO);

        UserAccountVO userAccountVO = new UserAccountVO();
        userAccountVO.setId(1L);
        userAccountVO.setBalance(placeOrderReq.getTotalMoney());
        GlobalResult<String> subUserAccountBalanceResult = businessFeign.subUserAccountBalance(userAccountVO);
        if (subUserAccountBalanceResult.isFailure()) {
            throw new RuntimeException(subUserAccountBalanceResult.getMsg());
        }

        CommodityStorageVO commodityStorageVO = new CommodityStorageVO();
        commodityStorageVO.setId(1L);
        commodityStorageVO.setStorageAmount(placeOrderReq.getQuantity());

        return GlobalResult.DefaultSuccess();

    }
}
