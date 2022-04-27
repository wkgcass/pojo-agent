package my.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vproxy.pojoagent.api.PojoAgent;
import io.vproxy.pojoagent.api.PojoCaller;

public class Main {
    @PojoCaller
    public static void main(String[] args) throws Exception {
        PojoAgent.ensurePojoAgent();

        ObjectMapper om = new ObjectMapper();

        Bean bean = new Bean();
        System.out.println("new bean: id is set?    " + PojoAgent.fieldIsSet(bean.getId()));
        System.out.println("new bean: name is set?  " + PojoAgent.fieldIsSet(bean.getName()));
        System.out.println("new bean: admin is set? " + PojoAgent.fieldIsSet(bean.isAdmin()));
        System.out.println("new bean: array is set? " + PojoAgent.fieldIsSet(bean.getArray()));
        bean.setId(123);
        bean.setName(null);
        bean.setAdmin(false);
        bean.setArray(null);
        System.out.println("call setters on bean: id is set?    " + PojoAgent.fieldIsSet(bean.getId()));
        System.out.println("call setters on bean: name is set?  " + PojoAgent.fieldIsSet(bean.getName()));
        System.out.println("call setters on bean: admin is set? " + PojoAgent.fieldIsSet(bean.isAdmin()));
        System.out.println("call setters on bean: array is set? " + PojoAgent.fieldIsSet(bean.getArray()));

        PojoAgent.unsetField(bean.getId());
        PojoAgent.unsetField(bean.getName());
        PojoAgent.unsetField(bean.isAdmin());
        PojoAgent.unsetField(bean.getArray());
        System.out.println("unset: id is set?    " + PojoAgent.fieldIsSet(bean.getId()));
        System.out.println("unset: name is set?  " + PojoAgent.fieldIsSet(bean.getName()));
        System.out.println("unset: admin is set? " + PojoAgent.fieldIsSet(bean.isAdmin()));
        System.out.println("unset: array is set? " + PojoAgent.fieldIsSet(bean.getArray()));

        String serialized = om.writeValueAsString(bean);
        System.out.println("serialize with jackson: " + serialized);

        Bean newBean = om.readValue(serialized, new TypeReference<Bean>() {
        });
        System.out.println("deserialize with jackson: id is set?    " + PojoAgent.fieldIsSet(newBean.getId()));
        System.out.println("deserialize with jackson: name is set?  " + PojoAgent.fieldIsSet(newBean.getName()));
        System.out.println("deserialize with jackson: admin is set? " + PojoAgent.fieldIsSet(newBean.isAdmin()));
        System.out.println("deserialize with jackson: array is set? " + PojoAgent.fieldIsSet(newBean.getArray()));

        String partialJson = "{\"id\":123}";
        System.out.println("partial json: " + partialJson);
        Bean partialBean = om.readValue(partialJson, new TypeReference<Bean>() {
        });
        System.out.println("partial json: id is set?    " + PojoAgent.fieldIsSet(partialBean.getId()));
        System.out.println("partial json: name is set?  " + PojoAgent.fieldIsSet(partialBean.getName()));
        System.out.println("partial json: admin is set? " + PojoAgent.fieldIsSet(partialBean.isAdmin()));
        System.out.println("partial json: array is set? " + PojoAgent.fieldIsSet(partialBean.getArray()));

        bean = new Bean();
        bean.setId(222);
        bean.setName("wkgcass");
        bean.setAdmin(true);
        bean.setArray(new String[]{"test"});

        newBean = new Bean();
        newBean.setName("modified");
        newBean.setAdmin(false);

        System.out.println("original bean: " + bean);
        System.out.println("input bean     " + newBean);
        bean.updateFrom(newBean);
        System.out.println("current bean:  " + bean);

        System.out.println("bean.validate: " + bean.validate());
    }
}
