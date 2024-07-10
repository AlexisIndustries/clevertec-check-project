package ru.clevertec.check.controllers;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.clevertec.check.dao.CheckRequestDao;
import ru.clevertec.check.dao.ProductQuantityPair;
import ru.clevertec.check.models.Error;
import ru.clevertec.check.models.TransactionInfo;

@Path("/check")
public class CheckResource {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postCheckRequest(CheckRequestDao checkRequestDao) {
        System.out.println(checkRequestDao);
        TransactionInfo transactionInfo = TransactionInfo.builder()
                .error(Error.NO_ERROR)
                .balanceDebitCard(checkRequestDao.getBalanceDebitCard())
                .discountCard(checkRequestDao.getDiscountCard())
                .build();

        for (ProductQuantityPair pair : checkRequestDao.getProducts()) {
            transactionInfo.addIdQuantityPair(pair.getId(), pair.getQuantity());
        }


    }
}
