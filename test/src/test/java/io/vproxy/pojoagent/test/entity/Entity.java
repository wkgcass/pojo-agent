package io.vproxy.pojoagent.test.entity;

import io.vproxy.pojoagent.api.template.PojoSetAllFields;
import io.vproxy.pojoagent.api.template.PojoUnsetAllFields;

import java.util.BitSet;

public interface Entity extends PojoSetAllFields, PojoUnsetAllFields {
    void doSet(BitSet bitset);

    void doSet2(BitSet bitset);

    void doUnset(BitSet bitset);

    void doUnsetByField(BitSet bitset);

    void doAssert(BitSet bitset);

    void doAssertByField(BitSet bitset);
}
