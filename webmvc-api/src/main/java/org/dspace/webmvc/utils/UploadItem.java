/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dspace.webmvc.utils;

import java.io.IOException;
import java.io.File;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.dspace.core.ConfigurationManager;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 *
 * @author Robert Qin
 */
public class UploadItem {

    private String name;
    private CommonsMultipartFile fileData;
    private String tempDir = null;
    private File destination = null;

    public UploadItem(HttpServletRequest req, CommonsMultipartFile file) throws IOException, FileSizeLimitExceededException {

        tempDir = ConfigurationManager.getProperty("upload.temp.dir");
        long maxSize = ConfigurationManager.getLongProperty("upload.max");
        // MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) req;
        // MultipartFile file = multipartRequest.getFile(fileParam);
        CommonsMultipartResolver cmr = new CommonsMultipartResolver();
        try {
            cmr.setMaxUploadSize(maxSize);
            destination = new File(tempDir + file.getOriginalFilename());
            name = file.getOriginalFilename();
            file.transferTo(destination);
            
        } catch (Exception e) {
            if (e.getMessage().contains("exceeds the configured maximum")) {
                // ServletFileUpload is not throwing the correct error, so this is workaround
                // the request was rejected because its size (11302) exceeds the configured maximum (536)
                int startFirstParen = e.getMessage().indexOf("(") + 1;
                int endFirstParen = e.getMessage().indexOf(")");
                String uploadedSize = e.getMessage().substring(startFirstParen, endFirstParen).trim();
                Long actualSize = Long.parseLong(uploadedSize);
                throw new FileSizeLimitExceededException(e.getMessage(), actualSize, maxSize);
            }
            throw new IOException(e.getMessage(), e);

        }//end catch

    }//end UploadItem

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public File getFile(){
        
        return destination;
    }
    
    public void setFile(File destination){
        this.destination = destination;
    }
    
    public CommonsMultipartFile getFileData() {
        return fileData;
    }

    public void setFileData(CommonsMultipartFile fileData) {
        this.fileData = fileData;
    }
}
