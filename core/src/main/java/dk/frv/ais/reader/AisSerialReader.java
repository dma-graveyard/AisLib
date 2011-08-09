package dk.frv.ais.reader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;

import org.apache.log4j.Logger;

public class AisSerialReader extends AisReader implements SerialPortEventListener {

	private static Logger LOG = Logger.getLogger(AisSerialReader.class);

	private String portName;
	private int portSpeed = 38400;
	private int dataBits = SerialPort.DATABITS_8;
	private int stopBits = SerialPort.STOPBITS_1;
	private int parity = SerialPort.PARITY_NONE;
	private long reconnectInterval = 30000; // Default 30 sec

	private SerialPort serialPort = null;
	private CommPortIdentifier portId = null;
	private InputStream inputStream;
	private OutputStream outputStream;
	private StringBuffer buffer = new StringBuffer();
	private Boolean connected = false;

	public AisSerialReader() {
	}

	@Override
	public void run() {

		while (true) {
			if (!isConnected()) {
				try {
					connect();
				} catch (Exception e) {
					e.printStackTrace();
					LOG.error("Failed to open serial port");
				}
			}

			try {
				Thread.sleep(reconnectInterval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public void send(SendRequest sendRequest, ISendResultListener resultListener) throws SendException {
		doSend(sendRequest, resultListener, outputStream);

	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		switch (event.getEventType()) {
		case SerialPortEvent.BI:
		case SerialPortEvent.OE:
		case SerialPortEvent.FE:
		case SerialPortEvent.PE:
		case SerialPortEvent.CD:
		case SerialPortEvent.CTS:
		case SerialPortEvent.DSR:
		case SerialPortEvent.RI:
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			LOG.debug("Output buffer empty");
			break;
		case SerialPortEvent.DATA_AVAILABLE:
			byte[] readBuffer = new byte[1024];
			try {
				while (inputStream.available() > 0) {
					int count = inputStream.read(readBuffer);
					for (int i = 0; i < count; i++) {
						buffer.append((char) readBuffer[i]);
						// If line feed we have a whole line
						if (readBuffer[i] == '\n') {
							String msg = buffer.toString();
							handleLine(msg);
							buffer = new StringBuffer();
						}
					}
				}
			} catch (IOException e) {
				LOG.error("Failed to read serial data: " + e.getMessage());
				serialPort.removeEventListener();
				serialPort.close();
				serialPort = null;
				portId = null;
				setConnected(false);
			}
			break;
		}
	}

	private void connect() throws IOException, UnsupportedCommOperationException, PortInUseException, TooManyListenersException {
		// Find port
		findPort();
		// Open port
		serialPort = (SerialPort) portId.open("AisSerialReader", 2000);
		// Settings
		serialPort.setSerialPortParams(portSpeed, dataBits, stopBits, parity);
		// Get streams
		inputStream = serialPort.getInputStream();
		outputStream = serialPort.getOutputStream();
		// Add event listener
		serialPort.addEventListener(this);
		serialPort.notifyOnDataAvailable(true);
		serialPort.notifyOnOutputEmpty(true);
		setConnected(true);
	}

	public void findPort() throws IOException {
		LOG.debug("Searching for port " + portName);
		Enumeration<?> portList = CommPortIdentifier.getPortIdentifiers();
		while (portList.hasMoreElements()) {
			portId = (CommPortIdentifier) portList.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				LOG.debug("portId: " + portId.getName());
				if (portId.getName().equals(portName) || portName.equals("AUTO")) {
					portName = portId.getName();
					break;
				}
			}
			portId = null;
		}
		if (portId == null) {
			throw new IOException("Unable to find serial port " + portName);
		}
	}

	public boolean isConnected() {
		synchronized (connected) {
			return connected;
		}
	}

	public void setConnected(Boolean connected) {
		synchronized (this.connected) {
			this.connected = connected;
		}
	}

	@Override
	public Status getStatus() {
		synchronized (connected) {
			return (connected) ? Status.CONNECTED : Status.DISCONNECTED;
		}
	}

	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	public int getPortSpeed() {
		return portSpeed;
	}

	public void setPortSpeed(int portSpeed) {
		this.portSpeed = portSpeed;
	}

	public int getDataBits() {
		return dataBits;
	}

	public void setDataBits(int dataBits) {
		this.dataBits = dataBits;
	}

	public int getStopBits() {
		return stopBits;
	}

	public void setStopBits(int stopBits) {
		this.stopBits = stopBits;
	}

	public int getParity() {
		return parity;
	}

	public void setParity(int parity) {
		this.parity = parity;
	}

	public long getReconnectInterval() {
		return reconnectInterval;
	}

	public void setReconnectInterval(long reconnectInterval) {
		this.reconnectInterval = reconnectInterval;
	}

}
