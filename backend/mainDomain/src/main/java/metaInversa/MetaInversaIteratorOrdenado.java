package metaInversa;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class MetaInversaIteratorOrdenado implements Iterator<MetaInversa> {

    private final List<MetaInversa> metasOrdenadas;
    private int index = 0;

    public MetaInversaIteratorOrdenado(List<MetaInversa> metas) {
        this.metasOrdenadas = new ArrayList<>(metas);
        this.metasOrdenadas.sort(Comparator.comparing(MetaInversa::getDataLimite));
    }

    @Override
    public boolean hasNext() {
        return index < metasOrdenadas.size();
    }

    @Override
    public MetaInversa next() {
        return metasOrdenadas.get(index++);
    }
}
