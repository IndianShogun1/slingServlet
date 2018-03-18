package com.shreyansh.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;


import javax.servlet.Servlet;
import javax.servlet.ServletException;

import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_EXTENSIONS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_RESOURCE_TYPES;

@Component(service = {Servlet.class},
            property = {SLING_SERVLET_RESOURCE_TYPES + "=/apps/item1",
                        SLING_SERVLET_EXTENSIONS + "=json"
            })
public class ListBlogsServlet extends SlingSafeMethodsServlet {
    @Override
    protected void doGet(SlingHttpServletRequest request, final SlingHttpServletResponse response) throws ServletException, IOException {

        Resource resource = request.getResource();

        Writer w = response.getWriter();
        Iterable<Resource> children = resource.getChildren();

        List<Resource> childrenList = new ArrayList<>();

        for (Resource child : children) {
            childrenList.add(child);
        }

        if(request.getParameter("order").equals("ascending")){

            Collections.sort(childrenList, new Comparator<Resource>() {

                @Override
                public int compare(Resource resource1, Resource resource2) {

                    ValueMap valueMap1 = resource1.adaptTo(ValueMap.class);
                    ValueMap valueMap2 = resource2.adaptTo(ValueMap.class);
                    String stringDate1 = valueMap1.get("jcr:created", "default");
                    String stringDate2 = valueMap2.get("jcr:created", "default");
                    Date date1 = null, date2 = null;
                    try {
                        date1 = new SimpleDateFormat("yyyy-MM-dd").parse(stringDate1);
                        date2 = new SimpleDateFormat("yyyy-MM-dd").parse(stringDate2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return date1.compareTo(date2);
                }
            });
        }

        if(request.getParameter("order").equals("descending")){

            Collections.sort(childrenList, new Comparator<Resource>() {

                @Override
                public int compare(Resource resource1, Resource resource2) {

                    ValueMap valueMap1 = resource1.adaptTo(ValueMap.class);
                    ValueMap valueMap2 = resource2.adaptTo(ValueMap.class);
                    String stringDate1 = valueMap1.get("jcr:created", "default");
                    String stringDate2 = valueMap2.get("jcr:created", "default");
                    Date date1 = null, date2 = null;
                    try {
                        date1 = new SimpleDateFormat("yyyy-MM-dd").parse(stringDate1);
                        date2 = new SimpleDateFormat("yyyy-MM-dd").parse(stringDate2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return -1 * ( date1.compareTo(date2) );
                }
            });
        }

        for(Resource child : childrenList) {

            ValueMap valueMap = child.adaptTo(ValueMap.class);
            w.write(valueMap.get("jcr:title", "default title") + " ");
            w.write(valueMap.get("jcr:created", "default date") + "\n");
        }
    }
}
