package main;

import org.apache.velocity.app.VelocityEngine;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import static spark.Spark.get;

public class App {
    private static final Logger log = Logger.getLogger(App.class.getName());

    public static void main(String[] args) throws IOException {
        VelocityEngine configuredEngine = new VelocityEngine();
        configuredEngine.setProperty("runtime.references.strict", true);
        configuredEngine.setProperty("resource.loader", "class");
        configuredEngine.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        VelocityTemplateEngine velocityTemplateEngine = new
                VelocityTemplateEngine(configuredEngine);

        get("/log", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();

            //An array of size 3
            int[] a = {1, 2, 3};
            int index = 4;
            model.put("a", Arrays.toString(a));
            model.put("index", index);

            try {
                int test = a[index];
                model.put("test", test);
            } catch (ArrayIndexOutOfBoundsException ex) {
                log.log(Level.SEVERE, "Exception Occur", ex);
            }

            String templatePath = "/views/log.vm";
            return velocityTemplateEngine.render(new ModelAndView(model,
                    templatePath));
        });

        get("/throwing", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            String s = "Saya";
            if (s == null) {
                throw new NullPointerException();
            }
            if (s.equals("")) {
                return true;
            }
            String first = s.substring(0, 1);
            String rest = s.substring(1);
            boolean hasil = first.equals(first.toUpperCase()) &&
                    rest.equals(rest.toLowerCase());
            model.put("hasil", hasil);
            String templatePath = "/views/throwing.vm";
            return velocityTemplateEngine.render(new ModelAndView(model,
                    templatePath));
        });
        get("/nullPointer", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            String s = null;
//            try{
            String first = s.substring(0, 1);
            String rest = s.substring(1);
            boolean hasil = first.equals(first.toUpperCase()) &&
                    rest.equals(rest.toLowerCase());
            model.put("s", s);
            model.put("first", first);
            model.put("rest", rest);
            model.put("hasil", hasil);
//            }catch(NullPointerException ex) {
//                return false;
//            }
            String templatePath = "/views/nullPointer.vm";
            return velocityTemplateEngine.render(new ModelAndView(model,
                    templatePath));
        });
    }
}
