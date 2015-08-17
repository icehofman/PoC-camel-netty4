@Grab('org.apache.camel:camel-core:2.15.1')
@Grab('org.apache.camel:camel-netty4:2.15.1')
@Grab('org.apache.camel:camel-cxf:2.15.1')

import org.apache.camel.CamelContext
import org.apache.camel.impl.DefaultCamelContext
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.model.RouteDefinition

def camelContext = new DefaultCamelContext()

camelContext.addRoutes(new RouteBuilder() {
    @Override
    public void configure() throws Exception {
      from("netty4:tcp://localhost:7000?sync=true&allowDefaultCodec=false&encoder=#stringEncoder&decoder=#stringDecoder")
        .to("bean:echoService")
      from("netty4:tcp://localhost:7000?sync=true&clientMode=true")  
    }
})

camelContext.start()


class EchoService {
  String sayHello(String guestName) {
    System.out.println("Input guestName : "+ guestName)
    return "Hello " + guestName
}
