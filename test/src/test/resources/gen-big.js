var s = '';
for (var i = 0; i < 70; ++i) {
    s += '    ';
    s += 'private String f' + i + ';\n';
}
s += '\n';
s += '    public void doSet(BitSet bitset) {\n';
for (var i = 0; i < 70; ++i) {
    s += '        if (bitset.get(' + i + ')) {\n' +
        '            this.setF' + i + '(null);\n' +
        '        }\n'
}
s += '    }\n';
s += '\n';
s += '    public void doUnset(BitSet bitset) {\n';
for (var i = 0; i < 70; ++i) {
    s += '        if (bitset.get(' + i + ')) {\n' +
        '            PojoAgent.unsetField(this.getF' + i + '());\n' +
        '        }\n'
}
s += '    }\n';
s += '\n';
s += '    public void doUnsetByField(BitSet bitset) {\n';
for (var i = 0; i < 70; ++i) {
    s += '        if (bitset.get(' + i + ')) {\n' +
        '            PojoAgent.unsetField(this.f' + i + ');\n' +
        '        }\n'
}
s += '    }\n';
s += '\n';
s += '    public void doAssert(BitSet bitset) {\n';
for (var i = 0; i < 70; ++i) {
    s += '        if (bitset.get(' + i + ')) {\n' +
        '            assertTrue(PojoAgent.fieldIsSet(this.getF' + i + '()));\n' +
        '        } else {\n' +
        '            assertFalse(PojoAgent.fieldIsSet(this.getF' + i + '()));\n' +
        '        }\n'
}
s += '    }\n';
s += '\n';
s += '    public void doAssertByField(BitSet bitset) {\n';
for (var i = 0; i < 70; ++i) {
    s += '        if (bitset.get(' + i + ')) {\n' +
        '            assertTrue(PojoAgent.fieldIsSet(this.f' + i + '));\n' +
        '        } else {\n' +
        '            assertFalse(PojoAgent.fieldIsSet(this.f' + i + '));\n' +
        '        }\n'
}
s += '    }\n';

console.log(s);
