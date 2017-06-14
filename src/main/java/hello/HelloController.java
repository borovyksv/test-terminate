package hello;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String index() {
        return "Greetings from Spring Boot!!!";
    }

    @RequestMapping("/terminate")
    public String terminate() {

        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

        String instanceId = null;
        try {
            URL EC2MetaData = new URL("http://169.254.169.254/latest/meta-data/instance-id");
            URLConnection EC2MD = EC2MetaData.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(EC2MD.getInputStream()));
            instanceId = in.readLine();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TerminateInstancesRequest request = new TerminateInstancesRequest()
                .withInstanceIds(instanceId);

        ec2.terminateInstances(request);
        return "terminated";
    }

}
