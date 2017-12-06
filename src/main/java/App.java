import static spark.Spark.*;
import java.util.HashMap;
import java.util.Map;
import java.io.*;
import spark.ModelAndView;
import spark.utils.IOUtils;
import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import spark.template.handlebars.HandlebarsTemplateEngine;
import java.util.ArrayList;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;


public class App {

    public static void main(String[] args) {

        File uploadDir = new File("upload");
        uploadDir.mkdir(); //creates new directory titled upload in the project folder
        String folderPath = uploadDir.getAbsolutePath(); //helper string for folder path
        String outFolderPath=folderPath+"/";

        /*
        Scans existing image file in the folder so users can still access previously uploaded images
         */
        File[] fList = uploadDir.listFiles();
        for(File file: fList){
            Image image = new Image(file.getName(), file.getAbsolutePath());
        }


        /*
        Initial Spark Route, loads main page
         */
        get("/", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            return new ModelAndView(model, "main.hbs");
        }, new HandlebarsTemplateEngine());

        /*
        Route processes upload method, and image file checking
        If submitted file is a image, routes user to success page
        If submitted file is not a image, routes to a error page
         */
        post("/imageUpload", (req, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement(folderPath));
            Part filePart = req.raw().getPart("myfile");
            String fileName = filePart.getSubmittedFileName();

            /*
            If the uploaded file is an image file, inputs file into the upload folder
             */
            if(filePart.getContentType().contains("image")){
                Image image = new Image(fileName,outFolderPath+fileName);
                try (InputStream inputStream = filePart.getInputStream()) {
                    OutputStream outputStream = new FileOutputStream(outFolderPath + fileName);
                    IOUtils.copy(inputStream, outputStream);
                    outputStream.close();
                }
                model.put("filename", fileName);
                return new ModelAndView(model, "sucess.hbs");
            }else {

                /*
                If uploaded file is a ZIP, unzips individual image files and adds to upload
                 */

                if (filePart.getContentType().contains("zip")) {

                    byte[] buffer = new byte[1024];

                    ZipInputStream zStream = new ZipInputStream(filePart.getInputStream());
                    ZipEntry zEntry = zStream.getNextEntry();

                    while (zEntry != null) {

                        String zipName = zEntry.getName();
                        Image image = new Image(zEntry.getName(),outFolderPath+zEntry.getName());

                        File newFile = new File("upload" + "/" + zipName);
                        FileOutputStream fStream = new FileOutputStream((newFile));

                        int len;
                        while ((len = zStream.read(buffer)) > 0) {
                            fStream.write(buffer, 0, len);
                        }

                        fStream.close();
                        zEntry = zStream.getNextEntry();
                    }

                    zStream.closeEntry();
                    zStream.close();

                    return new ModelAndView(model, "sucess.hbs");
                } else {
                    return new ModelAndView(model, "notImage.hbs");
                }
            }
            }, new HandlebarsTemplateEngine());


        /*
        Route that allows users to view new and existing uploads
        Users get a list of file names related to each upload

        ** Was not able to link the uploaded pictures to the anchor tags in my template files
        * Wasn't exactly sure how to link within the template and file path to link the image
         */
        post("/viewImageList", (request, response) -> {
            Map<String, ArrayList<Image>> model = new HashMap<>();
            ArrayList myImageList = Image.getAll();
            model.put("myImages", myImageList);
            return new ModelAndView(model, "viewImage.hbs");
            }, new HandlebarsTemplateEngine());



    }


}
