
package Logica.WebServer;
//imports servidor web
import jakarta.xml.ws.Service;
import java.net.MalformedURLException;
import javax.xml.namespace.QName;
import java.net.URL;
/**
 *
 * @author mateo
 */
public class Server {
    URL url;
    QName qname = new QName("http://WebServer/", "LabServiceImplService");
    public Server() throws MalformedURLException{
        url = new URL("http://localhost:8181/ws/lab?wsdl");
    }
    public Service getService(){
        return Service.create(url, qname);
    }
    public LabService getPortLabService(Service service){
        return service.getPort(LabService.class);
    }
}
