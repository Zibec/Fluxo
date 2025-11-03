package com.fluxo.controllers;

import cartao.Cartao;
import cartao.CartaoNumero;
import cartao.Cvv;
import com.fluxo.controllers.cartao.CartaoDTO;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ControllerMapper extends ModelMapper {

    public ControllerMapper() {
        var config = getConfiguration();
        config.setFieldMatchingEnabled(true);
        config.setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);

        addConverter(new AbstractConverter<Cartao, CartaoDTO>() {
            @Override
            protected CartaoDTO convert(Cartao cartao) {
                CartaoDTO cartaoDTO = new CartaoDTO();
                cartaoDTO.id = cartao.getId().getId();
                cartaoDTO.titular = cartao.getTitular();
                cartaoDTO.numero = cartao.getNumero().getCodigo();
                cartaoDTO.cvv  = cartao.getCvv().getCodigo();
                cartaoDTO.validade = cartao.getValidade();
                cartaoDTO.limite =  cartao.getLimite();
                cartaoDTO.saldo =  cartao.getSaldo();
                cartaoDTO.dataFechamentoFatura = cartao.getDataFechamentoFatura().toString();
                cartaoDTO.dataVencimentoFatura = cartao.getDataVencimentoFatura().toString();
                return cartaoDTO;
            }
        });

        addConverter(new AbstractConverter<CartaoDTO, Cartao>() {
            @Override
            protected Cartao convert(CartaoDTO cartao) {
                return new Cartao(new CartaoNumero(cartao.numero), cartao.titular, cartao.validade, new Cvv(cartao.cvv), cartao.limite, LocalDate.parse(cartao.dataFechamentoFatura), LocalDate.parse(cartao.dataVencimentoFatura));
            }
        });
    }
}
