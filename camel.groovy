@Grab('org.apache.camel:camel-core:2.15.1')
@Grab('org.apache.camel:camel-netty4:2.15.1')
@Grab('org.apache.camel:camel-cxf:2.15.1')

import org.apache.camel.CamelContext
import org.apache.camel.impl.DefaultCamelContext
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.model.RouteDefinition

def body = 'Kermit'

def camelContext = new DefaultCamelContext()

camelContext.addRoutes(new RouteBuilder() {
    @Override
    public void configure() throws Exception {
        from("netty4:tcp://localhost:7666?textline=true")
                .transform(simple("Hello ${body}"))
                .to("direct:end")
    }
})

camelContext.start()

def pollingConsumer = camelContext.getEndpoint("direct:end").createPollingConsumer()
pollingConsumer.start()

def socket = new Socket("localhost", 7666)
def out = new PrintWriter(socket.getOutputStream(), true)

try {
    out.write("Kermit\n")
} finally {
    out.close()
    socket.close()
}

String result = pollingConsumer.receive().getIn().getBody(String.class)

assert "Hello Kermit" == result

camelContext.stop()
