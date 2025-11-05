package com.fluxo.controllers;

import cartao.Cartao;
import cartao.CartaoId;
import cartao.CartaoNumero;
import cartao.Cvv;
import com.fluxo.controllers.cartao.CartaoDTO;
import com.fluxo.controllers.conta.ContaDTO;
import conta.Conta;
import conta.ContaId;
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
                cartaoDTO.usuarioId = cartao.getUsuarioId();
                return cartaoDTO;
            }
        });

        addConverter(new AbstractConverter<CartaoDTO, Cartao>() {
            @Override
            protected Cartao convert(CartaoDTO cartao) {
                return new Cartao(new CartaoNumero(cartao.numero), cartao.titular, cartao.validade, new Cvv(cartao.cvv), cartao.limite, LocalDate.parse(cartao.dataFechamentoFatura), LocalDate.parse(cartao.dataVencimentoFatura), cartao.saldo, cartao.usuarioId);
            }
        });

        addConverter(new AbstractConverter<Conta, ContaDTO>() {
            @Override
            protected ContaDTO convert(Conta conta) {
                ContaDTO dto = new ContaDTO();
                dto.id = conta.getId().getId();
                dto.nome = conta.getNome();
                dto.tipo = conta.getTipo();
                dto.banco = conta.getBanco();
                dto.saldo = conta.getSaldo();
                dto.usuarioId = conta.getUsuarioId();
                return dto;
            }
        });

        addConverter(new AbstractConverter<ContaDTO, Conta>() {
            @Override
            protected Conta convert(ContaDTO dto) {
                return new Conta(
                        new ContaId(dto.id),
                        dto.nome,
                        dto.tipo,
                        dto.banco,
                        dto.saldo,
                        dto.usuarioId
                );
            }
        });
    }
}
