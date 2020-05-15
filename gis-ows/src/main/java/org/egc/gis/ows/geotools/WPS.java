package org.egc.gis.ows.geotools;

import net.opengis.wps10.*;
import org.eclipse.emf.common.util.EList;
import org.geotools.data.ows.SimpleHttpClient;
import org.geotools.data.wps.WPSFactory;
import org.geotools.data.wps.WebProcessingService;
import org.geotools.data.wps.request.DescribeProcessRequest;
import org.geotools.data.wps.response.DescribeProcessResponse;
import org.geotools.ows.ServiceException;
import org.geotools.process.Process;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

/**
 * Description:
 * <pre>
 * http://docs.geotools.org/stable/userguide/unsupported/wps.html
 * </pre>
 *
 * @author houzhiwei
 * @date 2020/1/12 19:59
 */
public class WPS {

    public void getCapabilties() throws IOException, ServiceException {
        URL url = new URL("http://localhost:8080/geoserver/ows?service=WPS&request=GetCapabilities");
        WebProcessingService wps = new WebProcessingService(url);
        WPSCapabilitiesType capabilities = wps.getCapabilities();
        // view a list of processes offered by the server
        ProcessOfferingsType processOfferings = capabilities.getProcessOfferings();
        EList processes = processOfferings.getProcess();
    }

    public WPSFactory createWPSFactory() throws IOException, ServiceException {
        URL url = new URL("http://localhost:8080/geoserver/ows?service=WPS&request=DescribeProcess");
        SimpleHttpClient client = new SimpleHttpClient();
        WebProcessingService wps = new WebProcessingService(url, client, null);
        // create a WebProcessingService as shown above, then do a full describeprocess on my process
        DescribeProcessRequest descRequest = wps.createDescribeProcessRequest();
        descRequest.setIdentifier("DoubleAddition"); // describe the double addition process

        // send the request and get the ProcessDescriptionType bean to create a WPSFactory
        DescribeProcessResponse descResponse = wps.issueRequest(descRequest);
        ProcessDescriptionsType processDesc = descResponse.getProcessDesc();
        ProcessDescriptionType pdt = (ProcessDescriptionType) processDesc.getProcessDescription().get(0);
        WPSFactory wpsfactory = new WPSFactory(pdt, url);
        return wpsfactory;
    }

    public void describeProcess() throws IOException, ServiceException {
        WPSFactory wpsfactory = createWPSFactory();
        // create a process
        Process process = wpsfactory.create();
    }

    public void wpsExecute() throws IOException, ServiceException {
        WPSFactory wpsfactory = createWPSFactory();
        // create a WebProcessingService, WPSFactory and WPSProcess as shown above and execute it
        Process process = wpsfactory.create();
        // setup the inputs
        Map<String, Object> map = new TreeMap<String, Object>();
        Double d1 = 77.5;
        Double d2 = 22.3;
        map.put("input_a", d1);
        map.put("input_b", d2);

        // you could validate your inputs against what the process expected by checking
        // your map against the Parameters in wpsfactory.getParameterInfo(), but
        // to keep this simple let's just try sending the request without validation
        Map<String, Object> results = process.execute(map, null);
        Double result = (Double) results.get("result");
    }

    /*public void getExecutionResponse() throws IOException, ServiceException {
        URL url = new URL("http://localhost:8080/geoserver/ows?service=WPS&request=DescribeProcess");
        SimpleHttpClient client = new SimpleHttpClient();
        WebProcessingService wps = new WebProcessingService(url, client, null);

        GetExecutionStatusRequest execRequest = wps.createGetExecutionStatusRequest();
        execRequest.setIdentifier(this.wpsProcessIdentifier);

        GetExecutionStatusResponse response = wps.issueRequest(execRequest);

        // Checking for Exceptions and Status...
        if ((response.getExceptionResponse() == null) && (response.getExecuteResponse() != null)) {
            if (response.getExecuteResponse().getStatus().getProcessSucceeded() != null) {
                // Process complete ... checking output
                for (Object processOutput : response.getExecuteResponse().getProcessOutputs().getOutput()) {
                    OutputDataType wpsOutput = (OutputDataType) processOutput;
                    // retrieve the value of the output ...
                    wpsOutput.getData().getLiteralData().getValue();
                }
            } else if (response.getExecuteResponse().getStatus().getProcessFailed() != null) {
                // Process failed ... handle failed status
            } else if (response.getExecuteResponse().getStatus().getProcessStarted() != null) {
                // Updating status percentage...
                int percentComplete =
                        response.getExecuteResponse().getStatus().getProcessStarted().getPercentCompleted().intValue();
            }
        } else {
            // Retrieve here the Exception message and handle the errored status ...
        }
    }*/

}
