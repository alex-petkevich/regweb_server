package serv;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.Properties;

public class req extends HttpServlet {

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException, ServletException
    {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("</head>");
        out.println("<body bgcolor=\"white\">");
        out.println("<h1>Internal server error</h1>");
        out.println("</body>");
        out.println("</html>");
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
            throws IOException, ServletException
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        StringBuilder result = new StringBuilder();
        String body = getBody(request);
        String[] pairs = body.split("&");
        for (int k=0;k<pairs.length;k++) {
           String[] pair = pairs[k].split("=");
           result.append( pair.length > 1 ? pair[1] : pair[0] );
        }

        Properties prop = new Properties();
        prop.load(this.getClass().getResourceAsStream("/general.properties"));

        String filename = prop.getProperty("logs").trim() + (new Date()).getTime() + ".date";
        PrintWriter fileout  = null;
        try {
            fileout = new PrintWriter(filename);
            fileout.println(result);
        } catch (FileNotFoundException e) {
            out.println("Information output error");
        }  finally {
            fileout.close();
        }

        out.println("ok");
    }

    public static String getBody(HttpServletRequest request) throws IOException {

        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        body = stringBuilder.toString();
        return body;
    }

}