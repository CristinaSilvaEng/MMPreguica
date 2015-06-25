package app;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Cristina Silva on 25/06/2015.
 */
public class Main {

    public static void main(String args[]){

        try {
            InputStream inputStream = new URL("http://www.miniquentinhas.com.br/cardapio_atual.pdf").openStream();

            Parser parser = new AutoDetectParser();
            ContentHandler contentHandler = new BodyContentHandler();
            parser.parse(inputStream, contentHandler, new Metadata(), new ParseContext());
            HashMap<Date, ArrayList<String>> menu = new HashMap<Date, ArrayList<String>>();

            String[] lines = contentHandler.toString().split("\n");
            for (int count = 0; count < lines.length; count++){
                String line = lines[count].trim();
                if (line.matches(".*?\\d{2}\\.\\d{2}.*?") && !line.contains("FERIADO")) {
                    Date date = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),
                            Integer.parseInt(line.substring(3, 5)),
                            Integer.parseInt(line.substring(0, 2))).getTime();
                    ArrayList<String> options = new ArrayList<String>();
                    for (int index = 0; index < line.length(); index++) {
                        if (Character.isUpperCase(line.charAt(index))) {
                            StringBuilder stringBuilder = new StringBuilder();
                            while (!Character.isLowerCase(line.charAt(index))) {
                                stringBuilder.append(line.charAt(index));
                                if (index < line.length() - 1) {
                                    index++;
                                } else {
                                    break;
                                }
                            }
                            options.add(stringBuilder.toString().trim());
                        }
                    }
                    menu.put(date, options);
                }
            }

            for (Date date : menu.keySet()) {
                System.out.println("["+ date +"] " + menu.get(date));
            }

        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (SAXException exception) {
            exception.printStackTrace();
        } catch (TikaException exception) {
            exception.printStackTrace();
        }
    }

}
