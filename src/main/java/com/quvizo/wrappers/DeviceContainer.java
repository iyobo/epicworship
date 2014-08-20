/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quvizo.wrappers;

import java.awt.GraphicsDevice;

/**
 * This class is a container for presenting devices to the user, most commonly, for selection.
 * @author peki
 */
public class DeviceContainer {

    private String id;
    private GraphicsDevice device;

    public DeviceContainer(String id, GraphicsDevice device) {
        this.id = id;
        this.device = device;
    }

    public GraphicsDevice getDevice() {
        return device;
    }

    public void setDevice(GraphicsDevice device) {
        this.device = device;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public String toString()
    {
        return id;
    }
}
