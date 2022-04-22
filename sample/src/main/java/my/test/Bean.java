package my.test;

import io.vproxy.pojoagent.api.*;
import io.vproxy.pojoagent.api.template.PojoUpdateFrom;

import java.util.Arrays;

@Pojo
public class Bean implements PojoUpdateFrom<Bean> {
    private int id;
    private String name;
    private boolean admin;
    private String[] array;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String[] getArray() {
        return array;
    }

    public void setArray(String[] array) {
        this.array = array;
    }

    @PojoAutoImpl
    @Override
    public void updateFrom(Bean another) {
        throw new RequirePojoAgentException();
    }

    @PojoCaller
    @Override
    public void preUpdateFrom(Bean another) {
        System.out.println("pre updateFrom is called");
        System.out.println("unsetting another.name");
        PojoAgent.unsetField(another.getName());
    }

    @Override
    public void postUpdateFrom(Bean another) {
        System.out.println("post updateFrom is called");
    }

    @Override
    public String toString() {
        return "Bean{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", admin=" + admin +
            ", array=" + Arrays.toString(array) +
            '}';
    }
}
