package my.test.kt

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.vproxy.pojoagent.api.Pojo
import io.vproxy.pojoagent.api.PojoAgent
import io.vproxy.pojoagent.api.PojoCaller

object Main {
  @JvmStatic
  @PojoCaller
  fun main(args: Array<String>) {
    val om = ObjectMapper()

    val bean = Bean()
    println("new bean: id is set?    ${PojoAgent.fieldIsSet(bean.id)}")
    println("new bean: name is set?  ${PojoAgent.fieldIsSet(bean.name)}")
    println("new bean: admin is set? ${PojoAgent.fieldIsSet(bean.admin)}")
    bean.id = 123
    bean.name = "my-test"
    bean.admin = true
    println("call setters on bean: id is set?    ${PojoAgent.fieldIsSet(bean.id)}")
    println("call setters on bean: name is set?  ${PojoAgent.fieldIsSet(bean.name)}")
    println("call setters on bean: admin is set? ${PojoAgent.fieldIsSet(bean.admin)}")

    val serialized = om.writeValueAsString(bean)
    println("serialize with jackson: $serialized")

    val newBean = om.readValue<Bean>(serialized, object : TypeReference<Bean>() {})
    println("deserialize with jackson: id is set?    " + PojoAgent.fieldIsSet(newBean.id))
    println("deserialize with jackson: name is set?  " + PojoAgent.fieldIsSet(newBean.name))
    println("deserialize with jackson: admin is set? " + PojoAgent.fieldIsSet(newBean.admin))

    val partialJson = "{\"id\":123}"
    println("partial json: $partialJson")
    val partialBean = om.readValue<Bean>(partialJson, object : TypeReference<Bean>() {})
    println("partial json: id is set?    " + PojoAgent.fieldIsSet(partialBean.id))
    println("partial json: name is set?  " + PojoAgent.fieldIsSet(partialBean.name))
    println("partial json: admin is set? " + PojoAgent.fieldIsSet(partialBean.admin))
  }
}

@Pojo
data class Bean(
  var id: Int = 0,
  var name: String = "",
  var admin: Boolean = false,
)
