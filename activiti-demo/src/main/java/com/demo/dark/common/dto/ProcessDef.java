package com.demo.dark.common.dto;

import java.util.Date;

public class ProcessDef {
    
    public String id;
    public String deploymentId;
    public String name;
    public String key;
    public int version;
    public Date deploymentTime;
    public boolean suspended;
    
    private String resourceName;
    private String diagramResourceName;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDeploymentId() {
        return deploymentId;
    }
    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public int getVersion() {
        return version;
    }
    public void setVersion(int version) {
        this.version = version;
    }
    public Date getDeploymentTime() {
        return deploymentTime;
    }
    public void setDeploymentTime(Date deploymentTime) {
        this.deploymentTime = deploymentTime;
    }
    public boolean isSuspended() {
        return suspended;
    }
    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }
    public String getResourceName() {
        return resourceName;
    }
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
    public String getDiagramResourceName() {
        return diagramResourceName;
    }
    public void setDiagramResourceName(String diagramResourceName) {
        this.diagramResourceName = diagramResourceName;
    }
    
    
}
