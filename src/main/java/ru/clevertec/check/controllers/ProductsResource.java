package ru.clevertec.check.controllers;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import ru.clevertec.check.models.Error;
import ru.clevertec.check.models.TransactionInfo;

@Path("/products")
public class ProductsResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public TransactionInfo hello() {
        return TransactionInfo.builder()
                .error(Error.INTERNAL_SERVER_ERROR)
                .build();
    }
}