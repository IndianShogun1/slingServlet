package com.shreyansh.servlets;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_PATHS;

@Component(service = {Servlet.class},
            property = {SLING_SERVLET_PATHS + "=/blogs"})
public class BlogAsPdfServlet extends SlingSafeMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request,
                         SlingHttpServletResponse response) throws ServletException,
            IOException {
        Resource resource = request.getResourceResolver().getResource("/content/sample");

        Iterator<Resource> children = resource.listChildren();

        List<Resource> childrenList = new ArrayList();

        Document document = new Document();

        ServletOutputStream baos = response.getOutputStream();
        response.setContentType("application/pdf");

        try {

            PdfWriter.getInstance(document, baos);
            document.open();


            while (children.hasNext()) {
                childrenList.add(children.next());
            }

            for (Resource resource1 : childrenList) {
                document.add(new Paragraph(resource1.getName() + "             " + resource1.getValueMap().get("jcr:created", "default")));
            }
            document.add(new Paragraph("\n"));

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
