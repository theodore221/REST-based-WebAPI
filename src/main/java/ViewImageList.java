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

public class ViewImageList {


    public static void main(String[] args) {


        get("/", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            return new ModelAndView(model, "main.hbs");
        }, new HandlebarsTemplateEngine());

        post("/viewImageList", (request, response) -> {
            System.out.println("********* view image list");
            Map<String, Object> model = new HashMap<String, Object>();

            return new ModelAndView(model, "main.hbs");
        }, new HandlebarsTemplateEngine());

        /*post("/imageUpload", (req, response) -> {

        }, new HandlebarsTemplateEngine());*/
    }



    /*public static void main(String[] args) {
        staticFileLocation("/public");
        get("/", (request, response) -> {
            return new ModelAndView(new HashMap(), "main.hbs");
        }, new HandlebarsTemplateEngine());


    }*/
}
