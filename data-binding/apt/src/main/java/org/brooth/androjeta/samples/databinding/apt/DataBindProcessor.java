package org.brooth.androjeta.samples.databinding.apt;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import org.brooth.androjeta.samples.databinding.DataBind;
import org.brooth.androjeta.samples.databinding.DataBindMetacode;
import org.brooth.jeta.apt.ProcessingContext;
import org.brooth.jeta.apt.ProcessingException;
import org.brooth.jeta.apt.RoundContext;
import org.brooth.jeta.apt.processors.AbstractProcessor;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileNotFoundException;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * @author Oleg Khalidov (brooth@gmail.com)
 */
public class DataBindProcessor extends AbstractProcessor {

    private static final String XMLNS_PREFIX = "xmlns:";
    private static final String ANDROID_NAMESPACE = "http://schemas.android.com/apk/res/android";
    private static final String ANDROJETA_NAMESPACE = "http://schemas.jeta.brooth.org/androjeta";

    private ClassName textViewClassname;
    private ClassName rCLassName;

    private String layoutsPath;

    private String androidPrefix;
    private String androjetaPrefix;
    private String componentId;
    private String componentExpression;

    public DataBindProcessor() {
        super(DataBind.class);
    }

    @Override
    public void init(ProcessingContext processingContext) {
        super.init(processingContext);
        layoutsPath = processingContext.processingEnv().getOptions().get("layoutsPath");
        if (layoutsPath == null)
            throw new ProcessingException("'layoutsPath' not defined");

        String appPackage = processingContext.processingProperties().getProperty("application.package");
        if (appPackage == null)
            throw new ProcessingException("'application.package' not defined");

        textViewClassname = ClassName.bestGuess("android.widget.TextView");
        rCLassName = ClassName.bestGuess(appPackage + ".R");
    }

    @Override
    public boolean process(TypeSpec.Builder builder, final RoundContext roundContext) {
        TypeElement element = roundContext.metacodeContext().masterElement();
        ClassName masterClassName = ClassName.get(element);
        builder.addSuperinterface(ParameterizedTypeName.get(
                ClassName.get(DataBindMetacode.class), masterClassName));

        final MethodSpec.Builder methodBuilder = MethodSpec.
                methodBuilder("apply")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(masterClassName, "master");

        String layoutName = element.getAnnotation(DataBind.class).layout();
        String layoutPath = layoutsPath + File.separator + layoutName + ".xml";
        File layoutFile = new File(layoutPath);
        if (!layoutFile.exists())
            throw new ProcessingException(new FileNotFoundException(layoutPath));

        androidPrefix = null;
        androjetaPrefix = null;

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(layoutFile, new DefaultHandler() {
                        @Override
                        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                            for (int i = 0; i < attributes.getLength(); i++) {
                                if (androidPrefix == null &&
                                        attributes.getQName(i).startsWith(XMLNS_PREFIX) &&
                                        attributes.getValue(i).equals(ANDROID_NAMESPACE)) {
                                    androidPrefix = attributes.getQName(i).substring(XMLNS_PREFIX.length());
                                    continue;
                                }

                                if (androjetaPrefix == null &&
                                        attributes.getQName(i).startsWith(XMLNS_PREFIX) &&
                                        attributes.getValue(i).equals(ANDROJETA_NAMESPACE)) {
                                    androjetaPrefix = attributes.getQName(i).substring(XMLNS_PREFIX.length());
                                    continue;
                                }

                                if (componentId == null && androidPrefix != null &&
                                        attributes.getQName(i).equals(androidPrefix + ":id")) {
                                    componentId = attributes.getValue(i).substring("@+id/".length());
                                    continue;
                                }

                                if (componentExpression == null && androjetaPrefix != null &&
                                        attributes.getQName(i).equals(androjetaPrefix + ":setText")) {
                                    componentExpression = attributes.getValue(i);
                                }
                            }
                        }

                        @Override
                        public void endElement(String uri, String localName, String qName) throws SAXException {
                            if (componentExpression == null)
                                return;

                            if (componentId == null)
                                throw new ProcessingException("Failed to process expression '" +
                                        componentExpression + "', component has no id");

                            methodBuilder.addStatement("(($T) master.findViewById($T.id.$L)).setText($L)",
                                    textViewClassname, rCLassName, componentId, componentExpression);

                            componentId = null;
                            componentExpression = null;
                        }
                    }
            );

        } catch (Exception e) {
            throw new ProcessingException(e);
        }

        builder.addMethod(methodBuilder.build());
        return false;
    }
}
