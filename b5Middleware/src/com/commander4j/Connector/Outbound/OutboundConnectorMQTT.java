package com.commander4j.Connector.Outbound;

import java.io.File;
import java.io.FileWriter;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.commander4j.Interface.Outbound.OutboundInterface;
import com.commander4j.sys.Common;
import com.commander4j.util.JXMLDocument;
import com.commander4j.util.Utility;

import ABSTRACT.com.commander4j.Connector.OutboundConnectorABSTRACT;

public class OutboundConnectorMQTT extends OutboundConnectorABSTRACT
{

	Logger logger = org.apache.logging.log4j.LogManager.getLogger((OutboundConnectorMQTT.class));

	public OutboundConnectorMQTT(OutboundInterface inter)
	{
		super(Connector_MQTT, inter);
	}

	@Override
	public boolean connectorSave(String path, String prefix, String filename)
	{
		boolean result = false;
		String errorDescription="";

		filename = getOutboundInterface().get83GUIDFilename(prefix, filename);

		String fullPath = path + File.separator + filename;

		logger.debug("connectorSave [" + fullPath + "." + getOutboundInterface().getOutputFileExtension().toLowerCase() + "]");

		try
		{

			if (fullPath.endsWith("." + getType().toLowerCase()) == false)
			{
				fullPath = fullPath + "." + getType().toLowerCase();
			}

			String tempFilename = fullPath + ".tmp";
			String finalFilename = fullPath;

			//////// MQTT Code Start

			MemoryPersistence persistence;
			MqttClient sampleClient;
			MqttConnectOptions connOpts;
			MqttMessage message;
			FileWriter fw = new FileWriter(tempFilename);
			try
			{

				fw.write("MQTT Broker : " + getOutboundInterface().getMQTBroker() + "\n");
				fw.write("MQTT Client : " + getOutboundInterface().getMQTTClient() + "\n");
				fw.write("MQTT Content : " + getOutboundInterface().getMQTTContentXPath() + "\n");
				fw.write("MQTT Topic : " + getOutboundInterface().getMQTTTopic() + "\n");
				fw.write("MQTT QOS : " + getOutboundInterface().getMQTTQos() + "\n\n");
				fw.flush();

				JXMLDocument document = new JXMLDocument();
				document.setDocument(getData());
				String messageContent = Utility.replaceNullStringwithBlank(document.findXPath(getOutboundInterface().getMQTTContentXPath()));

				persistence = new MemoryPersistence();

				sampleClient = new MqttClient(getOutboundInterface().getMQTBroker(), getOutboundInterface().getMQTTClient(), persistence);
				connOpts = new MqttConnectOptions();
				connOpts.setCleanSession(true);
				fw.write("Connecting to broker: " + getOutboundInterface().getMQTBroker()+ "\n");
				fw.flush();

				sampleClient.connect(connOpts);
				fw.write("Connected\n");
				fw.flush();

				fw.write("Publishing message: " + messageContent+ "\n");
				fw.flush();
				message = new MqttMessage(messageContent.getBytes());
				message.setQos(getOutboundInterface().getMQTTQos());

				sampleClient.publish(getOutboundInterface().getMQTTTopic(), message);
				fw.write("Message published\n");
				fw.flush();

				sampleClient.disconnect();
				fw.write("Disconnected\n\n");
				fw.flush();

				sampleClient.close();
				
				fw.write("SUCCESS\n");
				fw.flush();

				fw.close();

			}
			catch (MqttException me)
			{
				errorDescription = "\n\nError Reason : " + me.getReasonCode() + "\n" + "Error Message : " + me.getReasonCode() + "\n" + "Error Location : " + me.getReasonCode() + "\n" + "Error Cause : " + me.getReasonCode() + "\n" + "Error Exception : " + me
						+ "\n\n";

				fw.write(errorDescription);
				fw.write("SUCCESS\n");
				fw.flush();

				logger.debug(errorDescription);

			}
			finally
			{
				fw.close();
				fw = null;
				persistence = null;
				sampleClient = null;
				connOpts = null;
				message = null;
			}

			//////// MQTT Code End

			FileUtils.deleteQuietly(new File(finalFilename));
			FileUtils.moveFile(new File(tempFilename), new File(finalFilename));

			tempFilename = null;
			finalFilename = null;

			result = true;

		}
		catch (Exception ex)
		{
			result = false;
			logger.error(errorDescription+ex.getMessage());
			Common.emailqueue.addToQueue("Error", "Error Writing File [" + fullPath + "]", errorDescription+ex.getMessage() + "\n\n", "");

		}

		return result;
	}

}
