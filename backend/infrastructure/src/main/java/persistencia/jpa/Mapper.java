package persistencia.jpa;

import conta.ContaId;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import persistencia.jpa.cartao.CartaoJpaRepository;
import persistencia.jpa.conta.ContaJpaRepository;

@Component
public class Mapper extends ModelMapper {

    @Autowired
    private CartaoJpaRepository cartaoJpaRepository;

    @Autowired
    private ContaJpaRepository contaJpaRepository;

    Mapper() {
        var config = getConfiguration();

        config.setFieldMatchingEnabled(true);
        config.setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);

        addConverter(new AbstractConverter<, Object>() {
                     }
                var id = map(source.id, ContaId.class)
        );
    }
}
