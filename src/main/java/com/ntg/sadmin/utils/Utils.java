package com.ntg.sadmin.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

import com.ntg.sadmin.common.NTGMessageOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.CaseFormat;
import com.ntg.sadmin.constants.CodesAndKeys;
import com.ntg.sadmin.constants.CommonConstants;
import com.ntg.sadmin.exceptions.BusinessException;
import com.ntg.sadmin.web.response.BaseResponse;

import de.odysseus.staxon.json.JsonXMLConfig;
import de.odysseus.staxon.json.JsonXMLConfigBuilder;
import de.odysseus.staxon.json.JsonXMLInputFactory;
import de.odysseus.staxon.json.JsonXMLOutputFactory;
import de.odysseus.staxon.xml.util.PrettyXMLEventWriter;

public class Utils {

    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    @SuppressWarnings("rawtypes")
    public static boolean isNotEmpty(List obj) {
        return obj != null && obj.size() != 0;
    }

    public static boolean isNotEmpty(String obj) {
        return obj != null && obj.length() != 0;
    }

    public static boolean isNotEmpty(Object obj) {
        return obj != null;
    }

    public static boolean isNotEmpty(Set<?> obj) {
        return obj != null && obj.size() > 0;
    }

    public static Boolean isEmptyString(String str) {
        return str == null || str.equals("");
    }

    public static boolean isEmpty(Long obj) {
        return obj == null || obj.equals(0L);
    }

    public static boolean isNotEmpty(Long obj) {
        return obj != null && obj.toString().length() != 0;
    }

    public static boolean isNotEmpty(Object[] obj) {
        return obj != null && obj.length != 0;
    }

