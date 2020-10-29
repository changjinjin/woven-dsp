package com.info.baymax.common.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * jaxb处理工具
 *
 * @author jingwei.yang
 * @date 2019-05-29 15:41
 */
public class JaxbUtils {

	private static final String DEFAULT_ENCODING = "UTF-8";

	/**
	 * 创建上下文对象. <br>
	 *
	 * @param clazz
	 *            转换类型
	 * @param format
	 *            是否格式化
	 * @param fragment
	 *            是否省略头信息
	 * @param encoding
	 *            编码
	 * @param schema_location
	 * @param no_namespace_schema_location
	 * @return
	 * @throws JAXBException
	 */
	private static <T> Marshaller createMarshaller(Class<T> clazz, boolean format, boolean fragment, String encoding,
			String schema_location, String no_namespace_schema_location) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(clazz);
		Marshaller ms = jc.createMarshaller();
		ms.setProperty(Marshaller.JAXB_ENCODING, StringUtils.defaultIfEmpty(encoding, DEFAULT_ENCODING));// 默认编码UTF-8
		ms.setProperty(Marshaller.JAXB_FRAGMENT, fragment);// 是否省略xml头声明信息
		// 用来指定是否使用换行和缩排对已编组XML数据进行格式化的属性名称
		ms.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, format);

		if (StringUtils.isNotEmpty(schema_location)) {
			// 用来指定将放置在已编组 XML 输出中的 xsi:schemaLocation 属性值的属性名称
			ms.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, schema_location);
		}
		if (StringUtils.isNotEmpty(no_namespace_schema_location)) {
			// 用来指定将放置在已编组 XML 输出中的 xsi:noNamespaceSchemaLocation 属性值的属性名称
			ms.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, no_namespace_schema_location);
		}
		return ms;

	}

	/**
	 * java对象序列化为xml数据流，通过参数设置xml是否格式化和省略头信息. <br>
	 * <p>
	 *
	 * @param out
	 *            xml输出流
	 * @param t
	 *            java对象
	 * @param clazz
	 *            java类
	 * @param format
	 *            是否格式化xml流，默认false
	 * @param fragment
	 *            是否省略头信息，true-是，false-否
	 * @param encoding
	 *            xml编码
	 * @param schema_location
	 *            The name of the property used to specify the
	 *            xsi:schemaLocation attribute value to place in the marshalled
	 *            XML output.
	 * @param no_namespace_schema_location
	 *            The name of the property used to specify the
	 *            xsi:noNamespaceSchemaLocation attribute value to place in the
	 *            marshalled XML output.
	 * @throws JAXBException
	 */
	public static <T> void java2xml(OutputStream out, T t, Class<T> clazz, boolean format, boolean fragment,
			String encoding, String schema_location, String no_namespace_schema_location) throws JAXBException {
		try {
			Marshaller ms = createMarshaller(clazz, format, fragment, encoding, schema_location,
					no_namespace_schema_location);
			ms.marshal(t, out);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * java对象序列化为xml数据流，通过参数设置xml是否格式化和省略头信息. <br>
	 * <p>
	 *
	 * @param out
	 *            xml输出流
	 * @param t
	 *            java对象
	 * @param clazz
	 *            java类
	 * @param format
	 *            是否格式化xml流，默认false
	 * @param fragment
	 *            是否省略头信息，true-是，false-否
	 * @throws JAXBException
	 */
	public static <T> void java2xml(OutputStream out, T t, Class<T> clazz, boolean format, boolean fragment,
			String schema_location, String no_namespace_schema_location) throws JAXBException {
		java2xml(out, t, clazz, format, fragment, DEFAULT_ENCODING, schema_location, no_namespace_schema_location);
	}

	/**
	 * java对象序列化为xml数据流，通过参数设置xml是否格式化和省略头信息. <br>
	 * <p>
	 *
	 * @param out
	 *            xml输出流
	 * @param t
	 *            java对象
	 * @param clazz
	 *            java类
	 * @param format
	 *            是否格式化xml流，默认false
	 * @param fragment
	 *            是否省略头信息，true-是，false-否
	 * @throws JAXBException
	 */
	public static <T> void java2xml(OutputStream out, T t, Class<T> clazz, boolean format, boolean fragment)
			throws JAXBException {
		java2xml(out, t, clazz, format, fragment, null, null);
	}

	/**
	 * java对象序列化为xml数据流，默认不格式化. <br>
	 * <p>
	 *
	 * @param out
	 *            输出流
	 * @param t
	 *            java对象
	 * @param clazz
	 *            java类
	 * @param fragment
	 *            是否省略头信息，true-是，false-否
	 * @throws JAXBException
	 */
	public static <T> void java2xml(OutputStream out, T t, Class<T> clazz, boolean fragment) throws JAXBException {
		java2xml(out, t, clazz, false, fragment);
	}

	/**
	 * java对象序列化为xml数据流，默认不格式化xml，并且省略头信息. <br>
	 * <p>
	 *
	 * @param out
	 *            输出流
	 * @param t
	 *            java对象
	 * @param clazz
	 *            java类
	 * @throws JAXBException
	 */
	public static <T> void java2xml(OutputStream out, T t, Class<T> clazz) throws JAXBException {
		java2xml(out, t, clazz, true);
	}

	/**
	 * java对象序列化为xml数据流.<br>
	 * <p>
	 *
	 * @param writer
	 *            xml输出
	 * @param t
	 *            java对象
	 * @param clazz
	 *            java类
	 * @param format
	 *            是否格式化
	 * @param fragment
	 *            是否省略头信息，true-是，false-否
	 * @throws JAXBException
	 */
	public static <T> void java2xml(Writer writer, T t, Class<T> clazz, boolean format, boolean fragment,
			String encoding, String schema_location, String no_namespace_schema_location) throws JAXBException {
		try {
			Marshaller ms = createMarshaller(clazz, format, fragment, encoding, schema_location,
					no_namespace_schema_location);
			ms.marshal(t, writer);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * java对象序列化为xml数据流.<br>
	 * <p>
	 *
	 * @param writer
	 *            xml输出
	 * @param t
	 *            java对象
	 * @param clazz
	 *            java类
	 * @param format
	 *            是否格式化
	 * @param fragment
	 *            是否省略头信息，true-是，false-否
	 * @throws JAXBException
	 */
	public static <T> void java2xml(Writer writer, T t, Class<T> clazz, boolean format, boolean fragment,
			String encoding) throws JAXBException {
		java2xml(writer, t, clazz, format, fragment, encoding, null, null);
	}

	/**
	 * java对象序列化为xml数据流.<br>
	 * <p>
	 *
	 * @param writer
	 *            xml输出
	 * @param t
	 *            java对象
	 * @param clazz
	 *            java类
	 * @param format
	 *            是否格式化
	 * @param fragment
	 *            是否省略头信息，true-是，false-否
	 * @throws JAXBException
	 */
	public static <T> void java2xml(Writer writer, T t, Class<T> clazz, boolean format, boolean fragment)
			throws JAXBException {
		java2xml(writer, t, clazz, format, fragment, DEFAULT_ENCODING);
	}

	/**
	 *
	 *
	 * java对象序列化为xml数据流
	 *
	 *
	 * @param writer
	 *            xml输出
	 * @param t
	 *            java对象
	 * @param clazz
	 *            java类
	 * @param fragment
	 *            是否省略头信息，true-是，false-否
	 * @throws JAXBException
	 */
	public static <T> void java2xml(Writer writer, T t, Class<T> clazz, boolean fragment) throws JAXBException {
		java2xml(writer, t, clazz, false, fragment);
	}

	/**
	 *
	 *
	 * java对象序列化为xml数据流
	 *
	 *
	 * @param writer
	 *            xml输出
	 * @param t
	 *            java对象
	 * @param clazz
	 *            java类
	 * @throws JAXBException
	 */
	public static <T> void java2xml(Writer writer, T t, Class<T> clazz) throws JAXBException {
		java2xml(writer, t, clazz, true);
	}

	/**
	 *
	 *
	 * java对象序列化为xml字符串
	 *
	 *
	 * @param t
	 *            java对象
	 * @param clazz
	 *            java类
	 * @param format
	 *            是否格式化xml文档，true-是，false-否
	 * @param fragment
	 *            是否省略头信息，true-是，false-否
	 * @return 返回转化xml字符串
	 * @throws JAXBException
	 */
	public static <T> String java2xml(T t, Class<T> clazz, boolean format, boolean fragment, String encoding,
			String schema_location, String no_namespace_schema_location) throws JAXBException {
		StringWriter stringWriter = new StringWriter();
		java2xml(stringWriter, t, clazz, format, fragment, encoding, schema_location, no_namespace_schema_location);
		return stringWriter.toString();
	}

	/**
	 *
	 *
	 * java对象序列化为xml字符串
	 *
	 *
	 * @param t
	 *            java对象
	 * @param clazz
	 *            java类
	 * @param format
	 *            是否格式化xml文档，true-是，false-否
	 * @param fragment
	 *            是否省略头信息，true-是，false-否
	 * @return 返回转化xml字符串
	 * @throws JAXBException
	 */
	public static <T> String java2xml(T t, Class<T> clazz, boolean format, boolean fragment, String encoding)
			throws JAXBException {
		StringWriter stringWriter = new StringWriter();
		java2xml(stringWriter, t, clazz, format, fragment, encoding);
		return stringWriter.toString();
	}

	/**
	 *
	 *
	 * java对象序列化为xml字符串
	 *
	 * @param t
	 *            java对象
	 * @param clazz
	 *            java类
	 * @param format
	 *            是否格式化xml文档，true-是，false-否
	 * @param fragment
	 *            是否省略头信息，true-是，false-否
	 * @return 返回转化xml字符串
	 * @throws JAXBException
	 */
	public static <T> String java2xml(T t, Class<T> clazz, boolean format, boolean fragment) throws JAXBException {
		StringWriter stringWriter = new StringWriter();
		java2xml(stringWriter, t, clazz, format, fragment);
		return stringWriter.toString();
	}

	/**
	 * java对象序列化为xml字符串
	 *
	 * @param t
	 *            java对象
	 * @param clazz
	 *            java类
	 * @param fragment
	 *            是否省略头信息，true-是，false-否
	 * @return 返回转化xml字符串
	 * @throws JAXBException
	 */
	public static <T> String java2xml(T t, Class<T> clazz, boolean fragment) throws JAXBException {
		return java2xml(t, clazz, false, fragment);
	}

	/**
	 * java对象序列化为xml字符串
	 *
	 * @param t
	 *            java对象
	 * @param clazz
	 *            java类
	 * @return 返回转化xml字符串
	 * @throws JAXBException
	 */
	public static <T> String java2xml(T t, Class<T> clazz) throws JAXBException {
		return java2xml(t, clazz, true);
	}

	/**
	 * java对象序列化为xml数据流
	 *
	 * @param file
	 *            xml数据存储目标文件
	 * @param t
	 *            java对象
	 * @param clazz
	 *            java类
	 * @param format
	 *            是否格式化xml文档，true-是，false-否
	 * @param fragment
	 *            是否省略头信息，true-是，false-否
	 * @throws FileNotFoundException
	 * @throws JAXBException
	 */
	public static <T> void java2xml(File file, T t, Class<T> clazz, boolean format, boolean fragment, String encoding,
			String schema_location, String no_namespace_schema_location) throws FileNotFoundException, JAXBException {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			java2xml(out, t, clazz, format, fragment, encoding, schema_location, no_namespace_schema_location);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * java对象序列化为xml数据流
	 *
	 * @param file
	 *            xml数据存储目标文件
	 * @param t
	 *            java对象
	 * @param clazz
	 *            java类
	 * @param format
	 *            是否格式化xml文档，true-是，false-否
	 * @param fragment
	 *            是否省略头信息，true-是，false-否
	 * @throws FileNotFoundException
	 * @throws JAXBException
	 */
	public static <T> void java2xml(File file, T t, Class<T> clazz, boolean format, boolean fragment, String encoding)
			throws FileNotFoundException, JAXBException {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			java2xml(out, t, clazz, format, fragment, encoding, null, null);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * java对象序列化为xml数据流
	 *
	 * @param file
	 *            xml数据存储目标文件
	 * @param t
	 *            java对象
	 * @param clazz
	 *            java类
	 * @param format
	 *            是否格式化xml文档，true-是，false-否
	 * @param fragment
	 *            是否省略头信息，true-是，false-否
	 * @throws FileNotFoundException
	 * @throws JAXBException
	 */
	public static <T> void java2xml(File file, T t, Class<T> clazz, boolean format, boolean fragment)
			throws FileNotFoundException, JAXBException {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			java2xml(out, t, clazz, format, fragment);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * java对象序列化为xml数据流
	 *
	 * @param file
	 *            xml数据存储目标文件
	 * @param t
	 *            java对象实例
	 * @param clazz
	 *            java类
	 * @param fragment
	 *            是否省略头信息，true-是，false-否
	 * @throws JAXBException
	 * @throws FileNotFoundException
	 */
	public static <T> void java2xml(File file, T t, Class<T> clazz, boolean fragment)
			throws FileNotFoundException, JAXBException {
		java2xml(file, t, clazz, false, fragment);
	}

	/**
	 * java对象序列化为xml数据流
	 *
	 * @param file
	 *            xml数据存储目标文件
	 * @param t
	 *            java对象实例
	 * @param clazz
	 *            java类
	 * @throws JAXBException
	 * @throws FileNotFoundException
	 */
	public static <T> void java2xml(File file, T t, Class<T> clazz) throws FileNotFoundException, JAXBException {
		java2xml(file, t, clazz, true);
	}

	/**
	 * java对象序列化为xml数据流
	 *
	 * @param node
	 *            xml DOM
	 * @param t
	 *            java对象
	 * @param clazz
	 *            java类
	 * @param format
	 *            是否格式化xml流，默认false
	 * @param fragment
	 *            是否省略头信息，true-是，false-否
	 * @throws JAXBException
	 */
	public static <T> void java2xml(Node node, T t, Class<T> clazz, boolean format, boolean fragment, String encoding,
			String schema_location, String no_namespace_schema_location) throws JAXBException {
		Marshaller ms = createMarshaller(clazz, format, fragment, encoding, schema_location,
				no_namespace_schema_location);
		ms.marshal(t, node);
	}

	/**
	 * java对象序列化为xml数据流
	 *
	 * @param node
	 *            xml DOM树
	 * @param t
	 *            java对象
	 * @param clazz
	 *            java类
	 * @param format
	 *            是否格式化xml流，默认false
	 * @param fragment
	 *            是否省略头信息，true-是，false-否
	 * @throws JAXBException
	 */
	public static <T> void java2xml(Node node, T t, Class<T> clazz, boolean format, boolean fragment, String encoding)
			throws JAXBException {
		java2xml(node, t, clazz, format, fragment, encoding, null, null);
	}

	/**
	 * java对象序列化为xml数据流
	 *
	 * @param node
	 *            xml DOM树
	 * @param t
	 *            java对象
	 * @param clazz
	 *            java类
	 * @param format
	 *            是否格式化xml流，默认false
	 * @param fragment
	 *            是否省略头信息，true-是，false-否
	 * @throws JAXBException
	 */
	public static <T> void java2xml(Node node, T t, Class<T> clazz, boolean format, boolean fragment)
			throws JAXBException {
		java2xml(node, t, clazz, format, fragment, DEFAULT_ENCODING);
	}

	/**
	 * java对象序列化为xml数据流
	 *
	 * @param node
	 *            xml DOM树
	 * @param t
	 *            java对象
	 * @param clazz
	 *            java类
	 * @param fragment
	 *            是否省略头信息，true-是，false-否
	 * @throws JAXBException
	 */
	public static <T> void java2xml(Node node, T t, Class<T> clazz, boolean fragment) throws JAXBException {
		java2xml(node, t, clazz, false, fragment);
	}

	/**
	 * java对象序列化为xml数据流
	 *
	 * @param node
	 *            xml DOM树
	 * @param t
	 *            java对象
	 * @param clazz
	 *            java类
	 * @throws JAXBException
	 */
	public static <T> void java2xml(Node node, T t, Class<T> clazz) throws JAXBException {
		java2xml(node, t, clazz, true);
	}

	/**
	 * xml数据转化为java对象
	 *
	 * @param xml
	 *            xml数据串
	 * @param clazz
	 *            java类
	 * @return java对象
	 * @throws JAXBException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T xml2java(String xml, Class<T> clazz)
			throws JAXBException, SAXException, ParserConfigurationException {
		JAXBContext jc = JAXBContext.newInstance(clazz);
		Unmarshaller unmar = jc.createUnmarshaller();
		StringReader reader = new StringReader(xml);

		SAXParserFactory sax = SAXParserFactory.newInstance();
		sax.setNamespaceAware(false);
		XMLReader xmlReader = sax.newSAXParser().getXMLReader();
		Source source = new SAXSource(xmlReader, new InputSource(reader));
		return (T) unmar.unmarshal(source);
	}

	/**
	 *
	 *
	 * xml数据转化为java对象
	 *
	 *
	 * @author jingwei.yang
	 * @date 2016年1月7日 下午7:26:57
	 * @param in
	 *            xml数据输入流
	 * @param clazz
	 *            java类
	 * @return java对象
	 * @throws JAXBException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T xml2java(InputStream in, Class<T> clazz) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(clazz);
		Unmarshaller unmar = jc.createUnmarshaller();
		return (T) unmar.unmarshal(in);
	}

	/**
	 * xml数据转化为java对象
	 *
	 * @author jingwei.yang
	 * @date 2016年1月7日 下午7:26:57
	 * @param file
	 *            xml数据文件
	 * @param clazz
	 *            java类
	 * @return java对象
	 * @throws JAXBException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T xml2java(File file, Class<T> clazz) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(clazz);
		Unmarshaller unmar = jc.createUnmarshaller();
		return (T) unmar.unmarshal(file);
	}

	/**
	 * xml数据转化为java对象
	 *
	 * @param reader
	 *            xml数据输入流
	 * @param clazz
	 *            java类
	 * @return java对象
	 * @throws JAXBException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T xml2java(Reader reader, Class<T> clazz) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(clazz);
		Unmarshaller unmar = jc.createUnmarshaller();
		return (T) unmar.unmarshal(reader);
	}

	/**
	 * xml数据转化为java对象
	 *
	 * @param source
	 *            xml数据源
	 * @param clazz
	 *            java类
	 * @return java对象
	 * @throws JAXBException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T xml2java(Source source, Class<T> clazz) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(clazz);
		Unmarshaller unmar = jc.createUnmarshaller();
		return (T) unmar.unmarshal(source);
	}

	/**
	 * xml数据转化为java对象
	 *
	 * @param url
	 *            xml数据网络路径
	 * @param clazz
	 *            java类
	 * @return java对象
	 * @throws JAXBException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T xml2java(URL url, Class<T> clazz) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(clazz);
		Unmarshaller unmar = jc.createUnmarshaller();
		return (T) unmar.unmarshal(url);
	}

	/**
	 * xml数据转化为java对象
	 *
	 * @param node
	 *            xml DOM树数据
	 * @param clazz
	 *            java类
	 * @return java对象
	 * @throws JAXBException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T xml2java(Node node, Class<T> clazz) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(clazz);
		Unmarshaller unmar = jc.createUnmarshaller();
		return (T) unmar.unmarshal(node);
	}
}
