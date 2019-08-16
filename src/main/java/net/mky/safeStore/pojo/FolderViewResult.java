/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.safeStore.pojo;

/**
 *
 * @author mkfs
 */
public class FolderViewResult {
    
    private String accessIdentity;

    /**
     * Get the value of accessIdentity
     *
     * @return the value of accessIdentity
     */
    public String getAccessIdentity() {
        return accessIdentity;
    }

    /**
     * Set the value of accessIdentity
     *
     * @param accessIdentity new value of accessIdentity
     */
    public void setAccessIdentity(String accessIdentity) {
        this.accessIdentity = accessIdentity;
    }

    private String previewContent;

    /**
     * Get the value of previewContent
     *
     * @return the value of previewContent
     */
    public String getPreviewContent() {
        return previewContent;
    }

    /**
     * Set the value of previewContent
     *
     * @param previewContent new value of previewContent
     */
    public void setPreviewContent(String previewContent) {
        this.previewContent = previewContent;
    }

    private long size;

    /**
     * Get the value of size
     *
     * @return the value of size
     */
    public long getSize() {
        return size;
    }

    /**
     * Set the value of size
     *
     * @param size new value of size
     */
    public void setSize(long size) {
        this.size = size;
    }

    private String name;

    /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value of name
     *
     * @param name new value of name
     */
    public void setName(String name) {
        this.name = name;
    }

        private String location;

    /**
     * Get the value of location
     *
     * @return the value of location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Set the value of location
     *
     * @param location new value of location
     */
    public void setLocation(String location) {
        this.location = location;
    }

}
