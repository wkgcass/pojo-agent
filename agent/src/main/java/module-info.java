module io.vproxy.pojoagent.agent {
    requires transitive java.instrument;
    requires transitive io.vproxy.pojoagent.api;

    exports io.vproxy.pojoagent.agent;
}
