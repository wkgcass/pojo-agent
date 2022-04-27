package my.test;

import io.vproxy.pojoagent.api.*;
import io.vproxy.pojoagent.api.template.PojoUpdateFrom;
import io.vproxy.pojoagent.api.template.PojoValidate;

import java.util.Arrays;

@Pojo
public class Bean implements PojoUpdateFrom<Bean>, PojoValidate {
    @MustNotExist
    private int id;
    @MustExist(CommonActions.CREATE)
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
        throw new RequirePojoAutoImplException();
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

    @PojoAutoImpl
    @Override
    public ValidationResult validate(int action) {
        throw new RequirePojoAutoImplException();
    }

    @Override
    public void preValidate(int action) {
        System.out.println("pre validate is called");
    }

    @Override
    public ValidationResult postValidate(ValidationResult result) {
        System.out.println("post validate is called");
        return result;
    }
}
