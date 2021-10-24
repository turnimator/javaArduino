/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.turnimator.dobratest.model;

import com.fazecast.jSerialComm.*;
import com.turnimator.dobratest.model.events.ReadDataListener;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 *
 * @author atle
 */
public class MotorControl {

    SerialPort port;
    SerialPort[] commPorts;
    ArrayList<ReadDataListener> readDataListenerArray = new ArrayList<>();
    
    public MotorControl() {
        commPorts = SerialPort.getCommPorts();
        for (SerialPort p1 : commPorts) {
            System.out.println(p1.getDescriptivePortName());
        }
    }

    public SerialPort[] getComPorts() {
        return commPorts;
    }

    public void setComPort(String s) {
        for (SerialPort p : commPorts) {
            if (p.getPortDescription().equals(s)) {
                port = p;
                port.openPort();
               
                System.out.println("Using port " + port.toString());
                port.addDataListener(new SerialPortDataListener() {
                    @Override
                    public int getListeningEvents() {
                        System.out.println("Listeningevents");
                        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
                    }

                    @Override
                    public void serialEvent(SerialPortEvent spe) {
                        if (spe.getEventType() != SerialPort.LISTENING_EVENT_DATA_RECEIVED) {
                            ///return;
                        }
                        byte[] newData = spe.getReceivedData();
                        String s = new String(newData);
                        System.out.println("Read " + s);
                        for(ReadDataListener l: readDataListenerArray){
                            l.DataRead(s);
                        }
                    }
                });
            }
        }
    }

    public void writeComPort(String s) {
        OutputStream outputStream = port.getOutputStream();
        PrintStream prn = new PrintStream(outputStream);
        prn.println(s);
        prn.flush();
    }

    public Boolean isSelected() {
        return port != null;
    }
    
    public void setSpeed(int speed){
        port.setBaudRate(speed);
    }
    
    public void addDataListener(ReadDataListener l){
        readDataListenerArray.add(l);
    }
    
    @Override
    protected void finalize() throws Throwable {
        try {
            port.closePort();
        } finally {
            super.finalize();
        }
        System.out.println("Port Closed");
    }

}