    public static boolean isEmpty(Object obj) {
        return obj == null;
    }

    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(List obj) {
        return obj == null || obj.size() == 0;
    }

    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Set obj) {
        return obj == null || obj.size() == 0;
    }

    public static Boolean isValidEmail(String email) {
        if (email == null || email.isEmpty())
            return true;
        Pattern p = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean compareDates(Date date1, Date date2) {
        try {
            String dt1 = null;
            String dt2 = null;

            if ((Utils.isEmpty(date1) & Utils.isNotEmpty(date2)) || (Utils.isNotEmpty(date1) & Utils.isEmpty(date2))) {
                return false;
            } else if (Utils.isEmpty(date1) & Utils.isEmpty(date2)) {
                return true;
            }

            dt1 = date1.toString();
            dt2 = date2.toString();

            SimpleDateFormat sdf = null;

            if (dt1.contains("/"))
                sdf = new SimpleDateFormat("dd/MM/yyyy");
            else
                sdf = new SimpleDateFormat("dd-MM-yyyy");

            if (dt2.contains("/"))
                sdf = new SimpleDateFormat("dd/MM/yyyy");
            else
                sdf = new SimpleDateFormat("dd-MM-yyyy");

            Date d1 = sdf.parse(dt1);
            Date d2 = sdf.parse(dt2);

            if (d1.compareTo(d2) > 0) {
                return false;
            } else if (d1.compareTo(d2) < 0) {
                return false;
            } else if (d1.compareTo(d2) == 0) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static MediaType getMediaType(int mediaTypeId) {
        if (mediaTypeId == 1) {
            return MediaType.APPLICATION_JSON;
        }
        return MediaType.APPLICATION_JSON;

    }

    public static HttpMethod getHttpMethod(int httpMethodId) {
        switch (httpMethodId) {
            case 1:
                return HttpMethod.GET;

            case 2:
                return HttpMethod.POST;

            default:
                return HttpMethod.GET;
        }

    }

    public static String format(int formatTypeId, String textToFormat) {

        if (formatTypeId == 1) {
            return wellFormatJson(textToFormat);
        }
        return textToFormat;
    }

    public static String wellFormatJson(String jsonString) {

        if (!isEmptyString(jsonString)) {

            ObjectMapper mapper = new ObjectMapper();

            try {
                mapper.enable(SerializationFeature.INDENT_OUTPUT);
                Object jsonObj = mapper.readValue(jsonString, Object.class);
                jsonString = mapper.writeValueAsString(jsonObj);
            } catch (Exception ex) {
                throw new BusinessException("1", ex);
            }

        }

        return jsonString;
    }

    public static String convertJSONToXML(String jsonString) {

        String xmlString = null;
        StringWriter stringWriter = null;
        InputStream input = null;
        JsonXMLConfig config = null;
        XMLEventReader reader = null;
        XMLEventWriter writer = null;

        if (isNotEmpty(jsonString)) {

            try {
                config = new JsonXMLConfigBuilder().virtualRoot("Root").multiplePI(false).prettyPrint(false).build();
                input = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
                reader = new JsonXMLInputFactory(config).createXMLEventReader(input);
                stringWriter = new StringWriter();
                writer = XMLOutputFactory.newInstance().createXMLEventWriter(stringWriter);
                writer = new PrettyXMLEventWriter(writer);
                writer.add(reader);
                xmlString = stringWriter.getBuffer().toString();
                stringWriter.close();

//            } catch (FactoryConfigurationError e) {
//                throw new BusinessException("1", e);
//                throw new NTGException("1", e.getMessage());
            } catch (UnsupportedEncodingException e) {
                throw new BusinessException("2", e);
//                throw new NTGException("2", e.getMessage());
            } catch (Exception e) {
                throw new BusinessException("3", e);
//                throw new NTGException("3", e.getMessage());
            } finally {
                try {
                    input.close();
                    writer.close();
                    reader.close();
                    stringWriter.close();
                } catch (IOException e) {
                    throw new BusinessException("4", e);
//                    throw new NTGException("4", e.getMessage());
                } catch (XMLStreamException e) {
                    throw new BusinessException("5", e);
//                    throw new NTGException("5", e.getMessage());
                }

            }
        }

        return xmlString;
    }

    public static String convertXMLToJSON(String xmlString) {

        String jsonString = null;
        StringWriter stringWriter = null;
        InputStream input = null;
        JsonXMLConfig config = null;
        XMLEventReader reader = null;
        XMLEventWriter writer = null;

        if (isNotEmpty(xmlString)) {

            try {
                config = new JsonXMLConfigBuilder().virtualRoot("Root").autoArray(true).autoPrimitive(true).prettyPrint(false).build();
                input = new ByteArrayInputStream(xmlString.getBytes(StandardCharsets.UTF_8));
                reader = XMLInputFactory.newInstance().createXMLEventReader(input);
                stringWriter = new StringWriter();
                writer = new JsonXMLOutputFactory(config).createXMLEventWriter(stringWriter);
                writer.add(reader);
                jsonString = wellFormatJson(stringWriter.getBuffer().toString());

//            } catch (FactoryConfigurationError e) {
//                throw new BusinessException("1", e);
//                throw new NTGException("1", e.getMessage());
            } catch (Exception e) {
                throw new BusinessException("3", e);
//                throw new NTGException("3", e.getMessage());
            } finally {
                try {
                    input.close();
                    writer.close();
                    reader.close();
                    stringWriter.close();
                } catch (IOException e) {
                    throw new BusinessException("4", e);
//                    throw new NTGException("4", e.getMessage());
                } catch (XMLStreamException e) {
                    throw new BusinessException("5", e);
//                    throw new NTGException("5", e.getMessage());
                }

            }
        }

        return jsonString;
    }

    public static boolean isXMLFormat(String inStr) {

        try {
            DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(inStr)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String addDefaultRootForJsonAarryIfNotExists(String jsonString) {

        StringBuilder builder = null;

        if (isNotEmpty(jsonString)) {

            if (jsonString.startsWith("[")) {
                builder = new StringBuilder();
                builder.append(CommonConstants.INTEGRATION_JSON_VIRTUAL_ROOT);
                builder.append(jsonString);
                builder.append("}");

                jsonString = builder.toString();
            }
        }

        return jsonString;
    }

    public static String removeDefaultRootForJsonAarryIfExists(String jsonString) {

        StringBuilder builder = null;

        if (isNotEmpty(jsonString)) {

            if (jsonString.startsWith(CommonConstants.INTEGRATION_JSON_VIRTUAL_ROOT)) {
                builder = new StringBuilder();
                builder.append(jsonString.replaceAll(CommonConstants.INTEGRATION_JSON_VIRTUAL_ROOT, "["));
                builder.append(jsonString.substring(0, jsonString.lastIndexOf("}")));
                builder.append("]");

                jsonString = builder.toString();
            }
        }

        return jsonString;
    }

    public static <SOURCE, TARGET> void convertListToDtoList(List<SOURCE> sourceList, List<TARGET> targetList, Class<TARGET> targetClass)
            throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        for (SOURCE src : sourceList) {
            String value = mapper.writeValueAsString(src);
            TARGET target = mapper.readValue(value, targetClass);
            targetList.add(target);
        }
    }

    public static String convertCamelCaseToUnderscore(String colName) {

        if (Utils.isNotEmpty(colName) && !colName.contains("_")) {
            colName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, colName);
        }

        return colName;
    }

    public static Map<String, String> buildParametersMap(HttpServletRequest httpServletRequest) {
        logger.info("start buildParametersMap function");
        Map<String, String> resultMap = new HashMap<>();
        Enumeration<String> parameterNames = httpServletRequest.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String value = httpServletRequest.getParameter(key);
            resultMap.put(key, value);
        }
        logger.info("end buildParametersMap function");
        return resultMap;
    }

    @SuppressWarnings("rawtypes")
    public static Map<String, String> buildRequestHeadersMap(HttpServletRequest request) {
        logger.info("start buildRequestHeadersMap function");

        Map<String, String> map = new HashMap<>();

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        logger.info("end buildRequestHeadersMap function");
        return map;
    }

    public static Map<String, String> buildResponseHeadersMap(HttpServletResponse response) {
        logger.info("start buildResponseHeadersMap function");

        Map<String, String> map = new HashMap<>();

        Collection<String> headerNames = response.getHeaderNames();
        for (String header : headerNames) {
            if (!header.equals("authorization")) {
                map.put(header, response.getHeader(header));
            }

        }
        logger.info("end buildResponseHeadersMap function");
        return map;
    }

    public static String loadFromPropertyFile(String fileName, String key) {
        try {
            String path = System.getProperty("user.home") + "/.Smart2GoConfig/." + fileName;
            File f = new File(path);
            Properties props = new Properties();
            props.load(new FileInputStream(f));
            return (props.getProperty(key) != null) ? props.getProperty(key.toLowerCase()) : null;
        } catch (IOException e) {
            NTGMessageOperation.PrintErrorTrace(e);
            return null;
        }
    }


    /**
     * <p>
     * This function used to Validate Password with this constrains 1-Be between 8
     * and 40 characters long Contain at least one digit. 2- Contain at least one
     * lower case character. 3- Contain at least one upper case character. 4-Contain
     * at least on special character from [ @ # $ % ! . ].
     * </p>
     *
     * @param password
     * @return true if password pattern is correct
     * @since 2019
     */

    public static boolean validatePasswordStrength(String password) {
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})";

        pattern = Pattern.compile(PASSWORD_PATTERN);

        matcher = pattern.matcher(password);
        return matcher.matches();

    }

    public static BaseResponse internalServerError(String message) {

        return new BaseResponse(CodesAndKeys.INTERNAL_SERVER_ERROR_CODE,
                CodesAndKeys.INTERNAL_SERVER_ERROR_KEY, message);
    }


}
