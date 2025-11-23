package metaInversa;

import java.util.Iterator;
import java.util.List;

public class ColecaoMetaInversa implements Iterable<MetaInversa> {

    private final List<MetaInversa> metas;

    public ColecaoMetaInversa(List<MetaInversa> metas) {
        this.metas = metas;
    }

    @Override
    public Iterator<MetaInversa> iterator() {
        return new MetaInversaIteratorOrdenado(metas);
    }
}